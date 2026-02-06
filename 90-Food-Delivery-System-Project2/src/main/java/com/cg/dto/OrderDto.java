package com.cg.dto;
 
import java.time.LocalDateTime;

import java.util.List;
 
import com.cg.enumeration.OrderStatus;
import com.cg.enumeration.PaymentMethod;
import com.cg.enumeration.TransactionStatus;
 
public class OrderDto {

    private Long orderId;

    private LocalDateTime orderDate;

    private OrderStatus orderStatus;

    private double totalAmount;
 
    private Long customerId;

    private Long deliveryAgentId;
 
    // items in this order (by IDs)

    private List<Long> itemIds;
 
    // payment ID if exists (1-1 relation)

    private Long paymentId;

    private PaymentMethod paymentMethod;

    private TransactionStatus transactionStatus;

    private String deliveryAgentName;   // add this

    private String deliveryAgentPhone;  // optional
 
    public OrderDto() {}
 
    public String getDeliveryAgentName() {
		return deliveryAgentName;
	}
 
	public void setDeliveryAgentName(String deliveryAgentName) {
		this.deliveryAgentName = deliveryAgentName;
	}
 
	public String getDeliveryAgentPhone() {
		return deliveryAgentPhone;
	}
 
	public void setDeliveryAgentPhone(String deliveryAgentPhone) {
		this.deliveryAgentPhone = deliveryAgentPhone;
	}
 
	public OrderDto(Long orderId, LocalDateTime orderDate, OrderStatus orderStatus, double totalAmount, Long customerId,
			Long deliveryAgentId, List<Long> itemIds, Long paymentId, PaymentMethod paymentMethod,
			TransactionStatus transactionStatus, String deliveryAgentName, String deliveryAgentPhone) {
		super();
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.totalAmount = totalAmount;
		this.customerId = customerId;
		this.deliveryAgentId = deliveryAgentId;
		this.itemIds = itemIds;
		this.paymentId = paymentId;
		this.paymentMethod = paymentMethod;
		this.transactionStatus = transactionStatus;
		this.deliveryAgentName = deliveryAgentName;
		this.deliveryAgentPhone = deliveryAgentPhone;
	}
 
	public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
 
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
 
    public OrderStatus getOrderStatus() { return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }
 
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
 
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
 
    public Long getDeliveryAgentId() { return deliveryAgentId; }
    public void setDeliveryAgentId(Long deliveryAgentId) { this.deliveryAgentId = deliveryAgentId; }

    public List<Long> getItemIds() { return itemIds; }
    public void setItemIds(List<Long> itemIds) { this.itemIds = itemIds; }
 
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
 
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
 
	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
}
 