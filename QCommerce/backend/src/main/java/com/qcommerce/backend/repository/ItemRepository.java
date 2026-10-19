package com.qcommerce.backend.repository;

import com.qcommerce.backend.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, String> {
//    Page<Item> findAllByItemNameContainingIgnoreCase(String itemName, Pageable pageable);
//
//    Page<Item> findAllByCategory_CategoryId(String categoryId,  Pageable pageable);_

    @Query("""
        SELECT i from Item i
            WHERE 
                (:search IS NULL OR :search = '' OR LOWER(i.itemName) LIKE LOWER(CONCAT('%', :search, '%'))) AND
                (:categoryId IS NULL OR :categoryId = '' OR i.category.categoryId = :categoryId) AND
                (:minPrice IS NULL OR i.itemPrice >= :minPrice) AND
                (:maxPrice IS NULL OR i.itemPrice <= :maxPrice) AND
                (:active IS NULL OR i.active = :active)
    """)
    Page<Item> searchItems(String search, String categoryId, Double minPrice, Double maxPrice, Boolean active, Pageable pageable);

    @Modifying
    @Query("UPDATE Item i SET " +
            "i.availableQuantity = i.availableQuantity - :deductBy, " +
            "i.active = CASE WHEN (i.availableQuantity - :deductBy) <= 0 THEN false ELSE i.active END " +
            "WHERE i.itemId = :itemId AND i.availableQuantity >= :deductBy")
    int decrementStock(String itemId, int deductBy);

    @Modifying
    @Query("UPDATE Item i SET " +
            "i.availableQuantity = i.availableQuantity + :restoreBy, " +
            "i.active = CASE WHEN (i.availableQuantity + :restoreBy) > 0 THEN true ELSE i.active END " +
            "WHERE i.itemId = :itemId")
    int restoreStock(String itemId, int restoreBy);
}
