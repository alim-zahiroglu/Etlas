package com.etlas.repository;

import com.etlas.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
  boolean existsByPnrNo(String pnrNo);

    List<Ticket> findAllByIsDeletedOrderByLastUpdateDateTimeDesc(boolean isDeleted);

    Ticket findByIdAndIsDeleted(long ticketId, boolean isDeleted);

  boolean existsByPnrNoAndIdNot(String pnrNo,long id);
}
