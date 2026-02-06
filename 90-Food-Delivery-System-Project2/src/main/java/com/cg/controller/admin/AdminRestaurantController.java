package com.cg.controller.admin;

import com.cg.dto.RestaurantDto;
import com.cg.iservice.IRestaurantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/restaurants")
public class AdminRestaurantController {

    private final IRestaurantService restaurantService;

    @Autowired
    public AdminRestaurantController(IRestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /* ---------------------- LIST ---------------------- */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("restaurants", restaurantService.getAll());
        return "admin/restaurants";
    }

    /* ---------------------- ADD FORM ---------------------- */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("restaurant", new RestaurantDto());
        return "admin/restaurant-form";
    }

    /* ---------------------- EDIT FORM ---------------------- */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        RestaurantDto restaurant = restaurantService.getById(id);
        model.addAttribute("restaurant", restaurant);
        return "admin/restaurant-form";
    }

    /* ---------------------- SAVE (ADD or UPDATE) ---------------------- */
    @PostMapping("/save")
    public String save(@ModelAttribute("restaurant") RestaurantDto dto) {

        if (dto.getRestaurantId() == null) {
            restaurantService.add(dto);      // Create
        } else {
            restaurantService.update(dto);   // Update
        }

        return "redirect:/admin/restaurants";
    }

    /* ---------------------- DELETE ---------------------- */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return "redirect:/admin/restaurants";
    }
}