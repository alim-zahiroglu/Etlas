package com.etlas.repository;

import com.etlas.entity.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
}
