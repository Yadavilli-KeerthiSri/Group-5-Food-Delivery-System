package com.cg.controller.user;

import com.cg.dto.CustomerDto;
import com.cg.iservice.ICustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/profile")
public class UserProfileController {

    private final ICustomerService customerService;

    @Autowired
    public UserProfileController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    /** VIEW PROFILE */
    @GetMapping
    public String profile(Authentication auth, Model model) {
        CustomerDto user = customerService.getByEmail(auth.getName());
        model.addAttribute("user", user);
        return "user/profile";
    }

    /** EDIT PROFILE FORM */
    @GetMapping("/edit")
    public String edit(Authentication auth, Model model) {
        CustomerDto user = customerService.getByEmail(auth.getName());
        model.addAttribute("user", user);
        return "user/profile-edit";
    }

    /**
     * UPDATE PROFILE
     * Notes:
     * - This uses the same DTO you show in the edit form.
     * - If your form does not include password, ensure the service preserves the existing password.
     * - Your Customer entity has updatable=false for some fields; ensure your service/mapper respects that.
     */
    @PostMapping("/update")
    public String update(@ModelAttribute("user") CustomerDto userDto, Authentication auth) {
        // Enforce current user email to prevent tampering (if email is non-updatable)
        CustomerDto current = customerService.getByEmail(auth.getName());
        userDto.setCustomerId(current.getCustomerId());
        userDto.setEmail(current.getEmail());   // Keep immutable if your entity marks it updatable=false
        userDto.setRole(current.getRole());     // Keep immutable if your entity marks it updatable=false

        // Use service to persist changes (your service maps DTO -> entity and saves)
        customerService.register(userDto);

        return "redirect:/user/profile";
    }
}