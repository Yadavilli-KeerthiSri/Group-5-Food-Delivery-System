package com.cg.controller.admin;

import com.cg.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    /* LIST */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.getAll());
        return "admin/order";
    }

    /* UPDATE STATUS */
    @PostMapping("/update/{id}")
    public String updateStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.updateStatus(id);
            redirectAttributes.addFlashAttribute("success", "Order status updated successfully!");
        } catch (Exception e) {
            // This sends the error message back to the UI instead of crashing
            redirectAttributes.addFlashAttribute("error", "Failed to update: " + e.getMessage());
        }
        return "redirect:/admin/orders";
    }
    
    @GetMapping("/admin/orders")
    public String viewOrders(Model model) {
        model.addAttribute("orders", orderService.getAll());
        return "admin/order";
    }
}
