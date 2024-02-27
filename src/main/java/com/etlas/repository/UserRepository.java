package com.etlas.repository;

import com.etlas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);

    List<User> findAllByIsDeleted(boolean isDeleted);

    boolean existsByUserName(String userName);
}
