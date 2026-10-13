package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Master Order entity containing payment and address references, plus line items.
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderId;
    private int userId;
    private Integer addressId;
    private Address address;
    private double totalAmount;
    private double discount;
    private String paymentMethod; // "COD", "CARD", "UPI", "NET_BANKING"
    private String paymentStatus; // "PENDING", "PAID", "FAILED"
    private String orderStatus;   // "PLACED", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"
    private Timestamp createdAt;
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
        this.paymentMethod = "COD";
        this.paymentStatus = "PENDING";
        this.orderStatus = "PLACED";
    }

    public Order(int orderId, int userId, Integer addressId, double totalAmount, double discount,
                 String paymentMethod, String paymentStatus, String orderStatus) {
        this.orderId = orderId;
        this.userId = userId;
        this.addressId = addressId;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }
}
