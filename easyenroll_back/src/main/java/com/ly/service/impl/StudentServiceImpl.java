package com.ly.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.exception.BusinessException;
import com.ly.mapper.SelectionsMapper;
import com.ly.mapper.StudentsMapper;
import com.ly.mapper.UsersMapper;
import com.ly.po.StudentsGradePO;
import com.ly.pojo.Students;
import com.ly.pojo.Users;
import com.ly.result.R;
import com.ly.service.StudentService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private SelectionsMapper selectionsMapper;

    @Override
    public Students selectStudentById(Integer id) {
        return studentsMapper.selectByPrimaryKey(id);
    }

    @Override
    public Students getStudentBySno(Long account) {
        return studentsMapper.selectBySno(account);
    }

    @Override
    public PageInfo<Students> getStudent(int page, int size) {
        PageHelper.startPage(page, size);
        List<Students> students = studentsMapper.selectStudent();
        PageInfo<Students> pageInfo = PageInfo.of(students);
        return pageInfo;
    }

    @Override
    public PageInfo<Students> getSearchStudent(int page, int size, String searchText) {
        PageHelper.startPage(page, size);
        List<Students> students = studentsMapper.selectStudentBySearch(searchText);
        PageInfo<Students> pageInfo = PageInfo.of(students);
        return pageInfo;
    }

    @Override
    public PageInfo<Students> getStudentByCourseId(int page, int size, Integer courseId) {
        //根据courseId得到学生ids
        List<Integer> studentIds = selectionsMapper.selectStudentIdsByCourseId(courseId);

        //根据ids得到学生列表
        PageHelper.startPage(page, size);
        List<Students> students = studentsMapper.selectStudentByIds(studentIds);
        PageInfo<Students> pageInfo = PageInfo.of(students);
        return pageInfo;
    }

    @Override
    public PageInfo<StudentsGradePO> getStudentGradeByCourseId(int page, int size, Integer courseId) {
        //根据courseId得到学生ids
        List<Integer> studentIds = selectionsMapper.selectStudentIdsByCourseId(courseId);
        //根据ids得到学生列表
        PageHelper.startPage(page, size);
        List<StudentsGradePO> students = studentsMapper.selectStudentGradeByIds(studentIds);
        PageInfo<StudentsGradePO> pageInfo = PageInfo.of(students);
        return pageInfo;
    }

    @Override
    public int updateStudent(Students student) {
        //更新前先判断数据是否合理（在前端已经判断过是否合法）
        //学号唯一
        Students s = studentsMapper.selectBySno(student.getSno());
        if (s != null && !s.getSno().equals(student.getSno())){
            //说明重复了
            return 2;
        }

        int i1 = studentsMapper.updateByPrimaryKey(student);
        int i2 = usersMapper.updateByAccountAndPasswordById(student.getUserId(), student.getSno());
        if (i1 == i2 && i1 == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteStudentById(Integer id) {
        Students student = studentsMapper.selectByPrimaryKey(id);
        int i1 = usersMapper.deleteByAccount(student.getSno());
        int i2 = studentsMapper.deleteByPrimaryKey(id);
        if (i1 == i2 && i1 == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public int insertStudent(Students students) {
        //先判断sno是否唯一
        Students s = studentsMapper.selectBySno(students.getSno());
        if (s != null){
            //说明重复了
            return 2;
        }

        //先拿到sno写入users表
        Users user = new Users();
        user.setPassword(String.valueOf(students.getSno()));
        user.setAccount(students.getSno());
        user.setRole("STUDENT");
        user.setIsActive(1);
        int i1 = usersMapper.insert(user);
        //从users表拿到id
        Integer id = usersMapper.selectIdByAccount(students.getSno());
        //新增
        students.setUserId(id);
        int i2 = studentsMapper.insert(students);
        if (i1 == i2 && i1 == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getStudentIdByAccount(Long account) {
        Integer studentId = studentsMapper.selectIdBySno(account);
        if (studentId == null) {
            throw new BusinessException(500,"找不到学号为 " + account + " 的学生");
        }
        return studentId;
    }

    @Override
    public List<Students> selectStudentByKeyword(String keyword) {
        return studentsMapper.selectListByKeyword(keyword);
    }
}
