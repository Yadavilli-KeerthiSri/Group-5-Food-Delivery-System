package com.cg.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.cg.enumeration.OrderStatus;
import com.cg.enumeration.PaymentMethod;
import com.cg.enumeration.TransactionStatus;

public class OrderDto {
	private Long orderId;

	// Set by server, but safe to validate if present
	private LocalDateTime orderDate;

	// If clients can post this, enforce allowed names (or rely on enum binding)
	@NotNull(message = "{order.status.notnull}")
	private com.cg.enumeration.OrderStatus orderStatus;

	@PositiveOrZero(message = "{order.totalAmount.positiveOrZero}")
	private double totalAmount;

	@NotNull(message = "{order.customerId.notnull}")
	@Positive(message = "{order.customerId.positive}")
	private Long customerId;

	@Positive(message = "{order.deliveryAgentId.positive}")
	private Long deliveryAgentId;

	@NotNull(message = "{order.itemIds.notnull}")
	@Size(min = 1, message = "{order.itemIds.size}")
	private List<@NotNull @Positive Long> itemIds;

	@Positive(message = "{order.paymentId.positive}")
	private Long paymentId;

	@NotNull(message = "{order.paymentMethod.notnull}")
	private PaymentMethod paymentMethod;

	@NotNull(message = "{order.txStatus.notnull}")
	private TransactionStatus transactionStatus;

	// Optional read-only cosmetics
	private String deliveryAgentName;
	private String deliveryAgentPhone;

	private Map<String, OrderItemDetail> itemDetails;

	public OrderDto() {
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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getDeliveryAgentId() {
		return deliveryAgentId;
	}

	public void setDeliveryAgentId(Long deliveryAgentId) {
		this.deliveryAgentId = deliveryAgentId;
	}

	public List<Long> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Long> itemIds) {
		this.itemIds = itemIds;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

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

	public Map<String, OrderItemDetail> getItemDetails() {
		return getItemDetails();
	}

	public void setItemDetails(Map<String, OrderItemDetail> itemDetails) {
		this.itemDetails = itemDetails;
	}

	// ===== ONLY ONE OrderItemDetail class definition =====

	public static class OrderItemDetail {
		private int quantity;
		private double price;
		private double subtotal;

		public OrderItemDetail() {
		}

		public OrderItemDetail(int quantity, double price) {
			this.quantity = quantity;
			this.price = price;
			this.subtotal = quantity * price;
		}

		public OrderItemDetail(int quantity, double price, double subtotal) {
			this.quantity = quantity;
			this.price = price;
			this.subtotal = subtotal;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public double getSubtotal() {
			return subtotal;
		}

		public void setSubtotal(double subtotal) {
			this.subtotal = subtotal;
		}
	}
}
