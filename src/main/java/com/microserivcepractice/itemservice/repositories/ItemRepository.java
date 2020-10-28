package com.microserivcepractice.itemservice.repositories;

import com.microserivcepractice.itemservice.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query(value = "SELECT CASE WHEN it.stock - :qnt >= 0 THEN TRUE ELSE FALSE " +
            "END as enough FROM Item as it WHERE it.id = :id")
    Optional<Boolean> validateQntById(@Param("id") int id, @Param("qnt") int qnt);

    @Modifying(flushAutomatically = true)
    @Query(value = "UPDATE Item SET stock = stock - :qnt WHERE id = :id AND stock - :qnt > 0")
    Integer updateItem(@Param("id") int id, @Param("qnt") int qnt);

    Optional<Item> findByIdAndStockIsGreaterThan(Integer id, Integer qnt);

    List<Item> findAllByStockGreaterThanEqual(int qnt);
}
