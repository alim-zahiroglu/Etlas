package com.etlas.repository;

import com.etlas.entity.VisaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisaTypeRepository extends JpaRepository<VisaType, Long> {

    @Query("SELECT v.name FROM VisaType v")
    List<String> findAllVisaTypes();

    VisaType findByName(String visaType);
}
