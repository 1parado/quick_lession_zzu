package com.ly.service.impl;

import com.ly.mapper.StudentsMapper;
import com.ly.mapper.TeachersMapper;
import com.ly.mapper.UsersMapper;
import com.ly.pojo.Students;
import com.ly.pojo.Teachers;
import com.ly.pojo.Users;
import com.ly.service.TeacherService;
import com.ly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// UserServiceImpl.java
@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private TeachersMapper teachersMapper;

    @Override
    public Users getUserByAccount(Long account) {
        return usersMapper.selectByAccount(account);
    }

    @Override
    public void register(Users user) {
        if (getUserByAccount(user.getAccount()) != null) {
            throw new RuntimeException("账号已存在");
        }

        //默认账号密码一样，只能由管理员注册
        if (user.getAccount() != null){
            if (user.getPassword() == null) {
                user.setPassword(String.valueOf(user.getAccount()));
            }
            usersMapper.insert(user);
        }

    }

    @Override
    public void checkUserLogin(Users user, String password) {
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (user.getIsActive() != 1) {
            throw new RuntimeException("用户已被禁用");
        }

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
    }

    @Override
    public int updatePhone(Long account, String role, String phone) {
        if ("STUDENT".equals(role)) {
            Students student = new Students();
            student.setSno(account);
            student.setPhone(Long.parseLong(phone));
            int i = studentsMapper.updateBySno(student);
            return i;
        } else if ("TEACHER".equals(role)) {
            Teachers teacher = new Teachers();
            teacher.setTno(account);
            teacher.setPhone(Long.parseLong(phone));
            int i = teachersMapper.updateByTno(teacher);
            return i;
        }
        return 0;
    }

    @Override
    public int updatePassword(Long account, String role, String password) {
        if ("STUDENT".equals(role) || "TEACHER".equals(role)) {
            Users user = new Users();
            user.setAccount(account);
            user.setPassword(password);
            return usersMapper.updatePasswordByAccount(user);
        }
        return 0;
    }

}
