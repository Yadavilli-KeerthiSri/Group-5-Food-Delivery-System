package com.cg.controller.user;

import com.cg.dto.PaymentDto;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/payment")
public class UserPaymentController {
    
    @PostMapping("/checkout")
    public String goToPayment(@RequestParam("totalAmount") double totalAmount, Model model) {
        // ✅ Do not persist anything here. Just pass amount to the view.
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setAmount(totalAmount);
        // No orderId here
        model.addAttribute("order", paymentDto);
        return "user/payment";
    }
}