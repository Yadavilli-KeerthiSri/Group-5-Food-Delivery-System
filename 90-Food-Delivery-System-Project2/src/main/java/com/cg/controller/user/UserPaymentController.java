package com.cg.controller.user;

import com.cg.dto.CustomerDto;
import com.cg.dto.OrderDto;
import com.cg.dto.PaymentDto;
import com.cg.enumeration.PaymentMethod;
import com.cg.enumeration.TransactionStatus;
import com.cg.service.CustomerService;
import com.cg.service.OrderService;
import com.cg.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/payment")
public class UserPaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CustomerService customerService;

    @PostMapping("/pay")
    public String pay(@RequestParam PaymentMethod paymentMethod,
                      @RequestParam Long orderId) {

        OrderDto order = orderService.getById(orderId);

        PaymentDto payment = new PaymentDto();
        payment.setOrderId(order.getOrderId());
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(order.getTotalAmount());
        payment.setTransactionStatus(TransactionStatus.SUCCESS);
        
        paymentService.makePayment(payment);
        
		return "redirect:/user/payment/success";
    }

    /* VIEW */
    @GetMapping
    public String profile(Authentication auth, Model model) {
        model.addAttribute("user", customerService.getByEmail(auth.getName()));
        return "user/profile-edit";
    }

    /* UPDATE */
    @PostMapping("/update")
    public String update(@ModelAttribute CustomerDto dto) {
        customerService.register(dto); // register() acts as create/update
        return "redirect:/user/profile";
    }
}