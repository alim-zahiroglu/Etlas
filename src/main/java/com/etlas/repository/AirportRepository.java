package com.etlas.repository;

import com.etlas.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport,Long> {
    Airport findByIataCode(String iataCode);
}
