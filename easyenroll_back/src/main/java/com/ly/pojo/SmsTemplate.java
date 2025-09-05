package com.ly.pojo;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

/**
 * sms_template
 */
@Data
public class SmsTemplate implements Serializable {
    private Integer id;

    /**
     * 模板类型：“教师模板teacherTemplate”，“学生模板studentTemplate”
     */
    private String type;

    /**
     * 选中状态，默认为0
     */
    private Integer status;

    /**
     * 最多255字节，约85个中文/255个英文
     */
    private String component;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsTemplate that = (SmsTemplate) o;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(status, that.status) && Objects.equals(component, that.component);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, status, component);
    }

    @Override
    public String toString() {
        return "SmsTemplate{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", status=" + status +
                ", component='" + component + '\'' +
                '}';
    }
}