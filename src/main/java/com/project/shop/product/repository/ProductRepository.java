package com.project.shop.product.repository;

import com.project.shop.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {


    @Modifying
    @Query("update Product p set p.viewCount = p.viewCount + 1 where p.id = :id")
    void incrementViewCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Product p SET p.viewCount = p.viewCount + :count WHERE p.id = :id")
    void incrementViewCountBatch(@Param("id") Long id, @Param("count") Long count);
}
