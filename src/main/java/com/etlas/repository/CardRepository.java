package com.etlas.repository;

import com.etlas.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

List<Card> findAllByIsDeletedOrderByAvailableLimitTRYDesc(boolean isDeleted);
    @Query("SELECT c FROM Card c WHERE c.isDeleted = false ORDER BY c.availableLimitTRY ASC")
    List<Card> findAllNonDeletedOrderByAvailableLimitTRY();


}

