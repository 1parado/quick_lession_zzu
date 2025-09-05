package com.ly.pojo;

import java.io.Serializable;
import java.util.Collection;

import lombok.Data;
import org.springframework.data.annotation.Id;


/**
 * users
 */

//UserDetails是Spring Security的核心接口，代表用户信息
//实体类实现这个接口，Spring Security才能识别你的用户模型
public class Users implements Serializable{

    private Integer id;

    /**
     * 登录账号
     */
    private Long account;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 角色
     */
    private Object role;

    /**
     * 默认为1，表示活跃，否则为0
     */
    private Integer isActive = 1;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getRole() {
        return role;
    }

    public void setRole(Object role) {
        this.role = role;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}