package com.ServiceBookingSystem.SERVICES.jwt;

import com.ServiceBookingSystem.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Extract the Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if the Authorization header is present and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " prefix
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                logger.error("Error extracting username from token: {}", e.getMessage());
            }
        }

        // If a username was extracted and there's no existing authentication in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Load UserDetails from the database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validate the token
                if (jwtUtil.validateToken(token, userDetails)) {
                    // Create an Authentication object
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set the authentication in the SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Authenticated user: {}", username);
                } else {
                    logger.warn("Invalid token for user: {}", username);
                }
            } catch (Exception e) {
                logger.error("Error loading user details or validating token: {}", e.getMessage());
            }
        }
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

}
