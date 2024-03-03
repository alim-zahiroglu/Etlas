package com.etlas.dto;

import com.etlas.enums.Gender;
import com.etlas.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;

    @NotBlank(message = "first name should not be blank")
    @Size(min = 2, max = 50, message = "first name must be 2~50 character long")
    private String firstName;

    @NotBlank(message = "last name should not be blank")
    @Size(min = 2, max = 50, message = "last name must be 2~50 character long")
    private String lastName;

    @Email(message = "please enter a valid email")
    private String userName;

    @NotBlank(message = "Please enter the phone number\n ex: 05552223344")
    private String phone;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Password must be contain at least one Upper case, one lower case letter, one digit and 8 character long")
    private String passWord;
    private boolean enabled = true;
    private Role role;
    private Gender gender;
    private boolean verifyUser;
    private boolean useDefaultPassword;
    private boolean useCurrentPassword;
}
