package com.cg.dto;

import java.util.List;

import jakarta.validation.constraints.*;

public class DeliveryAgentDto {
	private Long agentId;

	@NotBlank(message = "{agent.name.notblank}")
	@Size(min = 2, max = 50, message = "{agent.name.size}")
	@Pattern(regexp = "^[A-Za-z ]+$", message = "{agent.name.pattern}")
	private String agentName;

	@NotBlank(message = "{agent.contact.notblank}")
	@Pattern(regexp = "^[0-9]{10}$", message = "{agent.contact.pattern}")
	private String contact;

	@NotBlank(message = "{agent.vehicle.notblank}")
	@Size(min = 5, max = 20, message = "{agent.vehicle.size}")
	@Pattern(regexp = "^[A-Z0-9\\s-]+$", message = "{agent.vehicle.pattern}")
	private String vehicleDetails;

	// `boolean` primitives can’t be @NotNull; leave as-is.
	private boolean availability;

	// Optional: validate each id >= 1
	private List<@NotNull @Positive Long> orderIds;

	// order IDs assigned to this agent (optional, can be large if unbounded)
	// private List<Long> orderIds;

	public DeliveryAgentDto() {
	}

	public DeliveryAgentDto(Long agentId, String agentName, String contact, String vehicleDetails, boolean availability,
			List<Long> orderIds) {
		this.agentId = agentId;
		this.agentName = agentName;
		this.contact = contact;
		this.vehicleDetails = vehicleDetails;
		this.availability = availability;
		this.orderIds = orderIds;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getVehicleDetails() {
		return vehicleDetails;
	}

	public void setVehicleDetails(String vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}

	public boolean isAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	public List<Long> getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(List<Long> orderIds) {
		this.orderIds = orderIds;
	}
}