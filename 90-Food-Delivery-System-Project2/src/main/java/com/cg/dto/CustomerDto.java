package com.cg.dto;

public class CustomerDto {
    private Long customerId;
    private String customerName;
    private String contact;
    private String email;
    private String address;
    private String password; 
    private String role;

    public CustomerDto() {}

    public CustomerDto(Long customerId, String customerName, String contact, 
                       String email, String address, String password, String role) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.password = password;
        this.role = role;
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}