package com.cg.dto;

import com.cg.enumeration.PaymentMethod;
import com.cg.enumeration.TransactionStatus;

public class PaymentDto {
    private Long paymentId;
    private PaymentMethod paymentMethod;
    private double amount;
    private TransactionStatus transactionStatus;
    private Long orderId;

    public PaymentDto() {}

    public PaymentDto(Long paymentId, PaymentMethod paymentMethod, double amount, TransactionStatus transactionStatus, Long orderId) {
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.transactionStatus = transactionStatus;
        this.orderId = orderId;
    }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public TransactionStatus getTransactionStatus() { return transactionStatus; }
    public void setTransactionStatus(TransactionStatus transactionStatus) { this.transactionStatus = transactionStatus; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}