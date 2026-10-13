package com.shopsphere.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;

/**
 * Promotional discount coupon entity.
 */
public class Coupon implements Serializable {
    private static final long serialVersionUID = 1L;

    private int couponId;
    private String code;
    private String discountType; // "PERCENTAGE" or "FLAT"
    private double discountValue;
    private double minimumOrder;
    private double maximumDiscount;
    private Date expiryDate;
    private boolean status;

    public Coupon() {
        this.status = true;
        this.minimumOrder = 0.0;
        this.maximumDiscount = 9999.0;
    }

    public Coupon(int couponId, String code, String discountType, double discountValue,
                  double minimumOrder, double maximumDiscount, Date expiryDate, boolean status) {
        this.couponId = couponId;
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minimumOrder = minimumOrder;
        this.maximumDiscount = maximumDiscount;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(double minimumOrder) {
        this.minimumOrder = minimumOrder;
    }

    public double getMaximumDiscount() {
        return maximumDiscount;
    }

    public void setMaximumDiscount(double maximumDiscount) {
        this.maximumDiscount = maximumDiscount;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isValidForOrder(double orderTotal) {
        if (!status) return false;
        if (expiryDate != null && expiryDate.before(new java.util.Date())) return false;
        return orderTotal >= minimumOrder;
    }

    public double calculateDiscount(double orderTotal) {
        if (!isValidForOrder(orderTotal)) return 0.0;
        double calculated = 0.0;
        if ("PERCENTAGE".equalsIgnoreCase(discountType)) {
            calculated = orderTotal * (discountValue / 100.0);
            if (calculated > maximumDiscount) {
                calculated = maximumDiscount;
            }
        } else {
            calculated = Math.min(discountValue, orderTotal);
        }
        return BigDecimal.valueOf(calculated).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
