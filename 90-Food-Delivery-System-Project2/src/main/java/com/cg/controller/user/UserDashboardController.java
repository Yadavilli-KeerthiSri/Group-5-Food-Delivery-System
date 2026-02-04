package com.cg.controller.user;

import com.cg.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserDashboardController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("restaurants", restaurantService.getAll());
        return "user/user-dashboard";
    }
}
