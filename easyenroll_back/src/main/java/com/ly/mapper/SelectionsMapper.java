package com.ly.mapper;

import com.ly.pojo.Selections;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SelectionsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Selections record);

    int insertSelective(Selections record);

    Selections selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Selections record);

    int updateByPrimaryKey(Selections record);

    Selections selectByStudentIdAndCourseId(int preCourseId, Integer studentId);

    List<Selections> selectByStudentId(Integer studentId);

    List<Selections> selectByStudentIdByNormal(Integer studentId);

    int updateStatusById(Integer id);

    int deleteByCourseIdAndStudentId(Integer courseId, Integer studentId);

    void deleteByIds(List<Integer> ids, Integer studentId);

    boolean existsSelection(Integer studentId, Integer courseId);

    Integer selectIsSeckillByCourseIdAndStudentId(Integer courseId, Integer studentId);

    List<Integer> selectStudentIdsByCourseId(Integer courseId);

    List<Integer> selectCourseIdsByStudentId(Integer studentId);
}