package com.cg.dto;

import java.util.List;

public class DeliveryAgentDto {
    private Long agentId;
    private String agentName;
    private String contact;
    private String vehicleDetails;
    private boolean availability;

    // order IDs assigned to this agent (optional, can be large if unbounded)
    private List<Long> orderIds;

    public DeliveryAgentDto() {}

    public DeliveryAgentDto(Long agentId, String agentName, String contact, String vehicleDetails, boolean availability, List<Long> orderIds) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.contact = contact;
        this.vehicleDetails = vehicleDetails;
        this.availability = availability;
        this.orderIds = orderIds;
    }

    public Long getAgentId() { return agentId; }
    public void setAgentId(Long agentId) { this.agentId = agentId; }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getVehicleDetails() { return vehicleDetails; }
    public void setVehicleDetails(String vehicleDetails) { this.vehicleDetails = vehicleDetails; }

    public boolean isAvailability() { return availability; }
    public void setAvailability(boolean availability) { this.availability = availability; }

    public List<Long> getOrderIds() { return orderIds; }
    public void setOrderIds(List<Long> orderIds) { this.orderIds = orderIds; }
}