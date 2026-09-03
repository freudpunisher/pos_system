package com.example.pos.repository;

import com.example.pos_system.entity.Sale;
import com.example.pos_system.entity.SaleStatus;
//import com.example.pos_system.entity.Sales;
import com.example.pos_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findBySaleNumber(String saleNumber);

    List<Sale> findAllByCashier(User cashier);

    List<Sale> findAllByStatus(SaleStatus status);

    List<Sale> findAllByCompletedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(s.total) FROM Sale s WHERE s.status = 'COMPLETED' AND s.completedAt BETWEEN :start AND :end")
    BigDecimal findTotalSalesBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.status = 'COMPLETED' AND s.completedAt BETWEEN :start AND :end")
    Long countCompletedSalesBetween(LocalDateTime start, LocalDateTime end);
}