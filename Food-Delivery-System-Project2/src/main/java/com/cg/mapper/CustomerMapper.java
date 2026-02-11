package com.cg.mapper;

import java.util.function.Function;

import com.cg.dto.CustomerDto;
import com.cg.entity.Customer;

public final class CustomerMapper {

    private CustomerMapper() {}

    // ===== Read =====
    public static CustomerDto toDto(Customer entity) {
        if (entity == null) return null;
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(entity.getCustomerId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setContact(entity.getContact());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setPassword(entity.getPassword()); // Only if you truly need it in DTO
        dto.setRole(entity.getRole());
        return dto;
    }

    // ===== Create =====
    public static Customer fromCreateDto(CustomerDto dto, Function<String, String> passwordEncoder) {
        if (dto == null) return null;
        Customer entity = new Customer();
        entity.setCustomerId(dto.getCustomerId()); // usually null on create
        entity.setCustomerName(dto.getCustomerName());
        entity.setContact(dto.getContact());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setRole(dto.getRole());

        String rawPwd = dto.getPassword();
        entity.setPassword(passwordEncoder != null && rawPwd != null ? passwordEncoder.apply(rawPwd) : rawPwd);
        return entity;
    }

    // ===== Update =====
    public static void applyUpdate(CustomerDto dto, Customer target) {
        if (dto == null || target == null) return;
        target.setCustomerName(dto.getCustomerName());
        target.setContact(dto.getContact());
        target.setAddress(dto.getAddress());
        // email/password/role are updatable=false in entity → do not modify here
    }

    // ===== Delete helper =====
    public static Customer buildWithId(Long id) {
        if (id == null) return null;
        Customer e = new Customer();
        e.setCustomerId(id);
        return e;
    }
}
