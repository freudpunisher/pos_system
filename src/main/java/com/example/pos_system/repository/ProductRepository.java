package com.example.pos.repository;

import com.example.pos_system.entity.Product;
import com.example.pos_system.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    List<Product> findAllByActiveTrue();

    List<Product> findAllByCategory(Category category);

    List<Product> findAllByQuantityInStockLessThan(Integer reorderLevel);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Product> searchByKeyword(String keyword);

    List<Product> findAllByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    boolean existsBySku(String sku);
}