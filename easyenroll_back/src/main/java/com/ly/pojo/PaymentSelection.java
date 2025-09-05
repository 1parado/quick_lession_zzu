package com.ly.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import lombok.Data;

/**
 * payment_selection
 */

public class PaymentSelection implements Serializable {
    private Integer id;

    /**
     * 缴费id
     */
    private Integer payId;

    /**
     * 缴费人id（此处为学生学号）
     */
    private Long payerId;

    /**
     * 缴费方式（支付宝支付/微信支付）
     */
    private String payMethod;

    /**
     * 缴费时间
     */
    private Date payTime;

    /**
     * 缴费状态
     */
    private int payStatus;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "PaymentSelection{" +
                "id=" + id +
                ", payId=" + payId +
                ", payerId=" + payerId +
                ", payMethod='" + payMethod + '\'' +
                ", payTime=" + payTime +
                ", payStatus=" + payStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentSelection that = (PaymentSelection) o;
        return payStatus == that.payStatus && Objects.equals(id, that.id) && Objects.equals(payId, that.payId) && Objects.equals(payerId, that.payerId) && Objects.equals(payMethod, that.payMethod) && Objects.equals(payTime, that.payTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, payId, payerId, payMethod, payTime, payStatus);
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }
}