package com.cg.iservice;

import java.util.List;

import com.cg.dto.OrderDto;

public interface IOrderService {

    OrderDto place(OrderDto dto);

    OrderDto updateStatus(Long orderId);

    void cancel(Long orderId);

    OrderDto getById(Long id);

    List<OrderDto> getByCustomer(Long customerId);

    List<OrderDto> getAll();

    List<OrderDto> getOrdersByCustomerEmail(String email);
}