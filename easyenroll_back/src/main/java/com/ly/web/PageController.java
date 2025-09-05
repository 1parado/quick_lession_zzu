package com.ly.web;

import com.ly.annotation.RoleRequire;
import com.ly.pojo.Comment;
import com.ly.pojo.CourseRating;
import com.ly.pojo.Like;
import com.ly.pojo.Page;
import com.ly.result.R;
import com.ly.service.CommentService;
import com.ly.service.CourseRatingService;
import com.ly.service.LikeService;
import com.ly.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/page")
public class PageController {

    @Autowired
    private PageService pageService;

    @Autowired
    private CourseRatingService courseRatingService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    /**
     * 获取部分数据
     * @param size
     * @return
     */
    @GetMapping("/page/{size}")
    public R getPartDataByPage(@PathVariable int size) {
        List<Page> pageList = pageService.getPartData(size);
        return R.success(pageList);
    }

    /**
     * 获取部分数据(有搜索时加载搜索结果)
     * @param size
     * @return
     */
    @GetMapping("/page/{searchText}/{size}")
    public R getPartDataByPage(@PathVariable String searchText, @PathVariable int size) {
        if (searchText == null) {
            List<Page> pageList = pageService.getPartData(size);
            return R.success(pageList);
        } else {
            //查搜索结果
            List<Page> pageList = pageService.searchPartData(size, searchText);
            return R.success(pageList);
        }
    }

    /**
     * 获取热议榜数据
     * @return
     */
    @GetMapping("/page/hotList")
    public R getHotList() {
        List<Page> hotList = pageService.getHotList();
        return R.success(hotList, String.valueOf(new Date()));
    }

    /**
     * 搜索贴子
     * @param searchText
     * @param size
     * @return
     */
    @GetMapping("/page/search/{searchText}/{size}")
    public R search(@PathVariable String searchText, @PathVariable int size) {
        List<Page> pageList = pageService.searchPartData(size, searchText);
        return R.success(pageList);
    }

    /**
     * 获得课程列表页面数据
     * @return
     */
    @GetMapping("/course")
    public R getCourseList() {
        /*
            拿到所有的课程（课程名，课程代码，综合负载评价）
            需要修改之前的课程表？这很痛苦，所以不改，选择创建一个课程评价表（课程名，课程代码，综合负载评价）
            在新增课程时候顺便新增课程评价表记录，
         */
        List<CourseRating> courseRatingList = courseRatingService.getAllCourse();
        return R.success(courseRatingList);
    }


    /**
     * 获取部分数据（某课程）
     * @param size
     * @return
     */
    @GetMapping("/page/course/{courseCode}/{size}")
    public R getPartDataByPageByCourse(@PathVariable int size, @PathVariable String courseCode) {
        List<Page> pageList = pageService.getPartDataByCourseCode(size, courseCode);
        return R.success(pageList);
    }

    /**
     * 获取部分数据（某课程）（by时间，热度？）
     * @param size
     * @return
     */
    @GetMapping("/page/course/byType/{courseCode}/{byType}/{size}")
    public R getPartDataByPageByCourseByType(@PathVariable int size, @PathVariable String courseCode, @PathVariable String byType) {
        List<Page> pageList = pageService.getPartDataByCourseCodeByType(size, courseCode, byType);
        return R.success(pageList);
    }

    /**
     * 获取部分数据(有搜索时加载搜索结果)(某课程)
     * @param size
     * @return
     */
    @GetMapping("/page/course/{searchText}/{courseCode}/{size}")
    public R getPartDataByPageByCourse(@PathVariable String searchText, @PathVariable int size, @PathVariable String courseCode) {
        if (searchText == null) {
            List<Page> pageList = pageService.getPartDataByCourseCode(size, courseCode);
            return R.success(pageList);
        } else {
            //查搜索结果
            List<Page> pageList = pageService.searchPartDataByCourseCode(size, searchText, courseCode);
            return R.success(pageList);
        }
    }

    /**
     * 搜索贴子(某课程)
     * @param searchText
     * @param size
     * @return
     */
    @GetMapping("/page/search/course/{searchText}/{courseCode}/{size}")
    public R search(@PathVariable String searchText, @PathVariable int size, @PathVariable String courseCode) {
        List<Page> pageList = pageService.searchPartDataByCourseCode(size, searchText, courseCode);
        return R.success(pageList);
    }

