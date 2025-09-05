package com.ly.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.StringUtils;
import com.ly.exportData.StudentGradeListImportData;
import com.ly.exportData.StudentListImportData;
import com.ly.mapper.CoursesMapper;
import com.ly.mapper.GradeMapper;
import com.ly.mapper.StudentsMapper;
import com.ly.pojo.Grade;
import com.ly.pojo.Students;
import com.ly.pojo.Users;
import net.sf.jsqlparser.util.validation.ValidationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Transactional
public class StudentGradeDataListener extends AnalysisEventListener {

    private StudentsMapper studentsMapper;

    private GradeMapper gradeMapper;

    private CoursesMapper coursesMapper;

    public StudentGradeDataListener(StudentsMapper studentsMapper, GradeMapper gradeMapper, CoursesMapper coursesMapper) {
        this.studentsMapper = studentsMapper;
        this.gradeMapper = gradeMapper;
        this.coursesMapper = coursesMapper;
    }

    @Override
    public void invoke(Object o, AnalysisContext analysisContext){
        StudentGradeListImportData studentGradeListImportData = (StudentGradeListImportData) o;

            // 跳过学号已存在的记录
            Grade grade1 = gradeMapper.selectBySnoByCourseCode(Long.valueOf(studentGradeListImportData.getSno()), studentGradeListImportData.getCourseCode());
            if (grade1 != null) {
                //可以不去跳过，改为更新成绩
                grade1.setGrade(studentGradeListImportData.getGrade());
                gradeMapper.updateByPrimaryKey(grade1);
                return;
            }

            // 数据校验，判断sno唯一
            validateData(studentGradeListImportData);

            // 保存grade表
            Grade grade = new Grade();
            grade.setCourseCode(studentGradeListImportData.getCourseCode());
            grade.setSno(Long.valueOf(studentGradeListImportData.getSno()));
            grade.setGrade(studentGradeListImportData.getGrade());
            grade.setState(studentGradeListImportData.getState());
            gradeMapper.insert(grade);
            System.out.println("处理了一行数据" + grade);


    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("Excel解析完成");
    }

    private void validateData(StudentGradeListImportData data) throws ValidationException {
        if (StringUtils.isBlank(data.getName())) {
            throw new ValidationException("姓名不能为空");
        }

        if (data.getSno() == null) {
            throw new ValidationException("学号不能为空");
        }

        if (data.getSno() != null && data.getSno().toString().length() != 11) {
            throw new ValidationException("学号必须是11位数字");
        }

        if (studentsMapper.selectBySno(Long.valueOf(data.getSno())) == null) {
            throw new ValidationException("学号不存在");
        }

        if (coursesMapper.selectByCourseCodePO(data.getCourseCode()) == null) {
            throw new ValidationException("课程不存在");
        }

        if (data.getGrade() == null) {
            throw new ValidationException("成绩不能为空");
        }

        // 其他校验...
    }
}
