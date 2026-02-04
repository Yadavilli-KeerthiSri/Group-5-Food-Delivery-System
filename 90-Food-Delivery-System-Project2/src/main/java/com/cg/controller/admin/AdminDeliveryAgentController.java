package com.cg.controller.admin;

import com.cg.entity.DeliveryAgent;
import com.cg.service.DeliveryAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/agents")
public class AdminDeliveryAgentController {

    @Autowired
    private DeliveryAgentService deliveryAgentService;

    /* LIST */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("agents", deliveryAgentService.getAll());
        return "admin/agents";
    }

    /* ADD FORM */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("agent", new DeliveryAgent());
        return "admin/agent-form";
    }

    /* EDIT FORM */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("agent", deliveryAgentService.getById(id));
        return "admin/agent-form";
    }

    /* SAVE */
    @PostMapping("/save")
    public String save(@ModelAttribute DeliveryAgent agent) {
        deliveryAgentService.add(agent);
        return "redirect:/admin/agents";
    }

    /* DELETE */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        deliveryAgentService.delete(id);
        return "redirect:/admin/agents";
    }
}
