package com.cg.controller.admin;

import com.cg.dto.DeliveryAgentDto;
import com.cg.iservice.IDeliveryAgentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/agents")
public class AdminDeliveryAgentController {

	private final IDeliveryAgentService deliveryAgentService;

	@Autowired
	public AdminDeliveryAgentController(IDeliveryAgentService deliveryAgentService) {
		this.deliveryAgentService = deliveryAgentService;
	}

	/* -------------------- LIST -------------------- */
	@GetMapping
	public String list(Model model) {
		model.addAttribute("agents", deliveryAgentService.getAll());
		return "admin/agents";
	}

	/* -------------------- ADD FORM -------------------- */
	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("agent", new DeliveryAgentDto());
		return "admin/agent-form";
	}

	/* -------------------- EDIT FORM -------------------- */
	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {
		DeliveryAgentDto dto = deliveryAgentService.getById(id);
		model.addAttribute("agent", dto);
		return "admin/agent-form";
	}

	/* --------------------(CREATE OR UPDATE) -------------------- */
	/**
	 * Handles creation of new agents.
	 */
	@PostMapping
	public String createAgent(@ModelAttribute("agent") DeliveryAgentDto dto) {
		deliveryAgentService.add(dto);
		return "redirect:/admin/agents";
	}

	/**
	 * Handles updates for existing agents. Note: Requires
	 */
	@PutMapping("/{id}")
	public String updateAgent(@PathVariable Long id, @ModelAttribute("agent") DeliveryAgentDto dto) {
		System.out.println("Updating Agent ID: " + id); // Debug: Check if this hits your console
		dto.setAgentId(id);
		deliveryAgentService.update(dto);
		return "redirect:/admin/agents";
	}

	/* -------------------- DELETE -------------------- */
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		deliveryAgentService.delete(id);
		return "redirect:/admin/agents";
	}
}