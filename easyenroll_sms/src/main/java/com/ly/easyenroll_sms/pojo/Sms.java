package com.ly.easyenroll_sms.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * sms
 */
public class Sms implements Serializable {
    /**
     * 唯一标识
     */
    private Integer id;

    /**
     * 线程id
     */
    private Long threadId;

    /**
     * 发送者手机号，目前统一为学校号码
     */
    private Long sender;

    /**
     * 接收者手机号（单个接收者时）
     */
    private Long receiver;

    /**
     * 主题，可以没有
     */
    private String subject;

    /**
     * 类型（1：接收，2：发送，3：草稿）
     */
    private Integer type;

    /**
     * 状态（0-发送中，1-发送成功，2-发送失败，3-已送达（对于接收的短信，此字段可忽略或设为已送达）
     */
    private Integer status;

    /**
     * 发送时间（从学生系统，从sms系统）
     */
    private Date datesend;

    /**
     * 接收时间（到sms系统，到用户系统）
     */
    private Date datereceive;

    /**
     * 是否已读 (0-未读，1-已读)
     */
    private Integer read;

    /**
     * 短信内容
     */
    private String content;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDatesend() {
        return datesend;
    }

    public void setDatesend(Date datesend) {
        this.datesend = datesend;
    }

    public Date getDatereceive() {
        return datereceive;
    }

    public void setDatereceive(Date datereceive) {
        this.datereceive = datereceive;
    }

    public Integer getRead() {
        return read;
    }

    public void setRead(Integer read) {
        this.read = read;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sms sms = (Sms) o;
        return Objects.equals(id, sms.id) && Objects.equals(threadId, sms.threadId) && Objects.equals(sender, sms.sender) && Objects.equals(receiver, sms.receiver) && Objects.equals(subject, sms.subject) && Objects.equals(type, sms.type) && Objects.equals(status, sms.status) && Objects.equals(datesend, sms.datesend) && Objects.equals(datereceive, sms.datereceive) && Objects.equals(read, sms.read) && Objects.equals(content, sms.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, threadId, sender, receiver, subject, type, status, datesend, datereceive, read, content);
    }

    @Override
    public String toString() {
        return "Sms{" +
                "id=" + id +
                ", threadId=" + threadId +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", subject='" + subject + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", datesend=" + datesend +
                ", datereceive=" + datereceive +
                ", read=" + read +
                ", content='" + content + '\'' +
                '}';
    }
}