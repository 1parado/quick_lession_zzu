package com.ly.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import lombok.Data;

/**
 * payment
 */

public class Payment implements Serializable {
    /**
     * 缴费id
     */
    private Integer id;

    /**
     * 缴费项目名
     */
    private String name;

    /**
     * 缴费金额
     */
    private BigDecimal amount;

    /**
     * 缴费开始时间
     */
    private Date startTime;

    /**
     * 缴费结束时间
     */
    private Date endTime;

    /**
     * 已缴费人数
     */
    private Integer paidCount;

    /**
     * 应缴费人数
     */
    private Integer payableCount;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) && Objects.equals(name, payment.name) && Objects.equals(amount, payment.amount) && Objects.equals(startTime, payment.startTime) && Objects.equals(endTime, payment.endTime) && Objects.equals(paidCount, payment.paidCount) && Objects.equals(payableCount, payment.payableCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, amount, startTime, endTime, paidCount, payableCount);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", paidCount=" + paidCount +
                ", payableCount=" + payableCount +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPaidCount() {
        return paidCount;
    }

    public void setPaidCount(Integer paidCount) {
        this.paidCount = paidCount;
    }

    public Integer getPayableCount() {
        return payableCount;
    }

    public void setPayableCount(Integer payableCount) {
        this.payableCount = payableCount;
    }
}