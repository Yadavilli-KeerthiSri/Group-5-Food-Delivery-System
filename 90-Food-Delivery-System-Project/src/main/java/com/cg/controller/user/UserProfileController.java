package com.cg.controller.user;

import com.cg.dto.CustomerDto;
import com.cg.iservice.ICustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/profile")
public class UserProfileController {

	private final ICustomerService customerService;

	@Autowired
	public UserProfileController(ICustomerService customerService) {
		this.customerService = customerService;
	}

	/** VIEW PROFILE */
	@GetMapping
	public String profile(Authentication auth, Model model) {
		CustomerDto user = customerService.getByEmail(auth.getName());
		model.addAttribute("user", user);
		return "user/profile";
	}

	/** EDIT PROFILE FORM */
	@GetMapping("/edit")
	public String edit(Authentication auth, Model model) {
		CustomerDto user = customerService.getByEmail(auth.getName());
		model.addAttribute("user", user);
		return "user/profile-edit";
	}

	// UPDATE PROFILE
	@PutMapping("/update")
	public String update(@ModelAttribute("user") CustomerDto userDto, Authentication auth) {
		// 1. Fetch existing data to protect immutable fields
		CustomerDto current = customerService.getByEmail(auth.getName());

		// 2. Enforce immutability & identity
		userDto.setCustomerId(current.getCustomerId());
		userDto.setEmail(current.getEmail());
		userDto.setRole(current.getRole());

		// 3. Persist (Ensure your service logic handles 'updates' vs 'new
		// registrations')
		customerService.register(userDto);

		return "redirect:/user/profile";
	}
}