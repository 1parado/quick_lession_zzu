package com.ly.easyenroll_user.pojo;

import java.io.Serializable;
import java.util.Objects;


/**
 * s_user
 */

public class SUser implements Serializable {
    private Integer id;

    private Long phone;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SUser sUser = (SUser) o;
        return Objects.equals(id, sUser.id) && Objects.equals(phone, sUser.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone);
    }

    @Override
    public String toString() {
        return "SUser{" +
                "id=" + id +
                ", phone=" + phone +
                '}';
    }
}