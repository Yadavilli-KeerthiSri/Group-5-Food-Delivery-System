package com.cg.controller.admin;

import com.cg.dto.OrderDto;
import com.cg.service.OrderService;
import com.cg.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

	@Autowired
	private OrderService orderService; // Assuming service returns OrderDTO

	@Autowired
	private CustomerService customerService; // Assuming service returns CustomerDTO

	/* LIST ALL ORDERS */
	@GetMapping
	public String list(Model model) {
		List<OrderDto> orders = orderService.getAll();
		model.addAttribute("orders", orders);
		return "admin/order";
	}

	/* VIEW SPECIFIC ORDER + CUSTOMER DETAILS */
//    @GetMapping("/view/{id}")
//    public String viewDetails(@PathVariable Long id, Model model) {
//        // 1. Fetch the DTO
//        OrderDto orderDto = orderService.getById(id);
//        
//        // 2. Fetch Customer (Assume this is also a DTO or Entity)
//        var customer = customerService.getById(orderDto.getCustomerId());
//        
//        // 3. Add to model - names must match HTML exactly
//        model.addAttribute("order", orderDto);
//        model.addAttribute("customer", customer);
//        
//        return "admin/order"; 
//    }

	/* VIEW SPECIFIC ORDER + CUSTOMER DETAILS */
	@GetMapping("/view/{id}")
	public String viewDetails(@PathVariable Long id, Model model) {
		return "admin/order";
	}

	/* UPDATE STATUS */
	@PutMapping("/update/{id}")
	public String updateStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			orderService.updateStatus(id);
			redirectAttributes.addFlashAttribute("success", "Status updated successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Failed to update: " + e.getMessage());
		}
		return "redirect:/admin/orders";
	}
}
