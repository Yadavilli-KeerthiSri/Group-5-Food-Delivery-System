package com.cg.controller.user;

import com.cg.dto.MenuItemDto;
import com.cg.dto.RestaurantDto;
import com.cg.iservice.IMenuItemService;
import com.cg.iservice.IRestaurantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/restaurants")
public class UserRestaurantController {

    private final IRestaurantService restaurantService;
    private final IMenuItemService menuItemService;

    @Autowired
    public UserRestaurantController(IRestaurantService restaurantService,
                                    IMenuItemService menuItemService) {
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
    }

    /** LIST (View All page with optional filters) */
    @GetMapping
    public String listRestaurants(
            @RequestParam(name = "filter", required = false, defaultValue = "all") String filterType,
            Model model) {

        List<RestaurantDto> restaurants = "top".equalsIgnoreCase(filterType)
                ? restaurantService.findTopRated()
                : restaurantService.getAll();

        model.addAttribute("restaurants", restaurants);
        return "user/restaurants";
    }

    /** VIEW MENU for a restaurant */
    @GetMapping("/{id}")
    public String viewMenu(@PathVariable Long id, Model model) {
        RestaurantDto restaurant = restaurantService.getById(id);
        List<MenuItemDto> menuItems = menuItemService.getByRestaurant(id);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menuItems", menuItems);
        return "user/restaurant-menu";
    }

    /** DASHBOARD (Brought back from previous version) */
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Uses the specific dashboard logic (Top 6 with fallback)
        List<RestaurantDto> topRestaurants = restaurantService.findTopForDashboard();
        model.addAttribute("restaurants", topRestaurants);
        return "user/user-dashboard";
    }
}
