package com.ly.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.StringUtils;
import com.ly.exportData.StudentListImportData;
import com.ly.exportData.TeacherListImportData;
import com.ly.mapper.StudentsMapper;
import com.ly.mapper.TeachersMapper;
import com.ly.mapper.UsersMapper;
import com.ly.pojo.Students;
import com.ly.pojo.Teachers;
import com.ly.pojo.Users;
import net.sf.jsqlparser.util.validation.ValidationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Transactional
public class TeacherDataListener extends AnalysisEventListener {

    private List<Teachers> list = new ArrayList<>();

    private TeachersMapper teachersMapper;

    private UsersMapper usersMapper;

    public TeacherDataListener(TeachersMapper teachersMapper, UsersMapper usersMapper) {
        this.teachersMapper = teachersMapper;
        this.usersMapper = usersMapper;
    }

    @Override
    public void invoke(Object data, AnalysisContext context) {
        TeacherListImportData teacherListImportData = (TeacherListImportData) data;
        try {
            // 跳过学号已存在的记录
            if (teachersMapper.selectByTno(teacherListImportData.getTno()) != null) {
                System.out.println("跳过工号重复的记录: " + teacherListImportData.getTno());
                return;
            }

            // 数据校验，判断tno唯一
            validateData(teacherListImportData);

            // 保存user表
            Users user = createUser(teacherListImportData);
            int userId = saveUser(user, teacherListImportData.getTno());

            //保存学生信息

            //将导入的数据转换为实体类
            Teachers teacher = new Teachers();
            teacher.setTno(teacherListImportData.getTno());
            teacher.setPhone(teacherListImportData.getPhone());
            teacher.setAge(teacherListImportData.getAge());
            teacher.setCollege(teacherListImportData.getCollege());
            teacher.setName(teacherListImportData.getName());
            teacher.setGender(teacherListImportData.getGender());
            teacher.setTitle(teacherListImportData.getTitle());
            teacher.setUserId(userId);

            System.out.println("处理了一行数据" + teacher);
            teachersMapper.insert(teacher);
            list.add(teacher);
        }  catch (ValidationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("Excel解析完成");
    }

    private void validateData(TeacherListImportData data) throws ValidationException {
        if (StringUtils.isBlank(data.getName())) {
            throw new ValidationException("姓名不能为空");
        }

        if (!Arrays.asList("男", "女", "其他").contains(data.getGender())) {
            throw new ValidationException("性别必须是'男'、'女'或'其他'");
        }

        if (data.getAge() != null && data.getAge() <= 0) {
            throw new ValidationException("年龄必须是正整数");
        }

        if (data.getTno() == null) {
            throw new ValidationException("工号不得为空");
        }

        if (data.getTno() != null && data.getTno().toString().length() != 7) {
            throw new ValidationException("工号必须是7位数字");
        }

        if (existsBySno(data.getTno())) {
            throw new ValidationException("工号已存在");
        }

        if (StringUtils.isBlank(data.getCollege())) {
            throw new ValidationException("学院不能为空");
        }


        // 其他校验...
    }

    private Users createUser(TeacherListImportData data) {
        Users user = new Users();
        user.setAccount(data.getTno());
        user.setPassword(data.getTno().toString());
        user.setRole("TEACHER");
        user.setIsActive(1);
        return user;
    }

    private boolean existsBySno(Long tno) {
        Teachers s = teachersMapper.selectByTno(tno);
        if (s != null && !s.getTno().equals(tno)) {
            //说明重复了，重复就跳过不管这条数据
            return true;
        }
        return false;
    }

    private int saveUser(Users user, Long tno) {
        //保存user
        usersMapper.insert(user);
        //拿到userId
        Integer userId = usersMapper.selectIdByAccount(tno);
        return userId;
    }
}
