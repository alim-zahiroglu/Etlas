package com.etlas.repository;

import com.etlas.entity.User;
import com.etlas.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserNameAndIsDeleted(String username, Boolean isDeleted);

    List<User> findAllByIsDeleted(boolean isDeleted);

    boolean existsByUserName(String userName);

    List<User> findAllByRoleAndIsDeleted(Role role, boolean isDeleted);
}
