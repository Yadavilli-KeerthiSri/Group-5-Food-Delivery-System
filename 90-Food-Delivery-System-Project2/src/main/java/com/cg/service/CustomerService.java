package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cg.dto.CustomerDto;
import com.cg.entity.Customer;
import com.cg.iservice.ICustomerService;
import com.cg.mapper.CustomerMapper;
import com.cg.repository.CustomerRepository;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CustomerDto register(CustomerDto dto) {
        // DTO -> Entity (encode password here)
        Customer entity = CustomerMapper.fromCreateDto(dto, passwordEncoder::encode);
        // set default role if not provided
        if (entity.getRole() == null) {
            entity.setRole("ROLE_USER");
        }
        // Save
        Customer saved = customerRepository.save(entity);
        // Entity -> DTO
        return CustomerMapper.toDto(saved);
    }

    @Override
    public CustomerDto getById(Long id) {
        Customer entity = customerRepository.findById(id).orElseThrow();
        return CustomerMapper.toDto(entity);
    }

    @Override
    public CustomerDto getByEmail(String email) {
        Customer entity = customerRepository.findByEmail(email).orElseThrow();
        return CustomerMapper.toDto(entity);
    }

    @Override
    public List<CustomerDto> getAll() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toDto)
                .collect(Collectors.toList());
    }

    public CustomerDto saveUser(CustomerDto dto) {
        // For generic save; encode only if raw password provided
        Customer entity = CustomerMapper.fromCreateDto(dto, passwordEncoder::encode);
        Customer saved = customerRepository.save(entity);
        return CustomerMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}