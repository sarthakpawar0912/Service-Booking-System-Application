package com.ServiceBookingSystem.Controller;

import com.ServiceBookingSystem.DTO.AuthenticationRequest;
import com.ServiceBookingSystem.DTO.SignupRequestDTO;
import com.ServiceBookingSystem.DTO.UserDto;
import com.ServiceBookingSystem.ENTITY.User;
import com.ServiceBookingSystem.REPOSITORY.UserRepository;
import com.ServiceBookingSystem.SERVICES.Authentication.AuthService;
import com.ServiceBookingSystem.SERVICES.jwt.UserDetailsServiceImpl;
import com.ServiceBookingSystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/client/sign-up")
    public ResponseEntity<?> signupClient(@RequestBody SignupRequestDTO signupRequestDTO) {
        if (authService.presentByEmail(signupRequestDTO.getEmail())) {
            return new ResponseEntity<>("Client already exists with this email!", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto createdUser = authService.signupClient(signupRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PostMapping("/company/sign-up")
    public ResponseEntity<?> signupCompany(@RequestBody SignupRequestDTO signupRequestDTO) {
        if (authService.presentByEmail(signupRequestDTO.getEmail())) {
            return new ResponseEntity<>("Company already exists with this email!", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto createdUser = authService.signupCompany(signupRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Incorrect username or password"));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        // Fetch User from Database to Get ID & Role
        User user = userRepository.findFirstByEmail(authenticationRequest.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("userId", user.getId()); // ✅ Returning Correct ID
        responseBody.put("role", user.getRole().name()); // ✅ Role Included
        responseBody.put("token", jwt);

        return ResponseEntity.ok(responseBody);
    }

}
