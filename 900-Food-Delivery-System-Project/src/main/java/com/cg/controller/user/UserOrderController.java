package com.cg.controller.user;

import com.cg.dto.CustomerDto;
import com.cg.dto.MenuItemDto;
import com.cg.dto.OrderDto;
import com.cg.enumeration.OrderStatus;
import com.cg.enumeration.PaymentMethod;
import com.cg.enumeration.TransactionStatus;
import com.cg.model.CartItem;
import com.cg.iservice.ICustomerService;
import com.cg.iservice.IMenuItemService;
import com.cg.iservice.IOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/user/orders")
public class UserOrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IMenuItemService menuItemService;

    /* -------------------- PLACE ORDER -------------------- */
    @PostMapping("/place")
    public String placeOrder(@SessionAttribute("cart") Map<Long, CartItem> cart,
                             @RequestParam("paymentMethod") PaymentMethod paymentMethod,
                             Authentication auth,
                             RedirectAttributes redirectAttributes) { // Added RedirectAttributes
        
        CustomerDto customer = customerService.getByEmail(auth.getName());
        List<Long> itemIds = new ArrayList<>();
        double total = 0;

        for (CartItem ci : cart.values()) {
            MenuItemDto item = menuItemService.getById(ci.getItem().getItemId());
            for (int i = 0; i < ci.getQuantity(); i++) {
                itemIds.add(item.getItemId());
            }
            total += ci.getSubtotal();
        }
        
        System.out.println("Total itemIds in list: " + itemIds.size());
        System.out.println("ItemIds: " + itemIds);
        System.out.println("Total amount before delivery fee: " + total);

        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(customer.getCustomerId());
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setItemIds(itemIds);
        orderDto.setTotalAmount(total + 20); 
        orderDto.setPaymentMethod(paymentMethod);

        switch (paymentMethod) {
            case UPI:
            case NET_BANKING:
                orderDto.setTransactionStatus(TransactionStatus.SUCCESS);
                orderDto.setOrderStatus(OrderStatus.PLACED);
                break;
            case CASH_ON_DELIVERY:
                orderDto.setTransactionStatus(TransactionStatus.PENDING); 
                orderDto.setOrderStatus(OrderStatus.PLACED);
                break;
        }
         
        System.out.println("Final total with delivery fee: " + orderDto.getTotalAmount());
        
        // Assuming place returns the saved DTO with the generated ID
        OrderDto savedOrder = orderService.place(orderDto);
        
        cart.clear(); 

        // Add attributes to be used in payment-success.html
        redirectAttributes.addFlashAttribute("orderId", savedOrder.getOrderId());
        redirectAttributes.addFlashAttribute("paymentMethod", paymentMethod);
        redirectAttributes.addFlashAttribute("totalAmount", savedOrder.getTotalAmount());

        return "redirect:/user/orders/success";
    }

    // Map the success URL to the HTML file
    @GetMapping("/success")
    public String showSuccess() {
        return "user/payment-success";
    }

    /* -------------------- LIST CUSTOMER ORDERS -------------------- */
    @GetMapping
    public String myOrders(Authentication auth, Model model) {
        CustomerDto customer = customerService.getByEmail(auth.getName());
        model.addAttribute("orders", orderService.getByCustomer(customer.getCustomerId()));
        return "user/orders";
    }

    /* -------------------- VIEW ORDER DETAILS -------------------- */
    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id, Model model) throws NotFoundException {
        model.addAttribute("order", orderService.getById(id));
        return "user/order-details";
    }

    /* -------------------- CANCEL ORDER -------------------- */
    @GetMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id) {
        orderService.cancel(id);
        return "redirect:/user/orders";
    }

    /* -------------------- VIEW ORDERS BY EMAIL -------------------- */
    @GetMapping("/my-orders")
    public String viewMyOrders(Model model, Principal principal) {
        List<OrderDto> orders = orderService.getOrdersByCustomerEmail(principal.getName());
        model.addAttribute("orders", orders != null ? orders : Collections.emptyList());
        return "user/my-orders";
    }
}