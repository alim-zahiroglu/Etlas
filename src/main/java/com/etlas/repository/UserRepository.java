package com.etlas.repository;

import com.etlas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);

    List<User> findAllByIsDeleted(boolean isDeleted);

    boolean existsByUserName(String userName);
}
