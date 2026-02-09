package com.cg.iservice;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.cg.dto.OrderDto;
import com.cg.entity.DeliveryAgent;
import com.cg.entity.Order;

public interface IOrderService {

    OrderDto place(OrderDto dto);

    OrderDto updateStatus(Long orderId);

    void cancel(Long orderId);

    OrderDto getById(Long id) throws NotFoundException;

    List<OrderDto> getByCustomer(Long customerId);

    List<OrderDto> getAll();

    List<OrderDto> getOrdersByCustomerEmail(String email);
    
    OrderDto createOrder(OrderDto newOrderDto);
    
    List<DeliveryAgent> getAvailableAgents();
    
    OrderDto map(Order order);

}