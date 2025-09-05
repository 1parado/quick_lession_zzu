package com.ly.mapper;

import com.ly.pojo.Teachers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeachersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Teachers record);

    int insertSelective(Teachers record);

    Teachers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Teachers record);

    int updateByPrimaryKey(Teachers record);

    Teachers selectByTno(Long tno);

    int updateByTno(Teachers teacher);

    List<Teachers> selectTeacher();

    Integer selectIdByTno(Long tno);

    String selectNameByTno(Long account);

    List<Teachers> searchTeacher(String searchText);

    Long selectPhoneByName(String publicName);
}