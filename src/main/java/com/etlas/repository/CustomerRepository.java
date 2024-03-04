package com.etlas.repository;

import com.etlas.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByIsDeleted(boolean deleted);

    boolean existsByCompanyName(String companyName);
}
