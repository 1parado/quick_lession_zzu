package com.ly.mapper;

import com.ly.po.StudentsGradePO;
import com.ly.pojo.Students;
import com.ly.result.R;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Students record);

    int insertSelective(Students record);

    Students selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Students record);

    int updateByPrimaryKey(Students record);

    Students selectBySno(Long sno);

    int updateBySno(Students student);

    List<Students> selectStudent();

    Integer selectIdBySno(Long sno);

    List<Students> selectListByKeyword(String keyword);

    List<Students> selectStudentByIds(List<Integer> studentIds);

    List<Students> selectStudentBySearch(String searchText);

    Long selectSnoById(int studentId);

    List<Students> selectBySnos(List<Long> snos);

    void insertBatch(List<Students> list);

    List<Students> selectByHasPhone();

    List<StudentsGradePO> selectStudentGradeByIds(List<Integer> studentIds);

    List<Long> selectAllSno();

    String selectNameBySno(Long no);
}