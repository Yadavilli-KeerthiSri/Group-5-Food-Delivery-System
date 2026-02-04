package com.cg.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cg.entity.Order;
import com.cg.entity.Payment;
import com.cg.enumeration.PaymentMethod;
import com.cg.enumeration.TransactionStatus;
import com.cg.service.OrderService;
import com.cg.service.PaymentService;

@Controller
@RequestMapping("/user/payment")
public class UserPaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/pay")
    public String pay(@RequestParam PaymentMethod paymentMethod,
                      @RequestParam Long orderId) {

        Order order = orderService.getById(orderId);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(order.getTotalAmount());
        payment.setTransactionStatus(TransactionStatus.SUCCESS);

        paymentService.makePayment(payment);

        return "redirect:/user/payment/success";
    }

    @GetMapping("/success")
    public String success() {
        return "user/payment-success";
    }

    @GetMapping("/failure")
    public String failure() {
        return "user/payment-failure";
    }
}

