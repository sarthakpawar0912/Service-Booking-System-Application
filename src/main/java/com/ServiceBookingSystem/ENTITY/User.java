package com.ServiceBookingSystem.ENTITY;

import com.ServiceBookingSystem.DTO.UserDto;
import com.ServiceBookingSystem.ENUMS.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


    private String name;

    private String lastname;

    private String phone;


    @Enumerated(EnumType.STRING) // ✅ Ensure Enum is stored as STRING
    @Column(nullable = false)
    private UserRole role;


    public UserDto getDto() {
        UserDto userDto= new UserDto();
        userDto.setId(id); // ✅ Ensure ID is set correctly
        userDto.setName(name);
        userDto.setEmail(email);
        userDto.setRole(role);
        return userDto;
    }
}
