package com.ly.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.StringUtils;
import com.ly.exportData.StudentListImportData;
import com.ly.mapper.StudentsMapper;
import com.ly.mapper.UsersMapper;
import com.ly.pojo.Students;
import com.ly.pojo.Users;
import net.sf.jsqlparser.util.validation.ValidationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Transactional
public class StudentDataListener extends AnalysisEventListener {

    private List<Students> list = new ArrayList<>();

    private StudentsMapper studentsMapper;

    private UsersMapper usersMapper;

    // 入学时间正则表达式
    private static final Pattern INPUT_TIME_PATTERN =
            Pattern.compile("^\\d{4}年(1[0-2]|[1-9])月$");

    public StudentDataListener(StudentsMapper studentsMapper, UsersMapper usersMapper) {
        this.studentsMapper = studentsMapper;
        this.usersMapper = usersMapper;
    }

    @Override
    public void invoke(Object data, AnalysisContext context) {
        StudentListImportData studentListImportData = (StudentListImportData) data;
        try {
            // 跳过学号已存在的记录
            if (studentsMapper.selectBySno(studentListImportData.getSno()) != null) {
                System.out.println("跳过学号重复的记录: " + studentListImportData.getSno());
                return;
            }

            // 数据校验，判断sno唯一
            validateData(studentListImportData);

            // 保存user表
            Users user = createUser(studentListImportData);
            int userId = saveUser(user, studentListImportData.getSno());

            //保存学生信息

            //将导入的数据转换为实体类
            Students student = new Students();
            student.setAge(studentListImportData.getAge());
            student.setSno(studentListImportData.getSno());
            student.setCollege(studentListImportData.getCollege());
            student.setMajor(studentListImportData.getMajor());
            student.setName(studentListImportData.getName());
            student.setPhone(studentListImportData.getPhone());
            student.setCredit(studentListImportData.getCredit());
            student.setGender(studentListImportData.getGender());
            student.setInputTime(studentListImportData.getInputTime());
            student.setUserId(userId);
            System.out.println("处理了一行数据" + student);
            studentsMapper.insert(student);
            list.add(student);
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

    private void validateData(StudentListImportData data) throws ValidationException {
        if (StringUtils.isBlank(data.getName())) {
            throw new ValidationException("姓名不能为空");
        }

        if (!Arrays.asList("男", "女", "其他").contains(data.getGender())) {
            throw new ValidationException("性别必须是'男'、'女'或'其他'");
        }

        if (data.getAge() != null && data.getAge() <= 0) {
            throw new ValidationException("年龄必须是正整数");
        }

        if (data.getSno() == null) {
            throw new ValidationException("学号不能为空");
        }

        if (data.getSno() != null && data.getSno().toString().length() != 11) {
            throw new ValidationException("学号必须是11位数字");
        }

        if (existsBySno(data.getSno())) {
            throw new ValidationException("学号已存在");
        }

        // 入学时间格式校验
        if (StringUtils.isBlank(data.getInputTime())) {
            throw new ValidationException("入学时间不能为空");
        }

        if (!INPUT_TIME_PATTERN.matcher(data.getInputTime()).matches()) {
            throw new ValidationException("入学时间格式应为'xxxx年x月'或'xxxx年xx月'");
        }

        // 学分校验
        if (data.getCredit() == null) {
            throw new ValidationException("学分不能为空");
        }

        if (data.getCredit() < 0 || data.getCredit() > 100) {
            throw new ValidationException("学分必须在0-100之间");
        }

        // 其他校验...
    }

    private Users createUser(StudentListImportData data) {
        Users user = new Users();
        user.setAccount(data.getSno());
        user.setPassword(data.getSno().toString());
        user.setRole("STUDENT");
        user.setIsActive(1);
        return user;
    }

    private boolean existsBySno(Long sno) {
        Students s = studentsMapper.selectBySno(sno);
        if (s != null && !s.getSno().equals(sno)) {
            //说明重复了，重复就跳过不管这条数据
            return true;
        }
        return false;
    }

    private int saveUser(Users user, Long sno) {
        //保存user
        usersMapper.insert(user);
        //拿到userId
        Integer userId = usersMapper.selectIdByAccount(sno);
        return userId;
    }
}
