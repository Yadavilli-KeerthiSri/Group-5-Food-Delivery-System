package com.cg.iservice;

import java.util.List;

import com.cg.entity.Payment;

public interface IPaymentService {
	Payment makePayment(Payment payment);

    Payment getById(Long id);

    List<Payment> getAll();
}
