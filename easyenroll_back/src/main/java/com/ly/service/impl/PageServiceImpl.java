package com.ly.service.impl;

import com.ly.mapper.*;
import com.ly.pojo.CourseRating;
import com.ly.pojo.Page;
import com.ly.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PageServiceImpl implements PageService {

    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private CoursesMapper coursesMapper;

    @Autowired
    private CourseRatingMapper courseRatingMapper;

    @Autowired
    private SelectionsMapper selectionsMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private TeachersMapper teachersMapper;

    @Override
    public List<Page> getPartData(int size) {
        return pageMapper.selectPartBySize(size);
    }

    @Override
    public List<Page> getPartDataByCourseCode(int size, String courseCode) {
        return pageMapper.selectPartBySizeByCourseCode(size, courseCode);
    }

    @Override
    public List<Page> getPartDataByCourseCodeByType(int size, String courseCode, String byType) {
        if ("time".equals(byType)) {
            return pageMapper.selectPartBySizeByCourseCode(size, courseCode);
        } else {
            return pageMapper.selectPartBySizeByCourseCodeByHot(size, courseCode);
        }
    }

    @Override
    public List<Page> getHotList() {
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.YEAR, -1); // 减一年

        Date oneYearAgo = calendar.getTime();

        return pageMapper.selectHotList(oneYearAgo);
    }

    @Override
    public List<Page> searchPartData(int size, String searchText) {

        List<Page> pageList = pageMapper.selectPartBySizeByText(size, searchText);

        //查询完毕后判断一下，查询结果数量是否足够，不足的话，可以修改searchText进行一次不精确的查询保证结果数量
        if (pageList.size() != size && searchText.length() >= 3) {
            //掐头去尾
            String newSearchText = searchText.substring(1, searchText.length() - 1);
            pageList = pageMapper.selectPartBySizeByText(size, newSearchText);
        }

        return pageList;
    }

    @Override
    public List<Page> searchPartDataByCourseCode(int size, String searchText, String courseCode) {
        List<Page> pageList = pageMapper.selectPartBySizeByTextByCourseCode(size, searchText, courseCode);

        //查询完毕后判断一下，查询结果数量是否足够，不足的话，可以修改searchText进行一次不精确的查询保证结果数量
        if (pageList.size() != size && searchText.length() >= 3) {
            //掐头去尾
            String newSearchText = searchText.substring(1, searchText.length() - 1);
            pageList = pageMapper.selectPartBySizeByTextByCourseCode(size, newSearchText, courseCode);
        }

        return pageList;
    }

    @Override
    public List<CourseRating> recommendCourseBycontent(String courseCode, int count) {
        //根据该课程查找任课教师，根据教师所教授的课程进行推荐
        //通过课程代码获取教师id
        Integer teacherId = coursesMapper.selectTeacherIdByCourseCode(courseCode);
        //通过教师id找到课程代码列表(只拿count条)
        List<String> courseCodeList = coursesMapper.selectCourseCodeByTeacherId(teacherId, count + 1);
        List<String> newCourseCodeList = new ArrayList<>();
        for (String code : courseCodeList) {
            if (code.equals(courseCode)) {
                continue;
            }
            newCourseCodeList.add(code);
        }
        //根据课程代码列表找到course_rating表中的相关记录
        List<CourseRating> courseRatingList = courseRatingMapper.selectByCourseCodeList(newCourseCodeList);

        return courseRatingList;
    }

    @Override
    public List<CourseRating> recommendCourseByuser(String courseCode, int count) {
        //根据该课程查找选择了这么课程的学生（50个学生），查找每个学生的选择课程，拿到重复次数从高到低的n个课程。（排除本课程）

        //根据courseCode拿到courseId
        Integer courseId = coursesMapper.selectIdByCourseCode(courseCode);
        //在selections表，根据courseId拿到studentIds
        List<Integer> studentIds = selectionsMapper.selectStudentIdsByCourseId(courseId);
        //对每一个学生id拿到对应的courseId集合
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer studentId : studentIds) {
            List<Integer> courseIds = selectionsMapper.selectCourseIdsByStudentId(studentId);

            //现在得到一堆courseId集合，计算出出现频率最高的三个courseId
            //将每一个courseIds中的courseId一个一个插入一个HashMap中，作为key，value默认为0，key重复则value+1
            for (Integer newCourseId : courseIds) {
                if (map.containsKey(newCourseId)){
                    //重复，value+1
                    Integer newValue = map.get(newCourseId) + 1;
                    map.remove(newCourseId);
                    map.put(newCourseId, newValue);
                } else {
                    //新的
                    map.put(newCourseId, 1);
                }
            }
        }
        //计算对比map，取出最高value的三个courseId
        List<Integer> topThreeKeys = map.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(count + 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        //根据courseId集合（过滤后的三个）拿到 对应的 courseCodeList
        List<String> courseCodeList = new ArrayList<>();
        for (Integer newCourseId : topThreeKeys) {
            if (newCourseId.equals(coursesMapper.selectIdByCourseCode(courseCode))) {
                continue;
            }
            String newCourseCode = coursesMapper.selectCourseCodeByIdPO(newCourseId);
            courseCodeList.add(newCourseCode);
        }

        //根据courseCodeList拿到CourseRatingList，返回
        List<CourseRating> courseRatingList = courseRatingMapper.selectByCourseCodeList(courseCodeList);

        return courseRatingList;
    }

    @Override
    public Page getPostById(int id) {
        return pageMapper.selectByPrimaryKey(id);
    }

    @Override
    public int deletePostById(int id, Long currentNo, Long loginNo) {
        //先判断是否有资格
        if (!currentNo.equals(loginNo)) {
            return -1;
        }
        return pageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateCommentCountAddOne(int postId) {
        pageMapper.updateCommentCountById(postId);
    }

    @Override
    public void updateCommentCountSubOne(int postId) {
        pageMapper.updateCommentCountByIdSub(postId);
    }

    @Override
    public int savePost(Page page) {

        //在发布之前判断是否当前用户对相关课程评价过，只允许评价一次
        Page page1 = null;
        if (page.getMentionedCourseCode() == null || page.getMentionedCourseCode().isEmpty()) {

        } else {
            page1 = pageMapper.selectByNoAndCourseCode(page.getPublishSno(), page.getMentionedCourseCode());
        }

        if (page1 != null) {
            //说明重复评价了
            return 2;
        }

        String name = studentsMapper.selectNameBySno(page.getPublishSno());
        if (name == null) {
            name = teachersMapper.selectNameByTno(page.getPublishSno());
        }

        if (name != null) {
            page.setPublishName(name);
        }

        page.setPublishTime(new Date());
        page.setCommentCount(0L);

        if (page.getMentionedCourseCode() == null || page.getMentionedCourseCode().isEmpty()) {
            page.setType(0);
            // 如果是任意贴评分不需要，设置为0
            page.setRating(0.0);
        } else {
            page.setType(1);
            // 如果是评分贴，评分为0，返回报错
            if (page.getRating().equals(0.0)) {
                return 3;
            }
        }



        pageMapper.insert(page);

        return 1;
    }
}
