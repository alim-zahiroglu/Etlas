package com.etlas.repository;

import com.etlas.entity.BalanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceRecordRepository extends JpaRepository<BalanceRecord, Long> {

    List<BalanceRecord> findAllByIsDeleted(boolean isDeleted);
}
