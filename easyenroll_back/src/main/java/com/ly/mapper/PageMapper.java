package com.ly.mapper;

import com.ly.pojo.Page;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Page record);

    int insertSelective(Page record);

    Page selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Page record);

    int updateByPrimaryKey(Page record);

    List<Page> selectPartBySize(int size);

    List<Page> selectHotList(Date oneYearAgo);

    List<Page> selectPartBySizeByText(int size, String searchText);

    List<Page> selectPartBySizeByCourseCode(int size, String courseCode);

    List<Page> selectPartBySizeByTextByCourseCode(int size, String searchText, String courseCode);

    List<Page> selectPartBySizeByCourseCodeByHot(int size, String courseCode);

    void updateCommentCountById(int postId);

    void updateCommentCountByIdSub(int postId);

    List<Double> selectRatingByMentionedCourseCode(String courseCode);

    Page selectByNoAndCourseCode(Long publishSno, String mentionedCourseCode);
}