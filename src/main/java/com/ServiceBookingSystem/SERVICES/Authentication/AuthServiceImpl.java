package com.ServiceBookingSystem.SERVICES.Authentication;

import com.ServiceBookingSystem.DTO.SignupRequestDTO;
import com.ServiceBookingSystem.DTO.UserDto;
import com.ServiceBookingSystem.ENTITY.User;
import com.ServiceBookingSystem.ENUMS.UserRole;
import com.ServiceBookingSystem.REPOSITORY.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements  AuthService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto signupClient(SignupRequestDTO signupRequestDTO) {
        User user=new User();
        user.setName(signupRequestDTO.getName());
        user.setLastname(signupRequestDTO.getLastname());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPhone(signupRequestDTO.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));


        user.setRole(UserRole.CLIENT);

        return userRepository.save(user).getDto();
    }

     @Override
    public UserDto signupCompany(SignupRequestDTO signupRequestDTO) {
        User user = new User();
        user.setName(signupRequestDTO.getName());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPhone(signupRequestDTO.getPhone());
        user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        user.setRole(UserRole.COMPANY);


        System.out.println("Role being set for company: " + user.getRole());

        return userRepository.save(user).getDto();
    }


    @Override
    public Boolean presentByEmail(String email) {
        return userRepository.findFirstByEmail(email) != null;
    }
}
