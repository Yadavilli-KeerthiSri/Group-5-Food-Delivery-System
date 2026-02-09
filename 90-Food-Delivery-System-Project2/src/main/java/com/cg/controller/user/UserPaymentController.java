package com.cg.controller.user;

import com.cg.dto.CustomerDto;
import com.cg.dto.PaymentDto;
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

//    @PostMapping("/pay")
//    public String pay(@RequestParam PaymentMethod paymentMethod, 
//                      @RequestParam Long orderId, 
//                      Principal principal) { // Add Principal to get the logged-in user
//        
//        // 1. Fetch existing order
//        OrderDto orderDto = orderService.getById(orderId);
//        
//        // 2. Fetch the Customer using the principal email
//        // You need to find your customer ID based on principal.getName()
//        // Customer customer = customerRepository.findByEmail(principal.getName());
//        // orderDto.setCustomerId(customer.getCustomerId());
//
//        // 3. Finalise and Save via your Service
//        orderDto.setOrderStatus(OrderStatus.PLACED);
//        orderDto.setPaymentMethod(paymentMethod);
//        orderDto.setTransactionStatus(TransactionStatus.SUCCESS);
//        
//        orderService.createOrder(orderDto); // Ensure your service links the customerId to the Entity
//
//        return "redirect:/user/payment/success";
//    }
//    
//    @GetMapping("/success")
//    public String paymentSuccess() {
//        // Path: src/main/resources/templates/user/payment-success.html
//        return "user/payment-success";
//    }

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