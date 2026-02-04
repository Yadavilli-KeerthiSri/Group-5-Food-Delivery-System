package com.cg.controller.user;

import com.cg.service.MenuItemService;
import com.cg.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/restaurants")
public class UserRestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuItemService menuItemService;

    /* LIST */
    @GetMapping
    public String listRestaurants(Model model) {
        model.addAttribute("restaurants", restaurantService.getAll());
        return "user/restaurants";
    }

    /* VIEW MENU */
    @GetMapping("/{id}")
    public String viewMenu(@PathVariable Long id, Model model) {
        model.addAttribute("restaurant", restaurantService.getById(id));
        model.addAttribute("menuItems", menuItemService.getByRestaurant(id));
        return "user/restaurant-menu";
    }
}
