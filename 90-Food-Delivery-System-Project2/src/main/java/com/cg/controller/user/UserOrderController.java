package com.cg.controller.user;

import com.cg.entity.Customer;
import com.cg.entity.MenuItem;
import com.cg.entity.Order;
import com.cg.enumeration.OrderStatus;
import com.cg.model.CartItem;
import com.cg.service.CustomerService;
import com.cg.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/orders")
public class UserOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    /* CREATE (Place Order) */
    @PostMapping("/place")
    public String placeOrder(
            @SessionAttribute("cart") Map<Long, CartItem> cart,
            @RequestParam("paymentMethod") String paymentMethod, // Capture the radio button value
            Authentication auth) {

        Customer customer = customerService.getByEmail(auth.getName());

        Order order = new Order();
        order.setCustomer(customer);
        
        // 1. IMPORTANT: Set the timestamp for the timeline
        order.setOrderDate(LocalDateTime.now());
        
        // 2. IMPORTANT: Set the initial status based on your Enum/Logic
        order.setOrderStatus(OrderStatus.PLACED);
        
        // 3. Set the payment method from the form
        // Note: You'll need to add a 'paymentMethod' field to your Order Entity if not there
        // order.setPaymentMethod(paymentMethod);

        List<MenuItem> items = new ArrayList<>();
        double total = 0;

        for (CartItem ci : cart.values()) {
            // Adding items to the list
            for (int i = 0; i < ci.getQuantity(); i++) {
                items.add(ci.getItem());
            }
            total += ci.getSubtotal();
        }

        // Add service fee (matching your HTML summary)
        order.setTotalAmount(total + 20);
        order.setItems(items);

        orderService.place(order);
        
        // Clear the cart session
        cart.clear();

        // 4. THE FIX: Redirect to the specific 'my-orders' mapping to avoid 400/500 errors
        return "redirect:/user/orders/my-orders";
    }

    /* LIST */
    @GetMapping
    public String myOrders(Authentication auth, Model model) {

        Customer customer = customerService.getByEmail(auth.getName());
        model.addAttribute(
                "orders",
                orderService.getByCustomer(customer.getCustomerId())
        );
        return "user/orders";
    }

    /* VIEW */
    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getById(id));
        return "user/order-details";
    }

    /* DELETE (Cancel Order) */
    @GetMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id) {
        orderService.cancel(id);
        return "redirect:/user/orders";
    }
    
    @GetMapping("/my-orders")
    public String viewMyOrders(Model model, Principal principal) {
        List<Order> orders = orderService.getOrdersByCustomerEmail(principal.getName());
        model.addAttribute("orders", orders);
        return "user/my-orders";
    }
    
 // BUTTON 1: "Proceed to Payment" (Online)
    @PostMapping("/user/payment/online")
    public String startOnlinePayment(@RequestParam("totalAmount") double total) {
        // Redirect to a specific payment processing page or gateway
        return "redirect:/user/payment/gateway?amt=" + total;
    }

 // BUTTON 2: "Place COD Order"
    @PostMapping("/user/orders/place")
    public String placeCodOrder(@RequestParam("totalAmount") double total, 
                                @AuthenticationPrincipal UserDetails userDetails) {
        
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(total);
        order.setOrderStatus(OrderStatus.PENDING); // From your OrderStatus enum

        // Link the customer using their email (matches your repo method findAllByCustomer_Email...)
        if (userDetails != null) {
            order.setCustomer(customerRepository.findByEmail(userDetails.getUsername()));
        }
     // MySQL Insert: This triggers the 'orders' and 'order_items' table population
        orderRepository.save(order);

        return "redirect:/user/dashboard?success=order_placed";
    }
}
