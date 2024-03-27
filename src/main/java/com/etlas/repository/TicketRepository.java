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

    boolean existsByBoughtUser_UserNameOrReceivedUser_UserNameAndIsDeleted(String userName, String userName1, boolean isDeleted);

    boolean existsByPaidCard_IdOrReceivedCard_IdAndIsDeleted(long payedCardId, long receivedCardId, boolean isDeleted);

  @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Ticket t WHERE (t.paidCard.id = :payedCardId OR t.receivedCard.id = :receivedCardId) AND t.isDeleted = :isDeleted")
  boolean existsByPayedCardIdOrReceivedCardIdAndIsDeleted(@Param("payedCardId") long payedCardId, @Param("receivedCardId") long receivedCardId, @Param("isDeleted") boolean isDeleted);


}
