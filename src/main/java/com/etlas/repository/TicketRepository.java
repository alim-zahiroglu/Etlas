package com.etlas.repository;

import com.etlas.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
  boolean existsByPnrNo(String pnrNo);
}
