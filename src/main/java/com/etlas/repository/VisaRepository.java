package com.etlas.repository;

import com.etlas.entity.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public interface VisaRepository extends JpaRepository<Visa, Long> {

    List<Visa> findAllByIsDeletedFalse();

    Optional<Visa> findByIdAndIsDeletedFalse(long visaId);

    List<Visa> findAllByIsDeletedOrderByLastUpdateDateTimeDesc(boolean isDeleted);
    @Query(value = "SELECT DISTINCT country, visa_type FROM visas WHERE is_deleted = :isDeleted", nativeQuery = true)
    List<Object[]> getAllUniqueVisaTypeAndCountry(boolean isDeleted);
}
