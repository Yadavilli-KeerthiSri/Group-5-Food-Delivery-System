package com.cg.iservice;

import java.util.List;

import com.cg.entity.Order;

public interface IOrderService {
	Order place(Order order);

    Order getById(Long id);

    List<Order> getByCustomer(Long customerId);

    List<Order> getAll();

    Order updateStatus(Long orderId);

    void cancel(Long orderId);
    
    List<Order> getOrdersByCustomerEmail(String email);
}
