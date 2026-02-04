package com.cg.controller.user;

import com.cg.repository.MenuItemRepository;
import com.cg.repository.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserSearchController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    
    @GetMapping("/user/search")
    public String search(@RequestParam(name = "keyword", defaultValue = "") String keyword, Model model) {
        // 1. Fetch data
        var resList = restaurantRepository.findByRestaurantNameContainingIgnoreCase(keyword);
        var itemList = menuItemRepository.findByItemNameContainingIgnoreCase(keyword);

        // 2. Defensive: Ensure no nulls reach the template
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("restaurants", resList != null ? resList : new java.util.ArrayList<>());
        model.addAttribute("menuItems", itemList != null ? itemList : new java.util.ArrayList<>());

        return "user/search-results";
    }

}

