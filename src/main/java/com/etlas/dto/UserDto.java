package com.etlas.dto;

import com.etlas.enums.Gender;
import com.etlas.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String phone;
    private String passWord;
    private boolean enabled;
    private Role role;
    private Gender gender;
    private boolean verifyUser;
}
