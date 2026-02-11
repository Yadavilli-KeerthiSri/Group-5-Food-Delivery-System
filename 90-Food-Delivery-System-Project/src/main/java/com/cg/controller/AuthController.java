package com.cg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cg.dto.CustomerDto;
import com.cg.service.CustomerService;

@Controller
public class AuthController {

    @Autowired
    private CustomerService customerService;
    
//    @Autowired
//    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(CustomerDto customer) {
        // REMOVE OR COMMENT OUT THIS LINE:
        // customer.setPassword(passwordEncoder.encode(customer.getPassword())); 

        customer.setRole("ROLE_USER");
        customerService.saveUser(customer); // The Mapper inside the service will do the hashing correctly
        return "redirect:/login";
    }
}
