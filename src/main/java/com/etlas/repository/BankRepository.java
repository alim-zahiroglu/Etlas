package com.etlas.repository;

import com.etlas.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    @Query("SELECT b.bankName FROM Bank b ORDER BY b.bankName ASC")
    List<String> findAllByOrderByBankNameAsc();

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM bank_names WHERE LOWER(bank_name) = LOWER(:bankName)", nativeQuery = true)
    boolean existsByBankName(@Param("bankName") String bankName);

}
