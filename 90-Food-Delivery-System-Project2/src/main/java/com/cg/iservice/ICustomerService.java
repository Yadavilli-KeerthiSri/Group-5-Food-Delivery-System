package com.cg.iservice;

import com.cg.entity.Customer;

import java.util.List;

public interface ICustomerService {
	Customer register(Customer customer);

    Customer getById(Long id);

    Customer getByEmail(String email);

    List<Customer> getAll();

    void delete(Long id);
}

