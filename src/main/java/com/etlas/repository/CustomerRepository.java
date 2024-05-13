package com.etlas.repository;

import com.etlas.entity.Customer;
import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByIsDeletedOrderByLastUpdateDateTimeDesc(boolean isDeleted);

    List<Customer> findAllByIsDeletedAndCustomerType(boolean isDeleted, String customerType);

    List<Customer> findAllByCustomerTypeAndIsDeleted(CustomerType customerType, boolean isDeleted);

    boolean existsByCompanyName(String companyName);

    boolean existsByCompanyNameAndIdNot(String companyName, long id);

    Customer findByIdAndIsDeleted(long customerId, boolean isDeleted);


    @Query(value = "SELECT SUM(c.customerTRYBalance) FROM Customer c WHERE c.customerTRYBalance < 0 AND c.isDeleted =:isDeleted")
    BigDecimal getTotalTRYDebit(boolean isDeleted);

    @Query(value = "SELECT SUM(c.customerUSDBalance) FROM Customer c WHERE c.customerUSDBalance < 0 AND c.isDeleted =:isDeleted")
    BigDecimal getTotalUSDDebit(boolean isDeleted);

    @Query(value = "SELECT SUM(c.customerEURBalance) FROM Customer c WHERE c.customerEURBalance < 0 AND c.isDeleted =:isDeleted")
    BigDecimal getTotalEURDebit(boolean isDeleted);
}

