package com.ly.service;

import com.ly.pojo.CourseRating;
import com.ly.pojo.Page;

import java.util.List;

public interface PageService {
    List<Page> getPartData(int size);

    List<Page> getHotList();

    List<Page> searchPartData(int size, String searchText);

    List<Page> getPartDataByCourseCode(int size, String courseCode);

    List<Page> searchPartDataByCourseCode(int size, String searchText, String courseCode);

    List<Page> getPartDataByCourseCodeByType(int size, String courseCode, String byType);

    List<CourseRating> recommendCourseBycontent(String courseCode, int count);

    List<CourseRating> recommendCourseByuser(String courseCode, int count);

    Page getPostById(int id);

    int deletePostById(int id, Long currentNo, Long loginNo);

    void updateCommentCountAddOne(int postId);

    void updateCommentCountSubOne(int postId);

    int savePost(Page page);
}
