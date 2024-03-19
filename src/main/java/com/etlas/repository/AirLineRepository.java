package com.etlas.repository;


import com.etlas.entity.AirLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirLineRepository extends JpaRepository<AirLine, Long> {
    AirLine findByName(String airLineName);
}
