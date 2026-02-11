package com.cg.iservice;

import java.util.List;

import com.cg.dto.CustomerDto;

public interface ICustomerService {

    CustomerDto register(CustomerDto dto);

    CustomerDto getById(Long id);

    CustomerDto getByEmail(String email);

    List<CustomerDto> getAll();

    void delete(Long id);
}
