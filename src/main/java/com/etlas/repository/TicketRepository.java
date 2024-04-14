package com.etlas.repository;

import com.etlas.entity.Customer;
import com.etlas.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
  boolean existsByPnrNo(String pnrNo);

    List<Ticket> findAllByIsDeletedOrderByLastUpdateDateTimeDesc(boolean isDeleted);

    Ticket findByIdAndIsDeleted(long ticketId, boolean isDeleted);

  boolean existsByPnrNoAndIdNot(String pnrNo,long id);

  boolean existsByPayedCustomerOrPassengersAndIsDeleted(Customer customer,Customer passenger, boolean isDeleted);

    boolean existsByBoughtUser_UserNameAndIsDeleted(String userName, boolean isDeleted);

  @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Ticket t WHERE (t.paidCard.id = :payedCardId) AND t.isDeleted = :isDeleted")
  boolean existsByPayedCardIdAndIsDeleted(@Param("payedCardId") long payedCardId, @Param("isDeleted") boolean isDeleted);


}
