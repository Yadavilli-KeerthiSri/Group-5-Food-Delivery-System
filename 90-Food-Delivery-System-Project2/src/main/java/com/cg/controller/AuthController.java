package com.cg.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cg.entity.Customer;
import com.cg.repository.CustomerRepository;
import com.cg.service.CustomerService;

@Controller
public class AuthController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    
    @GetMapping("/dashboard")
    public String dashboardRedirect(Authentication authentication, Model model) {
        // 1. authentication.getName() returns the 'email' you logged in with
        String email = authentication.getName();
        
        // 2. Identify the roles
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            return "admin/admin-dashboard"; 
        } 
        
        if (roles.contains("ROLE_USER")) {
            // Use your getByEmail() method to fetch customer details
            Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            
            model.addAttribute("user", customer); 
            return "user/user-dashboard"; // or your specific user dashboard
        }

        return "redirect:/login";
    }


    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(Customer customer) {
    	
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole("ROLE_USER");
        customerService.saveUser(customer);
        return "redirect:/login";
        
    }
}
