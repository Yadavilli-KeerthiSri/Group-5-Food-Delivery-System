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
// Map this controller to the base path that caused the 404 error
@RequestMapping("/admin/clients") 
public class AdminCustomerController {
    @Autowired
    private ICustomerService customerService; // Use ICustomerService interface if defined
 
    /* VIEW SPECIFIC CUSTOMER DETAILS (Fixes the 404 error) */
    @GetMapping("/{id}")
    public String viewCustomerDetails(@PathVariable("id") Long id, Model model) {
        CustomerDto customer = customerService.getById(id); // Use getById as per your service interface assumption

        if (customer == null) {
            // Handle case where customer is not found
            return "admin/customerNotFoundPage"; 
        }

        // Add the DTO to the model so the HTML template can access it
        model.addAttribute("customer", customer);
        // Return the name of the new HTML template file 
        // e.g., src/main/resources/templates/admin/customer-details.html
        return "admin/customer-details"; 
    }

    // You might also add a method here to list all customers if needed:
    @GetMapping("/list")
    public String listCustomers(Model model) {
       // List<CustomerDto> customers = customerService.getAllCustomers();
       // model.addAttribute("customers", customers);
       return "admin/customer-list";
    }
}
 