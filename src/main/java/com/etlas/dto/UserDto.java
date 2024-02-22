package com.etlas.dto;

import com.etlas.entity.Role;
import com.etlas.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstName;
    private String lastName;
    private String userName;
    private String phone;
    private String passWord;
    private boolean enabled;
    private Role role;
    private Gender gender;
}