    /**
     * 基于内容的智能推荐课程
     * @param courseCode
     * @param count
     * @return
     */
    @GetMapping("/page/recommend/{courseCode}/{count}")
    public R recommendCourse(@PathVariable String courseCode, @PathVariable int count) {
        //根据该课程查找任课教师，根据教师所教授的课程进行推荐
        List<CourseRating> courseRatingList = pageService.recommendCourseBycontent(courseCode, count);
        return R.success(courseRatingList);
    }

    /**
     * 基于用户的智能推荐课程
     * @param courseCode
     * @param count
     * @return
     */
    @GetMapping("/page/recommend/byUser/{courseCode}/{count}")
    public R recommendCourseByUser(@PathVariable String courseCode, @PathVariable int count) {
        //根据该课程查找任课教师，根据教师所教授的课程进行推荐
        List<CourseRating> courseRatingList = pageService.recommendCourseByuser(courseCode, count);
        return R.success(courseRatingList);
    }

    /**
     * 根据id获取详细贴子
     * @param id
     * @return
     */
    @GetMapping("/page/post/{id}")
    public R getPostById(@PathVariable int id) {
        Page page = pageService.getPostById(id);
        return R.success(page);
    }

    /**
     * 根据id删除贴子
     * @param id
     * @return
     */
    @DeleteMapping("/page/post/{id}/{currentNo}/{loginNo}")
    public R deletePostById(@PathVariable int id, @PathVariable Long currentNo, @PathVariable Long loginNo) {
        int i = pageService.deletePostById(id, currentNo, loginNo);
        if (i != 1) {
            return R.error("删除失败");
        }
        return R.success("删除成功");
    }

    /**
     * 获取评论数据部分
     * @param postId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page/post/comment/{postId}")
    public R getCommentByPostId(@PathVariable int postId) {
        /*System.out.println("666");
        int offset = (page -1) * pageSize;
        //  初始化先来5条
        List<Comment> commentList = commentService.getCommentBySize(postId ,offset, pageSize);*/
        List<Comment> commentList = commentService.getComment(postId);
        return R.success(commentList);
    }

    /**
     * 发表评论
     */
    @PostMapping("/page/post/comment")
    public R saveComment(@RequestBody Comment comment) {
        //保存到comment表
        commentService.saveComment(comment);
        //修改page表的评论数
        pageService.updateCommentCountAddOne(comment.getRelatedPostId());

        return R.success("评论成功");
    }

    /**
     * 回复评论
     */
    @PostMapping("/page/post/comment/reply")
    public R replyComment(@RequestBody Comment comment) {
        //保存到comment表
        commentService.saveComment(comment);
        //修改page表的评论数
        pageService.updateCommentCountAddOne(comment.getRelatedPostId());
        return R.success("回复成功");
    }

    /**
     * 点赞API
     */
    @PostMapping("/page/post/comment/like")
    public R like(@RequestBody Like like) {
        int i = likeService.like(like);
        if (i == 1) {
            //点赞成功
            return R.success("点赞成功");
        } else if (i == 2) {
            //取消点赞成功
            return R.success("取消点赞成功");
        } else {
            return R.error("操作失败");
        }
    }


    /**
     * 删除评论
     * @param commentId
     * @return
     */
    @DeleteMapping("/page/post/comment/{commentId}/{loginNo}/{currentNo}/{postId}")
    public R deleteCommentById(@PathVariable int commentId, @PathVariable Long loginNo, @PathVariable Long currentNo, @PathVariable int postId) {
        //判断是否有删除权限
        if (!loginNo.equals(currentNo)){
            return R.error("您无权删除");
        }
        //删除评论
        commentService.deleteById(commentId);
        //修改page表的评论数
        pageService.updateCommentCountSubOne(postId);
        //删除相关的点赞信息
        likeService.deleteByCommentId(commentId);

        return R.success("删除成功");
    }

    @PostMapping("/page/post/publish")
    @RoleRequire("STUDENT")
    public R savePost(@RequestBody Page page) {
        System.out.println(page);
        int i = pageService.savePost(page);
        if (i == 1) {
            return R.success("保存成功");
        } else if (i == 2) {
            return R.error("不允许重复评价同一课程");
        } else if (i == 3){
            return R.error("关联的课程必须设置评分");
        }else {
            return R.error("失败");
        }
    }


}
