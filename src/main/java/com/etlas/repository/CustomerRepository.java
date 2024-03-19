package com.etlas.repository;

import com.etlas.entity.Customer;
import com.etlas.enums.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByIsDeletedOrderByLastUpdateDateTimeDesc(boolean isDeleted);
    List<Customer> findAllByIsDeletedAndCustomerType(boolean isDeleted, String customerType);
    List<Customer> findAllByCustomerTypeAndIsDeleted(CustomerType customerType, boolean isDeleted);
    boolean existsByCompanyName(String companyName);
    boolean existsByCompanyNameAndIdNot(String companyName, long id);

}
