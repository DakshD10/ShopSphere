package model;

import java.sql.Timestamp;

public class AdminOrder {
    private int orderId;
    private String orderNumber;
    private String customerEmail;
    private double totalAmount;
    private String status;
    private Timestamp orderDate;

    public AdminOrder() {}

    public AdminOrder(int orderId, String orderNumber, String customerEmail, double totalAmount, String status, Timestamp orderDate) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.customerEmail = customerEmail;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
}
