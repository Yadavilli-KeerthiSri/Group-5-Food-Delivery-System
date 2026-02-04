package com.cg.controller.user;

import com.cg.entity.Customer;
import com.cg.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/profile")
public class UserProfileController {

    @Autowired
    private CustomerService customerService;

    /* VIEW */
    @GetMapping
    public String profile(Authentication auth, Model model) {
        model.addAttribute("user",
                customerService.getByEmail(auth.getName()));
        return "user/profile";
    }

    /* EDIT FORM */
    @GetMapping("/edit")
    public String edit(Authentication auth, Model model) {
        model.addAttribute("user",
                customerService.getByEmail(auth.getName()));
        return "user/profile-edit";
    }

    /* UPDATE */
    @PostMapping("/update")
    public String update(@ModelAttribute Customer customer) {
        customerService.register(customer); // save/update
        return "redirect:/user/profile";
    }
}
