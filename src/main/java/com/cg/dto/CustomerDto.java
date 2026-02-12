package com.cg.dto;

import jakarta.validation.constraints.*;

public class CustomerDto {
	private Long customerId;

	@NotBlank(message = "{customer.name.notblank}")
	@Size(min = 2, max = 60, message = "{customer.name.size}")
	@Pattern(regexp = "^[\\p{L} .'-]+$", message = "{customer.name.pattern}")
	private String customerName;

	@NotBlank(message = "{customer.contact.notblank}")
	@Pattern(regexp = "^[0-9]{10}$", message = "{customer.contact.pattern}")
	private String contact;

	@NotBlank(message = "{customer.email.notblank}")
	@Email(message = "{customer.email.email}")
	@Size(max = 120, message = "{customer.email.size}")
	private String email;

	@NotBlank(message = "{customer.address.notblank}")
	@Size(min = 5, max = 200, message = "{customer.address.size}")
	private String address;

	// For registration forms only; for profile update you may not expose this.
	@NotBlank(message = "{customer.password.notblank}")
	@Size(min = 8, max = 72, message = "{customer.password.size}") // BCrypt limit
	private String password;

	// Server-controlled—still validate shape if it arrives
	@Pattern(regexp = "^ROLE_(USER|ADMIN)$", message = "{customer.role.pattern}")
	private String role;

	public CustomerDto() {
	}

	public CustomerDto(Long customerId, String customerName, String contact, String email, String address,
			String password, String role) {
		this.customerId = customerId;
		this.customerName = customerName;
		this.contact = contact;
		this.email = email;
		this.address = address;
		this.password = password;
		this.role = role;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}