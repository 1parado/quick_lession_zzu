package com.ly.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.mapper.CoursesMapper;
import com.ly.mapper.TeachersMapper;
import com.ly.mapper.UsersMapper;
import com.ly.pojo.Students;
import com.ly.pojo.Teachers;
import com.ly.pojo.Users;
import com.ly.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeacherServiceImpl implements com.ly.service.TeacherService {

    @Autowired
    private TeachersMapper teachersMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private CoursesMapper coursesMapper;

    @Override
    public Teachers getTeacherByTno(Long account) {
        return teachersMapper.selectByTno(account);
    }

    @Override
    public PageInfo<Teachers> getTeacher(int page, int size) {
        PageHelper.startPage(page, size);
        List<Teachers> teachers = teachersMapper.selectTeacher();
        PageInfo<Teachers> pageInfo = PageInfo.of(teachers);
        return pageInfo;
    }

    @Override
    public PageInfo<Teachers> searchTeacher(int page, int size, String searchText) {
        PageHelper.startPage(page, size);
        List<Teachers> teachers = teachersMapper.searchTeacher(searchText);
        PageInfo<Teachers> pageInfo = PageInfo.of(teachers);
        return pageInfo;
    }

    @Override
    public int updateTeacher(Teachers teacher) {
        //更新前先判断数据是否合理（在前端已经判断过是否合法）
        //工号唯一
        Teachers s = teachersMapper.selectByTno(teacher.getTno());
        if (s != null && !s.getTno().equals(teacher.getTno())){
            //说明重复了
            return 2;
        }

        int i1 = teachersMapper.updateByPrimaryKey(teacher);
        int i2 = usersMapper.updateByAccountAndPasswordById(teacher.getUserId(), teacher.getTno());
        if (i1 == i2 && i1 == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteTeacherById(Integer id) {
        Teachers teacher = teachersMapper.selectByPrimaryKey(id);
        int i1 = usersMapper.deleteByAccount(teacher.getTno());
        int i2 = teachersMapper.deleteByPrimaryKey(id);
        if (i1 == i2 && i1 == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public int insertTeacher(Teachers teachers) {
        //先判断sno是否唯一
        Teachers s = teachersMapper.selectByTno(teachers.getTno());
        if (s != null){
            //说明重复了
            return 2;
        }

        //先拿到sno写入users表
        Users user = new Users();
        user.setPassword(String.valueOf(teachers.getTno()));
        user.setAccount(teachers.getTno());
        user.setRole("TEACHER");
        user.setIsActive(1);
        int i1 = usersMapper.insert(user);
        //从users表拿到id
        Integer id = usersMapper.selectIdByAccount(teachers.getTno());
        //新增
        teachers.setUserId(id);
        int i2 = teachersMapper.insert(teachers);
        if (i1 == i2 && i1 == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteOnCourseById(Integer id) {
        //找到course表中 teacher_id 值 为 当前 id 的数据
        //修改为空
        int i = coursesMapper.updateByTeacherId(id);
        return i;
    }

    @Override
    public List<Teachers> getTeacherList() {
       return teachersMapper.selectTeacher();
    }
}
