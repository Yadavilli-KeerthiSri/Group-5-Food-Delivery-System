package com.cg.controller.admin;

import com.cg.dto.MenuItemDto;
import com.cg.iservice.IMenuItemService;
import com.cg.iservice.IRestaurantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/menu-items")
public class AdminMenuItemController {

	private final IMenuItemService menuItemService;
	private final IRestaurantService restaurantService;

	@Autowired
	public AdminMenuItemController(IMenuItemService menuItemService, IRestaurantService restaurantService) {
		this.menuItemService = menuItemService;
		this.restaurantService = restaurantService;
	}

	/* ---------------- LIST ALL ITEMS ---------------- */
	@GetMapping
	public String list(Model model) {
		model.addAttribute("items", menuItemService.getAll());
		return "admin/menu-items";
	}

	/* ---------------- ADD FORM ---------------- */
	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("item", new MenuItemDto());
		model.addAttribute("restaurants", restaurantService.getAll());
		return "admin/menu-item-form";
	}

	/* ---------------- EDIT FORM ---------------- */
	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {
		MenuItemDto item = menuItemService.getById(id);
		model.addAttribute("item", item);
		model.addAttribute("restaurants", restaurantService.getAll());
		return "admin/menu-item-form";
	}

	/* ---------------- SAVE ITEM (CREATE) ---------------- */
	// CREATE: Handles the POST request to add a new menu item
	@PostMapping("/save")
	public String create(@ModelAttribute("item") MenuItemDto dto, @RequestParam("restaurantId") Long restaurantId,
			RedirectAttributes ra) {
		dto.setRestaurantId(restaurantId);
		menuItemService.add(dto);
		ra.addFlashAttribute("success", "Menu item added successfully!");
		return "redirect:/admin/menu-items";
	}

	// UPDATE: Handles the PUT request to modify an existing item
	@PutMapping("/update/{id}")
	public String update(@PathVariable Long id, @ModelAttribute("item") MenuItemDto dto,
			@RequestParam("restaurantId") Long restaurantId, RedirectAttributes ra) {
		// Explicitly set the ID from the path variable to ensure data integrity
		dto.setItemId(id);
		dto.setRestaurantId(restaurantId);

		menuItemService.update(dto);
		ra.addFlashAttribute("success", "Menu item updated successfully!");
		return "redirect:/admin/menu-items";
	}

	/* ---------------- DELETE ITEM ---------------- */
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes ra) {
		try {
			menuItemService.delete(id);
			// SUCCESS: Add a success message for the user
			ra.addFlashAttribute("success", "Menu item deleted successfully!");
		} catch (Exception e) {
			// ERROR: Let the GlobalExceptionHandler handle the redirect logic,
			// but we re-throw it here to trigger the ExceptionHandler
			throw e;
		}
		return "redirect:/admin/menu-items";
	}
}