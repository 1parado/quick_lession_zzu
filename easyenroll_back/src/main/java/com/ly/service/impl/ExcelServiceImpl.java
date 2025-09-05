package com.ly.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.dto.GradeDTO;
import com.ly.exportData.*;
import com.ly.listener.StudentDataListener;
import com.ly.listener.StudentGradeDataListener;
import com.ly.listener.TeacherDataListener;
import com.ly.mapper.*;
import com.ly.po.CoursesPO;
import com.ly.pojo.Selections;
import com.ly.pojo.Students;
import com.ly.pojo.Teachers;
import com.ly.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private CoursesMapper coursesMapper;

    @Autowired
    private SelectionsMapper selectionsMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private TeachersMapper teachersMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private GradeMapper gradeMapper;

    @Override
    public void exportCourseTable(Long account, String role, String currentSemester, HttpServletResponse response) throws Exception {
        List<CoursesPO> courses = new ArrayList<>();
        if ("STUDENT".equals(role)) {
            Integer studentId = usersMapper.selectIdByAccount(account);
            List<Selections> selections = selectionsMapper.selectByStudentIdByNormal(studentId);
            // 如果没有任何选课记录
            if (selections == null || selections.isEmpty()) {
                return;
            }

            List<Integer> courseIdList = new ArrayList<>();
            for (Selections selection : selections) {
                courseIdList.add(selection.getCourseId());
            }

            courses = coursesMapper.selectCourseByCourseIdBySemester(courseIdList, currentSemester);
        } else if ("TEACHER".equals(role)) {
            Integer teacherId = teachersMapper.selectIdByTno(account);
            courses = coursesMapper.selectCourseByTeacherIdBySemester(teacherId, currentSemester);
        } else {
            return;
        }

        // 将课程数据转换为课程表格式
        List<CourseTableExportData> exportDataList = convertToTableFormat(courses);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 初始化样式策略
        HorizontalCellStyleStrategy styleStrategy = getStyleStrategy();

        // 使用EasyExcel导出
        try {
            EasyExcel.write(response.getOutputStream(), CourseTableExportData.class)
                    .sheet("课程表")
                    .registerWriteHandler(styleStrategy) // 注册样式
                    .doWrite(exportDataList);
        } catch (Exception e) {
            throw new Exception("导出课程表失败");
        }

    }

    /**
     * 定义Excel样式策略（调整单元格大小、自动换行等）
     */
    private HorizontalCellStyleStrategy getStyleStrategy() {
        // 1. 表头样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 表头背景色（浅灰色）
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 12); // 表头字体大小
        headWriteFont.setBold(true); // 加粗
        headWriteCellStyle.setWriteFont(headWriteFont);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER); // 水平居中

        // 2. 内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 内容字体大小
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 11);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 自动换行（关键：让多行内容正常显示）
        contentWriteCellStyle.setWrapped(true);
        // 垂直居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 单元格边框
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);

        // 返回样式策略（表头样式 + 内容样式）
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    private List<CourseTableExportData> convertToTableFormat(List<CoursesPO> courses) {
        // 初始化时间段的空行
        Map<String, CourseTableExportData> timeSlotMap = new LinkedHashMap<>();
        String[] timeSlots = {
                "8:00-9:40",
                "10:00-11:40",
                "13:30-15:10",
                "15:30-17:10",
                "18:30-20:10"
        };

        for (String time : timeSlots) {
            CourseTableExportData row = new CourseTableExportData();
            row.setTime(time);
            timeSlotMap.put(time, row);
        }

        // 填充课程信息
        for (CoursesPO course : courses) {
            // 解析上课时间，例如："每周一，第一、二节课" -> 周一 8:00-9:40
            String[] parts = parseClassTime(course.getClassTime());
            String dayOfWeek = parts[0]; // 周一/周二等
            String timeSlot = parts[1]; // 8:00-9:40等

            CourseTableExportData row = timeSlotMap.get(timeSlot);
            if (row != null) {
                String courseInfo = course.getName() + "\n" +
                        course.getClassLocation() + "\n" +
                        course.getTeacherName() + "\n" +
                        "层次：" + course.getWeekRange();

                switch (dayOfWeek) {
                    case "周一": row.setMonday(courseInfo); break;
                    case "周二": row.setTuesday(courseInfo); break;
                    case "周三": row.setWednesday(courseInfo); break;
                    case "周四": row.setThursday(courseInfo); break;
                    case "周五": row.setFriday(courseInfo); break;
                    case "周六": row.setSaturday(courseInfo); break;
                    case "周日": row.setSunday(courseInfo); break;
                }
            }
        }

        return new ArrayList<>(timeSlotMap.values());
    }

    private List<StudentListExportData> convertToStudentListFormat(List<Students> students) {
        List<StudentListExportData> exportDataList = new ArrayList<>();

        for (Students student : students) {
            StudentListExportData exportData = new StudentListExportData();
            exportData.setId(student.getId());
            exportData.setUser_id(student.getUserId());
            exportData.setName(student.getName());
            exportData.setGender(student.getGender());
            exportData.setAge(student.getAge());
            exportData.setSno(student.getSno());
            exportData.setCollege(student.getCollege());
            exportData.setMajor(student.getMajor());
            exportData.setCredit(student.getCredit());
            exportData.setPhone(student.getPhone());
            exportData.setInput_time(student.getInputTime());

            exportDataList.add(exportData);
        }

        return exportDataList;
    }

    private String[] parseClassTime(String classTime) {
        if (classTime == null || classTime.isEmpty()) {
            return new String[]{"", ""};
        }

        // 定义“节课”到时间段的映射（键：节课描述，值：对应的时间段）
        Map<String, String> classToTimeSlot = new HashMap<>();
        classToTimeSlot.put("第1、2节", "8:00-9:40");
        classToTimeSlot.put("第3、4节", "10:00-11:40");
        classToTimeSlot.put("第5、6节", "13:30-15:10");
        classToTimeSlot.put("第7、8节", "15:30-17:10");
        classToTimeSlot.put("第9、10节", "18:30-20:10");

        // 提取星期（周一至周日）
        String dayOfWeek = "";
        if (classTime.contains("周一")) {
            dayOfWeek = "周一";
        } else if (classTime.contains("周二")) {
            dayOfWeek = "周二";
        } else if (classTime.contains("周三")) {
            dayOfWeek = "周三";
        } else if (classTime.contains("周四")) {
            dayOfWeek = "周四";
        } else if (classTime.contains("周五")) {
            dayOfWeek = "周五";
        } else if (classTime.contains("周六")) {
            dayOfWeek = "周六";
        } else if (classTime.contains("周日")) {
            dayOfWeek = "周日";
        }

        // 提取节课描述，匹配对应的时间段
        String timeSlot = "";
        for (Map.Entry<String, String> entry : classToTimeSlot.entrySet()) {
            if (classTime.contains(entry.getKey())) {
                timeSlot = entry.getValue();
                break;
            }
        }

        return new String[]{dayOfWeek, timeSlot};
    }

    @Override
    public void exportStudentList(Integer courseId, HttpServletResponse response) throws Exception {
        //根据courseId得到学生ids
        List<Integer> studentIds = selectionsMapper.selectStudentIdsByCourseId(courseId);

        //根据ids得到学生列表
        List<Students> students = studentsMapper.selectStudentByIds(studentIds);

        // 将课程数据转换为课程表格式
        List<StudentListExportData> exportDataList = convertToStudentListFormat(students);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 初始化样式策略
        //HorizontalCellStyleStrategy styleStrategy = getStyleStrategy();

        // 使用EasyExcel导出
        try {
            EasyExcel.write(response.getOutputStream(), StudentListExportData.class)
                    .sheet("学生列表")
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 注册样式
                    .doWrite(exportDataList);
        } catch (Exception e) {
            throw new Exception("导出学生列表失败");
        }

    }

    @Override
    public void exportCourseList(HttpServletResponse response) throws Exception {
        List<CoursesPO> courses = coursesMapper.selectCourse();

        // 将课程数据转换为课程表格式
        List<CourseListExportData> exportDataList = new ArrayList<>();
        for (CoursesPO coursesPO : courses) {
            CourseListExportData exportData = new CourseListExportData();
            exportData.setId(coursesPO.getId());
            exportData.setCourseCode(coursesPO.getCourseCode());
            exportData.setName(coursesPO.getName());
            exportData.setCollege(coursesPO.getCollege());
            exportData.setCredit(coursesPO.getCredit());
            exportData.setCourseType(coursesPO.getCourseType());
            exportData.setCapacity(coursesPO.getCapacity());
            exportData.setRemain(coursesPO.getRemain());
            exportData.setIsSeckill(coursesPO.getIsSeckill());
            exportData.setDescription(coursesPO.getDescription());
            exportData.setTeacherName(coursesPO.getTeacherName());
            exportData.setTno(coursesPO.getTno());
            exportData.setSemester(coursesPO.getSemester());
            exportData.setClassTime(coursesPO.getClassTime());
            exportData.setClassLocation(coursesPO.getClassLocation());
            exportData.setWeekRange(coursesPO.getWeekRange());
            exportData.setPreCourseCode(coursesPO.getPreCourseCode());

            exportDataList.add(exportData);
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 初始化样式策略
        //HorizontalCellStyleStrategy styleStrategy = getStyleStrategy();

        // 使用EasyExcel导出
        try {
            EasyExcel.write(response.getOutputStream(), CourseListExportData.class)
                    .sheet("课程列表")
                    //.registerWriteHandler(styleStrategy) // 注册样式
                    .doWrite(exportDataList);
        } catch (Exception e) {
            throw new Exception("导出课程列表失败");
        }
    }

    @Override
    public void exportStudentListAll(HttpServletResponse response) throws Exception {
        List<Students> studentsList = studentsMapper.selectStudent();

        // 将课程数据转换为课程表格式
        List<StudentListExportData> exportDataList = convertToStudentListFormat(studentsList);

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 使用EasyExcel导出
        try {
            EasyExcel.write(response.getOutputStream(), StudentListExportData.class)
                    .sheet("课程列表")
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 注册样式
                    .doWrite(exportDataList);
        } catch (Exception e) {
            throw new Exception("导出学生列表失败");
        }
    }

    @Override
    public void exportGradeBySno(Long sno, HttpServletResponse response) throws Exception {

        //查询成绩数据（课程名，课程代码，成绩，状态，bySno）
        List<GradeDTO> gradeDTOList = gradeMapper.selectGradeDTOBySno(sno);
        // 将成绩数据转换为成绩表格式
        List<GradeExportData> exportDataList = new ArrayList<>();
        for (GradeDTO gradeDTO : gradeDTOList) {
            GradeExportData gradeExportData = new GradeExportData();
            gradeExportData.setGrade(gradeDTO.getGrade());
            gradeExportData.setName(gradeDTO.getCourseName());
            gradeExportData.setCourseCode(gradeDTO.getCourseCode());
            if (gradeDTO.getState() == 1) {
                gradeExportData.setStateText("正常");
            } else if (gradeDTO.getState() == 0) {
                gradeExportData.setStateText("缺考");
            } else if (gradeDTO.getState() == 2) {
                gradeExportData.setStateText("作弊");
            }

            exportDataList.add(gradeExportData);
        }


        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 使用EasyExcel导出
        try {
            EasyExcel.write(response.getOutputStream(), GradeExportData.class)
                    .sheet("个人成绩表")
                    .doWrite(exportDataList);
        } catch (Exception e) {
            throw new Exception("导出个人成绩表失败");
        }
    }

    @Override
    public void exportTeacherList(HttpServletResponse response) throws Exception {
        List<Teachers> teachersList = teachersMapper.selectTeacher();

        // 将课程数据转换为课程表格式
        List<TeacherListExportDate> exportDataList = new ArrayList<>();

        for (Teachers teacher : teachersList) {
            TeacherListExportDate teacherListExportDate = new TeacherListExportDate();
            teacherListExportDate.setId(teacher.getId());
            teacherListExportDate.setUserId(teacher.getUserId());
            teacherListExportDate.setName(teacher.getName());
            teacherListExportDate.setTno(teacher.getTno());
            teacherListExportDate.setGender(teacher.getGender());
            teacherListExportDate.setAge(teacher.getAge());
            teacherListExportDate.setCollege(teacher.getCollege());
            teacherListExportDate.setTitle(teacher.getTitle());
            teacherListExportDate.setPhone(teacher.getPhone());

            exportDataList.add(teacherListExportDate);
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 使用EasyExcel导出
        try {
            EasyExcel.write(response.getOutputStream(), TeacherListExportDate.class)
                    .sheet("教师列表")
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 注册样式
                    .doWrite(exportDataList);
        } catch (Exception e) {
            throw new Exception("导出教师列表失败");
        }
    }

    @Override
    public void exportStudentTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=student_template.xlsx");
        List<StudentListImportData> templateData = Arrays.asList(
                new StudentListImportData("张三", "男", 21, 20221614001L,
                        "软件学院", "软件工程", 3.8, 13800138000L, "2022年9月"),
                new StudentListImportData("李四", "女", 20, 20221614002L,
                        "计算机学院", "计算机科学", 3.5, 13900139000L, "2022年9月")
        );
        EasyExcel.write(response.getOutputStream(), StudentListImportData.class)
                .sheet("学生数据模板")
                .doWrite(templateData);
    }

    @Override
    public void exportStudentGradeTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=student_template.xlsx");
        List<StudentGradeListImportData> templateData = Arrays.asList(
                new StudentGradeListImportData("8888001", "张三", "20221614001", 90.0, 1),
                new StudentGradeListImportData("8888002", "李四", "20221614002", 95.5, 1)
        );
        EasyExcel.write(response.getOutputStream(), StudentGradeListImportData.class)
                .sheet("学生成绩数据模板")
                .doWrite(templateData);
    }

    @Override
    public void importStudent(MultipartFile file) throws IOException {
        StudentDataListener listener = new StudentDataListener(studentsMapper, usersMapper);
        EasyExcel.read(file.getInputStream(), StudentListImportData.class, listener).sheet().doRead();
    }

    @Override
    public void importStudentGrade(MultipartFile file) throws IOException {
        StudentGradeDataListener listener = new StudentGradeDataListener(studentsMapper, gradeMapper, coursesMapper);
        EasyExcel.read(file.getInputStream(), StudentGradeListImportData.class, listener).sheet().doRead();
    }

    @Override
    public void exportTeacherTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=student_template.xlsx");
        List<TeacherListImportData> templateData = Arrays.asList(
                new TeacherListImportData("刘老师", 6666001L, "男", 50, "软件学院", "院长", 18234806385L),
                new TeacherListImportData("张老师", 6666001L, "女", 38, "软件学院", "特级教师", 15801236014L)
        );
        EasyExcel.write(response.getOutputStream(), TeacherListImportData.class)
                .sheet("教师数据模板")
                .doWrite(templateData);
    }

    @Override
    public void importTeacher(MultipartFile file) throws IOException {
        TeacherDataListener listener = new TeacherDataListener(teachersMapper, usersMapper);
        EasyExcel.read(file.getInputStream(), TeacherListImportData.class, listener).sheet().doRead();
    }
}
