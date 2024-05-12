package com.etlas.repository;

import com.etlas.entity.Visa;
import com.etlas.enums.CurrencyUnits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisaRepository extends JpaRepository<Visa, Long> {

    List<Visa> findAllByIsDeletedFalse();

    Optional<Visa> findByIdAndIsDeletedFalse(long visaId);

    List<Visa> findAllByIsDeletedOrderByLastUpdateDateTimeDesc(boolean isDeleted);
    @Query(value = "SELECT DISTINCT country, visa_type FROM visas WHERE is_deleted = :isDeleted", nativeQuery = true)
    List<Object[]> getAllUniqueVisaTypeAndCountry(boolean isDeleted);

    @Query(value = "SELECT DISTINCT country, visa_type FROM visas WHERE paid_customer_id = :customerId AND is_deleted = :isDeleted",nativeQuery = true)
    List<Object[]> getAllUniqueVisaTypeAndCountryFromCustomer(long customerId, boolean isDeleted);

    boolean existsByBoughtUser_UserNameAndIsDeleted(String userName, boolean isDeleted);

    @Query(value = "SELECT CASE WHEN EXISTS ( " +
            "    SELECT 1 " +
            "    FROM visas v " +
            "    WHERE (v.customer_id = :customerId OR v.paid_customer_id = :paidCustomerId) " +
            "    AND v.is_deleted = :isDeleted " +
            ") THEN true ELSE false END", nativeQuery = true)
    boolean existsByCustomerIdOrPaidCustomerIdAndIsDeleted(@Param("customerId") long customerId,
                                                           @Param("paidCustomerId") long paidCustomerId,
                                                           @Param("isDeleted") boolean isDeleted);

    boolean existsByPaidCardIdAndIsDeleted(long curdId, boolean isDeleted);

    @Query(value = "SELECT SUM(v.perchesPrice) FROM Visa v WHERE v.currencyUnit =:currencyUnits AND MONTH(v.dateOfPerches) =:month AND v.isDeleted =:isDeleted")
    BigDecimal getVisaTotalPerchesByMonth(CurrencyUnits currencyUnits, int month, boolean isDeleted);

    @Query(value = "SELECT SUM(v.perchesPrice) FROM Visa v WHERE v.currencyUnit =:currencyUnits AND YEAR (v.dateOfPerches) =:year AND v.isDeleted =:isDeleted")
    BigDecimal getVisaTotalPerchesByYear(CurrencyUnits currencyUnits, int year, boolean isDeleted);

    @Query(value = "SELECT SUM(v.perchesPrice) FROM Visa v WHERE v.currencyUnit =:currencyUnits AND v.isDeleted =:isDeleted")
    BigDecimal getVisaTotalPerches(CurrencyUnits currencyUnits, boolean isDeleted);



    @Query(value = "SELECT SUM(v.salesPrice) FROM Visa v WHERE v.currencyUnit =:currencyUnits AND MONTH(v.dateOfPerches) =:month AND v.isDeleted =:isDeleted")
    BigDecimal getVisaTotalSalesByMonth(CurrencyUnits currencyUnits, int month, boolean isDeleted);

    @Query(value = "SELECT SUM(v.salesPrice) FROM Visa v WHERE v.currencyUnit =:currencyUnits AND YEAR (v.dateOfPerches) =:year AND v.isDeleted =:isDeleted")
    BigDecimal getVisaTotalSalesByYear(CurrencyUnits currencyUnits, int year, boolean isDeleted);

    @Query(value = "SELECT SUM(v.salesPrice) FROM Visa v WHERE v.currencyUnit =:currencyUnits AND v.isDeleted =:isDeleted")
    BigDecimal getVisaTotalSales(CurrencyUnits currencyUnits, boolean isDeleted);



    @Query(value = "SELECT SUM(v.profit) FROM Visa v WHERE v.currencyUnit =:currencyUnits AND MONTH(v.dateOfPerches) =:month AND v.isDeleted =:isDeleted")
    BigDecimal getVisaTotalProfitByMonth(CurrencyUnits currencyUnits, int month, boolean isDeleted);

    @Query(value = "SELECT SUM(v.profit) FROM Visa v WHERE v.currencyUnit =:currencyUnits AND YEAR (v.dateOfPerches) =:year AND v.isDeleted =:isDeleted")
    BigDecimal getVisaTotalProfitByYear(CurrencyUnits currencyUnits, int year, boolean isDeleted);

    @Query(value = "SELECT SUM(v.profit) FROM Visa v WHERE v.currencyUnit =:currencyUnits AND v.isDeleted =:isDeleted")
    BigDecimal getVisaTotalProfit(CurrencyUnits currencyUnits, boolean isDeleted);
}
