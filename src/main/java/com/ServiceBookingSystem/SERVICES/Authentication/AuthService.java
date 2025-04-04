package com.ServiceBookingSystem.SERVICES.Authentication;

import com.ServiceBookingSystem.DTO.SignupRequestDTO;
import com.ServiceBookingSystem.DTO.UserDto;

public interface AuthService {

    UserDto signupClient(SignupRequestDTO signupRequestDTO);

    Boolean presentByEmail(String email);

    UserDto signupCompany( SignupRequestDTO signupRequestDTO) ;
}
