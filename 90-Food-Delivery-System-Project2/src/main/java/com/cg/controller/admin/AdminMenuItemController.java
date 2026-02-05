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
    public String save(@ModelAttribute("item") MenuItem item,
                       @RequestParam("restaurantId") Long restaurantId) {

        // 1. Fetch the restaurant
        var restaurant = restaurantService.getById(restaurantId);

        if (item.getItemId() != null) {
            // 2. If editing, fetch the MANAGED entity from DB
            MenuItem existingItem = menuItemService.getById(item.getItemId());
            
            // 3. Update the managed entity with form data
            existingItem.setItemName(item.getItemName());
            existingItem.setCategory(item.getCategory());
            existingItem.setPrice(item.getPrice());
            existingItem.setRestaurant(restaurant);
            
            // 4. Save the managed entity
            menuItemService.add(existingItem);
        } else {
            // 5. If it's a brand new item
            item.setRestaurant(restaurant);
            menuItemService.add(item);
        }

        return "redirect:/admin/menu-items";
    }

    /* DELETE */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        menuItemService.delete(id);
        return "redirect:/admin/menu-items";
    }
}

