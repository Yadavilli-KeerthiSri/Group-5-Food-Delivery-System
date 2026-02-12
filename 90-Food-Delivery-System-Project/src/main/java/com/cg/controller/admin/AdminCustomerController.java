package com.cg.controller.admin;

import com.cg.dto.CustomerDto;

import com.cg.iservice.ICustomerService; // Use your interface if you have one

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/clients")
public class AdminCustomerController {
	@Autowired
	private ICustomerService customerService; 

	/* VIEW SPECIFIC CUSTOMER DETAILS (Fixes the 404 error) */
	@GetMapping("/{id}")
	public String viewCustomerDetails(@PathVariable("id") Long id, Model model) {
		CustomerDto customer = customerService.getById(id); 
		model.addAttribute("customer", customer);
		// e.g., src/main/resources/templates/admin/customer-details.html
		return "admin/customer-details";
	}

}
