package com.ly.service;

import com.ly.pojo.Users;

public interface UserService{
    Users getUserByAccount(Long account);
    void register(Users user);
    void checkUserLogin(Users user, String password);

    int updatePhone(Long account, String role,String phone);

    int updatePassword(Long account, String role, String password);
}
