package com.cg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cg.entity.DeliveryAgent;
import com.cg.entity.Order;
import com.cg.enumeration.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByCustomerCustomerId(Long customerId);

    List<Order> findByOrderStatus(OrderStatus status);
    
    List<Order> findAllByCustomer_EmailOrderByOrderDateDesc(String email);
    
    List<Order> findByOrderStatusNotIn(List<OrderStatus> statuses);
    
    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.orderStatus = :status WHERE o.orderId = :id")
    void updateStatusOnly(@Param("id") Long id, @Param("status") OrderStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.orderStatus = :status, o.deliveryAgent = :agent WHERE o.orderId = :id")
    void updateStatusAndAgent(@Param("id") Long id, @Param("status") OrderStatus status, @Param("agent") DeliveryAgent agent);
}
