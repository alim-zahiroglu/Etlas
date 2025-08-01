package com.etlas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airports")
public class Airport extends BaseEntity {
    private String airportName;
    private String city;
    private String country;
    @Column(unique = true)
    private String iataCode;
}
