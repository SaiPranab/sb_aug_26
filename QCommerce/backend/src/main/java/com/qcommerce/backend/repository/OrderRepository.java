package com.qcommerce.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qcommerce.backend.entity.OrderEntity;
import com.qcommerce.backend.entity.helper.OrderStatus;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    Page<OrderEntity> findByUserUserId(String userId, Pageable pageable);

    @Query("SELECT o FROM OrderEntity o WHERE " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(o.orderId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(o.user.userEmail) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(o.user.contactInfo.fullName) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:status IS NULL OR o.status = :status)")
    Page<OrderEntity> searchOrders(
            @Param("search") String search,
            @Param("status") OrderStatus status,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM OrderEntity o WHERE o.status = 'DELIVERED'")
    Double getTotalRevenue();

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM OrderEntity o WHERE o.user.userId = :userId AND o.status = 'DELIVERED'")
    Double getTotalPurchaseByUser(@Param("userId") String userId);
}
