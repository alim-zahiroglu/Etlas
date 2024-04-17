package com.etlas.repository;

import com.etlas.entity.BalanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceRecordRepository extends JpaRepository<BalanceRecord, Long> {
    List<BalanceRecord> findAllByIsDeletedOrderByLastUpdateDateTimeDesc(boolean isDeleted);

    Optional<BalanceRecord> findByIdAndIsDeletedFalse(long recordId);
}
