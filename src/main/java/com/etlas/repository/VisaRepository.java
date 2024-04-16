package com.etlas.repository;

import com.etlas.entity.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface VisaRepository extends JpaRepository<Visa, Long> {

    List<Visa> findAllByIsDeletedFalse();

    Optional<Visa> findByIdAndIsDeletedFalse(long visaId);
}
