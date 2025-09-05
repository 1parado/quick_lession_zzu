今日计划：	

​	学生端教务系统	->	普通选课页面的查询功能		1

​	管理端五个OA的查询功能								1

​	

​	bug:页面个人课表（教师/学生）显示数据时用的是分页后的数据

​	

​	教师端/学生端个人课表的导出							1

​		导出按钮	--->	选择学期	--->	确认导出

​		文件：

​				列显示周一到周日

​				行显示时间段

​				每个单元格显示：课程名，上课地点，授课老师，周次

​	BUG：不同学期的课程一并导出了						1



​	管理页面的一键导入导出（批量增加）(3)、

​	

​	导入导出这一块

​		个人课表导出			1

​		课程管理的批量导出			1

​		教师端的课程下的学生列表导出	1

​			

​	



​	成绩(不重要)

​	

​	支付系统(1)





总结：EasyExcel使用

​	后端

```
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
    String fileName = URLEncoder.encode("课程表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

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
```

​	

前端
	

```
const exportCourseTable = async () => {
  try {
    // 设置responseType为blob，接收二进制流
    const response = await axios.get('/exp/export/course/' + sessionStorage.getItem('account') + '/' + sessionStorage.getItem('role')+ '/' + currentSemester.value, {
      responseType: 'blob' // 必须设置，否则会解析为JSON导致乱码
    });

    // 从响应头获取文件名（如果后端设置了的话）
    const contentDisposition = response.headers['content-disposition'];
    let fileName = '课程表.xlsx'; // 默认文件名
    if (contentDisposition) {
      // 解析文件名（适配后端URLEncoder编码的格式）
      fileName = decodeURIComponent(contentDisposition.split('filename*=utf-8\'\'')[1]);
    }

    // 创建Blob对象（指定MIME类型为Excel）
    const blob = new Blob([response.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    });

    // 创建下载链接并触发下载
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName; // 设置文件名
    document.body.appendChild(a);
    a.click(); // 触发点击下载

    // 清理资源
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);

    ElMessage.success('导出成功');
  } catch (error) {
    console.error('导出失败:', error);
    // 处理后端返回的错误信息（如果后端在异常时返回JSON）
    if (error.response?.data instanceof Blob) {
      // 若错误响应是Blob，尝试解析为JSON
      const reader = new FileReader();
      reader.onload = () => {
        const errorMsg = JSON.parse(reader.result).message || '导出失败';
        ElMessage.error(errorMsg);
      };
      reader.readAsText(error.response.data);
    } else {
      ElMessage.error('导出失败，请重试');
    }
  }
};
```

