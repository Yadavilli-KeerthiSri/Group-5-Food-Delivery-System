package com.cg.controller.admin;

import com.cg.entity.Restaurant;
import com.cg.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/restaurants")
public class AdminRestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    /* LIST */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("restaurants", restaurantService.getAll());
        return "admin/restaurants";
    }

    /* ADD FORM */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        return "admin/restaurant-form";
    }

    /* EDIT FORM */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("restaurant", restaurantService.getById(id));
        return "admin/restaurant-form";
    }

    /* SAVE (ADD + EDIT) */
    @PostMapping("/save")
    public String save(@ModelAttribute Restaurant restaurant) {
        restaurantService.add(restaurant);
        return "redirect:/admin/restaurants";
    }

    /* DELETE */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return "redirect:/admin/restaurants";
    }
}

