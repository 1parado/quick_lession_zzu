package com.ly.service.impl;

import com.ly.mapper.CourseRatingMapper;
import com.ly.mapper.CoursesMapper;
import com.ly.mapper.PageMapper;
import com.ly.pojo.CourseRating;
import com.ly.pojo.Courses;
import com.ly.service.CourseRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseRatingServiceImpl implements CourseRatingService {

    @Autowired
    private CourseRatingMapper courseRatingMapper;

    @Autowired
    private CoursesMapper coursesMapper;

    @Autowired
    private PageMapper pageMapper;

    @Override
    public List<CourseRating> getAllCourse() {
        //在获取所有的课程负载评价数据之前，先删除所有数据再插入

        courseRatingMapper.deleteAll();

        List<Courses> coursesList = coursesMapper.selectAll();

        for (Courses course : coursesList) {

            CourseRating courseRating = new CourseRating();
            courseRating.setCourseName(course.getName());
            courseRating.setCourseCode(course.getCourseCode());
            // 查询该课程相关的所有帖子的负载评分的平均值
            List<Double> ratings = pageMapper.selectRatingByMentionedCourseCode(course.getCourseCode());
            Double sum = 0.0;
            for (Double r : ratings) {
                sum += r;
            }
            Double rating = sum / ratings.size();
            if (Double.isNaN(rating)) {
                // 处理NaN值，可以设置为null或默认值
                courseRating.setRating(0.0); // 或者设置为0.0等其他默认值
            } else {
                courseRating.setRating(rating);
            }

            courseRatingMapper.insert(courseRating);
        }

        return courseRatingMapper.selectAll();
    }
}
