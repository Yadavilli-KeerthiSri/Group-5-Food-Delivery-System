package com.cg.controller.admin;

import com.cg.entity.MenuItem;
import com.cg.service.MenuItemService;
import com.cg.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/menu-items")
public class AdminMenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private RestaurantService restaurantService;

    /* LIST */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", menuItemService.getAll());
        return "admin/menu-items";
    }

    /* ADD FORM */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new MenuItem());
        model.addAttribute("restaurants", restaurantService.getAll());
        return "admin/menu-item-form";
    }

    /* EDIT FORM */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("item", menuItemService.getById(id));
        model.addAttribute("restaurants", restaurantService.getAll());
        return "admin/menu-item-form";
    }

    /* SAVE */
    @PostMapping("/save")
    public String save(@ModelAttribute MenuItem item,
                       @RequestParam Long restaurantId) {

        item.setRestaurant(restaurantService.getById(restaurantId));
        menuItemService.add(item);

        return "redirect:/admin/menu-items";
    }

    /* DELETE */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        menuItemService.delete(id);
        return "redirect:/admin/menu-items";
    }
}

