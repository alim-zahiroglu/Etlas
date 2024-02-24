package com.etlas.entity;

import com.etlas.enums.Gender;
import com.etlas.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
//@Where(clause = "is_deleted = false")
public class User extends BaseEntity {
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String userName;
    private String phone;

    private String passWord;
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Gender gender;

}
