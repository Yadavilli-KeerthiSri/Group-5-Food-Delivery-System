package com.cg.controller.admin;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//import com.cg.repository.DeliveryAgentRepository;
//import com.cg.repository.OrderRepository;
//import com.cg.repository.RestaurantRepository;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

//    @Autowired
//    private RestaurantRepository restaurantRepository;
//
//    @Autowired
//    private DeliveryAgentRepository deliveryAgentRepository;
//
//    @Autowired
//    private OrderRepository orderRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
//        model.addAttribute("restaurants", restaurantRepository.findAll());
//        model.addAttribute("agents", deliveryAgentRepository.findAll());
//        model.addAttribute("orders", orderRepository.findAll());
        return "admin/admin-dashboard";
    }
}

