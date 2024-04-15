package com.etlas.repository;

import com.etlas.entity.VisaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisaTypeRepository extends JpaRepository<VisaType, Long> {
    List<VisaType> findByVisaType(String visaType);
}
