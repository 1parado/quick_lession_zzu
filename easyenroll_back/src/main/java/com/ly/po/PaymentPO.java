package com.ly.po;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.function.LongFunction;

public class PaymentPO {
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
     * 缴费人学号
     */
    private Long payerId;

    /**
     * 支付方式
     */
    private String payMethod;

    /**
     * 缴费时间
     */
    private Date payTime;

    /**
     * 缴费状态
     * @return
     */
    private int payStatus;

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
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

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentPO paymentPO = (PaymentPO) o;
        return Objects.equals(id, paymentPO.id) && Objects.equals(name, paymentPO.name) && Objects.equals(amount, paymentPO.amount) && Objects.equals(startTime, paymentPO.startTime) && Objects.equals(endTime, paymentPO.endTime) && Objects.equals(payerId, paymentPO.payerId) && Objects.equals(payMethod, paymentPO.payMethod) && Objects.equals(payTime, paymentPO.payTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, amount, startTime, endTime, payerId, payMethod, payTime);
    }

    @Override
    public String toString() {
        return "PaymentPO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", payerId=" + payerId +
                ", payMethod='" + payMethod + '\'' +
                ", payTime=" + payTime +
                '}';
    }
}
