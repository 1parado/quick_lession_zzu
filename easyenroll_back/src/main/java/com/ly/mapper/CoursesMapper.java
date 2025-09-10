package com.ly.mapper;

import com.ly.po.CoursesPO;
import com.ly.pojo.Courses;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CoursesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Courses record);

    int insertSelective(Courses record);

    Courses selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Courses record);

    int updateByPrimaryKey(Courses record);

    List<CoursesPO> selectCourse();

    CoursesPO selectByCourseCodePO(String courseCode);

    String selectCourseCodeByIdPO(Integer id);

    int updatePreCourseByoldCode(@Param("oldCode") String oldCode,@Param("newCode") String newCode);

    Integer selectIdByCourseCode(String preCourse);

    int updateByPreCourseId(Integer id);

    int updateByTeacherId(Integer id);

    List<CoursesPO> selectCourseByIsSeckill();

    //int selectIdByTeacherId(Integer id);
    List<Integer> selectIdByTeacherId(Integer id);

    List<CoursesPO> selectCourseByNOIsSeckill();

    Integer selectPreCourseIdById(Integer courseId);

    List<CoursesPO> selectCourseByCourseId(List<Integer> courseIdList);

    List<Integer> selectIdByPreCourseId(Integer preCourseId);

    void updateRemainByCourseId(Integer courseId);

    void updateRemainByCourseIdAdd(Integer preCourseId);

    List<CoursesPO> selectCourseByTeacherId(Integer teacherId);

    List<CoursesPO> selectCourseBySearch(String searchText);

    List<CoursesPO> selectAllCourseBySearch(String searchText);

    List<CoursesPO> selectCourseByCourseIdBySemester(List<Integer> courseIdList, String currentSemester);

    List<CoursesPO> selectCourseByTeacherIdBySemester(Integer teacherId, String currentSemester);

    List<CoursesPO> selectCourseByTeacherIdByExam(Integer teacherId);

    List<Courses> selectAll();

    Integer selectTeacherIdByCourseCode(String courseCode);

    List<String> selectCourseCodeByTeacherId(Integer teacherId, int count);

    Courses selectByCourseCodeAndTeacherId(String code, Integer teacherId);

    Integer selectIdByCourseName(String courseName);

    String selectCourseNameById(Integer id);
}