package com.etlas.repository;

import com.etlas.entity.Customer;
import com.etlas.entity.Ticket;
import com.etlas.enums.CurrencyUnits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
  boolean existsByPnrNo(String pnrNo);

    List<Ticket> findAllByIsDeletedOrderByLastUpdateDateTimeDesc(boolean isDeleted);

    Ticket findByIdAndIsDeleted(long ticketId, boolean isDeleted);

  boolean existsByPnrNoAndIdNot(String pnrNo,long id);

  boolean existsByPayedCustomerOrPassengersContainingAndIsDeleted(Customer customer,Customer passenger, boolean isDeleted);

  boolean existsByBoughtUser_UserNameAndIsDeleted(String userName, boolean isDeleted);

  @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Ticket t WHERE (t.paidCard.id = :payedCardId) AND t.isDeleted = :isDeleted")
  boolean existsByPayedCardIdAndIsDeleted(@Param("payedCardId") long payedCardId, @Param("isDeleted") boolean isDeleted);


    List<Ticket> findAllByPayedCustomer_IdAndIsDeleted(long customerId, boolean IsDeleted);

  @Query(value = "SELECT CASE WHEN EXISTS ( " +
          "    SELECT 1 " +
          "    FROM tickets t " +
          "    WHERE (t.payed_customer_id = :customerId OR :passengerId IN ( " +
          "              SELECT p.id " +
          "              FROM tickets t2 " +
          "              JOIN tickets_passengers tp ON t2.id = tp.ticket_id " +
          "              JOIN customers p ON tp.passengers_id = p.id " +
          "          )) " +
          "    AND t.is_deleted = :isDeleted " +
          ") THEN true ELSE false END", nativeQuery = true)
  boolean existsByPayedCustomerOrPassengersContainingAndIsDeleted(@Param("customerId") Long customerId,
                                                                  @Param("passengerId") Long passengerId,
                                                                  @Param("isDeleted") boolean isDeleted);

  @Query(value = "SELECT SUM(t.perchesPrice) FROM Ticket t WHERE t.currencyUnit =:currencyUnit AND MONTH(t.dateOfPerches) =:month AND t.isDeleted =:isDeleted")
  BigDecimal getTicketTotalPerchesByMonth(CurrencyUnits currencyUnit, int month, boolean isDeleted);

  @Query(value = "SELECT SUM(t.perchesPrice) FROM Ticket t WHERE t.currencyUnit =:currencyUnit AND YEAR (t.dateOfPerches) =:year AND t.isDeleted =:isDeleted")
  BigDecimal getTicketTotalPerchesByYear(CurrencyUnits currencyUnit, int year, boolean isDeleted);

  @Query(value = "SELECT SUM(t.perchesPrice) FROM Ticket t WHERE t.currencyUnit =:currencyUnit AND t.isDeleted =:isDeleted")
  BigDecimal getTicketTotalPerches(CurrencyUnits currencyUnit, boolean isDeleted);



  @Query(value = "SELECT SUM(t.salesPrice) FROM Ticket t WHERE t.currencyUnit =:currencyUnits AND MONTH(t.dateOfPerches) =:month AND t.isDeleted =:isDeleted")
  BigDecimal getTicketTotalSalesByMonth(CurrencyUnits currencyUnits, int month, boolean isDeleted);

  @Query(value = "SELECT SUM(t.salesPrice) FROM Ticket t WHERE t.currencyUnit =:currencyUnits AND YEAR (t.dateOfPerches) =:year AND t.isDeleted =:isDeleted")
  BigDecimal getTicketTotalSalesByYear(CurrencyUnits currencyUnits, int year, boolean isDeleted);

  @Query(value = "SELECT SUM(t.salesPrice) FROM Ticket t WHERE t.currencyUnit =:currencyUnits AND t.isDeleted =:isDeleted")
  BigDecimal getTicketTotalSales(CurrencyUnits currencyUnits, boolean isDeleted);


  @Query(value = "SELECT SUM(t.profit) FROM Ticket t WHERE t.currencyUnit =:currencyUnits AND MONTH(t.dateOfPerches) =:month AND t.isDeleted =:isDeleted")
  BigDecimal getTicketTotalProfitByMonth(CurrencyUnits currencyUnits, int month, boolean isDeleted);

  @Query(value = "SELECT SUM(t.profit) FROM Ticket t WHERE t.currencyUnit =:currencyUnits AND YEAR (t.dateOfPerches) =:year AND t.isDeleted =:isDeleted")
  BigDecimal getTicketTotalProfitByYear(CurrencyUnits currencyUnits, int year, boolean isDeleted);

  @Query(value = "SELECT SUM(t.profit) FROM Ticket t WHERE t.currencyUnit =:currencyUnits AND t.isDeleted =:isDeleted")
  BigDecimal getTicketTotalProfit(CurrencyUnits currencyUnits, boolean isDeleted);


  @Query(value = "SELECT SUM(t.ticketAmount) FROM Ticket t WHERE t.currencyUnit =:currencyUnits AND MONTH(t.dateOfPerches) =:month AND t.isDeleted =:isDeleted")
  Integer getTotalPerchesTicketByMonth(CurrencyUnits currencyUnits, int month, boolean isDeleted);

  @Query(value = "SELECT SUM(t.ticketAmount) FROM Ticket t WHERE t.currencyUnit =:currencyUnits AND YEAR (t.dateOfPerches) =:year AND t.isDeleted =:isDeleted")
  Integer getTotalPerchesTicketByYear(CurrencyUnits currencyUnits, int year, boolean isDeleted);

  @Query(value = "SELECT SUM(t.ticketAmount) FROM Ticket t WHERE t.currencyUnit =:currencyUnits AND t.isDeleted =:isDeleted")
  Integer getTotalPerchesTicket(CurrencyUnits currencyUnits, boolean isDeleted);
}
