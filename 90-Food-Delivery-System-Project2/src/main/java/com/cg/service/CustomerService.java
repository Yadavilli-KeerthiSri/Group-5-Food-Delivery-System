package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cg.entity.Customer;
import com.cg.iservice.ICustomerService;
import com.cg.repository.CustomerRepository;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Customer register(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole("ROLE_USER");
        return customerRepository.save(customer);
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id).orElseThrow();
    }

    @Override
    public Customer getByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }
    
    
    public Customer saveUser(Customer customer) {
    	return customerRepository.save(customer);
    }

    @Override
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}
