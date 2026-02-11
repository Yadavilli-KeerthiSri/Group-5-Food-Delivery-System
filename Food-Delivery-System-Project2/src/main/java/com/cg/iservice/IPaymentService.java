package com.cg.iservice;

import java.util.List;

import com.cg.dto.PaymentDto;

public interface IPaymentService {

	void makePayment(PaymentDto dto);

    PaymentDto getById(Long id);

    List<PaymentDto> getAll();
}