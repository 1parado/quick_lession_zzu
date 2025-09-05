package com.ly.web;

import com.github.pagehelper.PageInfo;
import com.ly.annotation.RoleRequire;
import com.ly.dto.BatchSelectDTO;
import com.ly.dto.PreSelectionDTO;
import com.ly.dto.SelectionAndCode;
import com.ly.po.CoursesPO;
import com.ly.pojo.Courses;
import com.ly.pojo.Selections;
import com.ly.pojo.Students;
import com.ly.result.R;
import com.ly.service.PreSelectionService;
import com.ly.service.SelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sel")
public class SelectionController {

    @Autowired
    private SelectionService selectionService;

    @Autowired
    private PreSelectionService preSelectionService;

    /**
     * 保存选课信息到表中
     * @param selections
     * @return
     */
    @PostMapping("/selection")
    @RoleRequire("STUDENT")
    public R insertNoSeckillSelection(@RequestBody Selections selections){
        System.out.println(selections);
        int i = selectionService.saveNoSeckillSelection(selections);
        if (i == 1) {
            return R.success("保存成功");
        } else if (i == 2) {
            return R.error("前置课程未选择");
        } else if (i == 3) {
            return R.error("课程时间冲突");
        } else {
            return R.error("保存失败");
        }
    }

    /**
     * 根据学生id获取选择列表(不分页)
     * @param studentId
     * @return
     */
    @GetMapping("/selection/{studentId}")
    @RoleRequire("STUDENT")
    public R getSelectionsByStudentId(@PathVariable Integer studentId) {
        List<Selections> list = selectionService.getListByStudentId(studentId);
        return R.success(list);
    }

    /**
     * 根据学生id获取选择列表(不分页),额外多返回一个courseCode
     * @param studentId
     * @return
     */
    @GetMapping("/selection/courseCode/{studentId}")
    @RoleRequire("STUDENT")
    public R getSelectionsCodeByStudentId(@PathVariable Integer studentId) {
        List<SelectionAndCode> list = selectionService.getListCodeByStudentId(studentId);
        return R.success(list);
    }

    /**
     * 根据学生id获取选择列表(分页)
     * @param studentId
     * @return
     */
    @GetMapping("/selection/list/{studentId}")
    @RoleRequire({"STUDENT","ADMIN"})
    public R getCourseListByStudentId(@RequestParam int page, @RequestParam int size, @PathVariable Integer studentId) {
        PageInfo<CoursesPO> pageInfo = selectionService.getCourseByNormal(page, size, studentId);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 根据学生id获取选择列表(全部)
     * @param studentId
     * @return
     */
    @GetMapping("/selection/fullList/{studentId}")
    @RoleRequire({"STUDENT","ADMIN"})
    public R getCourseFullListByStudentId(@PathVariable Integer studentId) {
        List<CoursesPO> list= selectionService.getCourseByNormalFull(studentId);
        return R.success(list);
    }

    /**
     * 根据学生id获取选择列表(不分页)
     * @param studentId
     * @return
     */
    @GetMapping("/selection/list/noPage/{account}")
    @RoleRequire({"STUDENT","ADMIN"})
    public R getCourseListByStudentIdNoPage(@PathVariable Long account) {

        List<CoursesPO> list = selectionService.getCourseByNormalNoPage(account);
        return R.success(list);
    }

    /**
     * 根据学生id获取选择列表(分页)（教师课表）
     * @param studentId
     * @return
     */
    @GetMapping("/selection/list/teacher/{tno}")
    @RoleRequire("TEACHER")
    public R getTeacherCourseListByStudentId(@RequestParam int page, @RequestParam int size, @PathVariable Long tno) {
        PageInfo<CoursesPO> pageInfo = selectionService.getTeacherCourseByNormal(page, size, tno);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }



    /**
     * 根据学生id获取选择列表(不分页)（教师课表）
     * @param studentId
     * @return
     */
    @GetMapping("/selection/list/teacher/noPage/{tno}")
    @RoleRequire("TEACHER")
    public R getTeacherCourseListByStudentIdNoPage(@PathVariable Long tno) {
        List<CoursesPO> coursesPOList = selectionService.getTeacherCourseByNormalNoPage(tno);
        return R.success(coursesPOList);
    }



    /**
     * 根据课程id和学生id删除选择
     * @param
     * @return
     */
    @DeleteMapping("/selection/{courseId}/{studentId}")
    @RoleRequire({"STUDENT","ADMIN"})
    public R deleteStatusById(@PathVariable Integer courseId, @PathVariable Integer studentId) {
        int i = selectionService.deleteStatusByCourseIdAndStudentId(courseId, studentId);
        if (i == 1) {
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }

    /**
     * 批量选择
     */
    @PostMapping("/selection/batch")
    @RoleRequire({"ADMIN"})
    public R batchSelect(@RequestBody BatchSelectDTO batchSelectDTO) {
        //对每个学生做一次循环，遍历所有的课程，生成选课记录保存到数据库，（需要检查是否选择过，避免重复选课）
        //n表示成功选择了几条数据
        System.out.println("学生列表：" + batchSelectDTO.getSelectedStudents());
        System.out.println("课程列表" + batchSelectDTO.getSelectedCourses());
        Integer n = selectionService.batchSelect(batchSelectDTO.getSelectedStudents(), batchSelectDTO.getSelectedCourses());
        if (n == 1) {
            return R.success("一键选择成功");
        } else if (n == -1) {
            return R.error("部分学生尚未满足所选课程的前置课程要求");
        } else if (n == -2) {
            return R.error("选课组合中存在时间排布冲突");
        } else {
            return R.error("系统错误");
        }
    }

    @PostMapping("/selection/pre")
    @RoleRequire("STUDENT")
    public R preSelection(@RequestBody PreSelectionDTO preData) {
        System.out.println("studentId:" + preData.getStudentId());
        System.out.println("courseCodes:" + preData.getCourseCodes());
        //拿到数据，保存数据（数据库/Redis）
        int i = preSelectionService.savePreSelection(preData);
        if (i != 1) {
            return R.error("预选失败");
        }
        //配合后端的一分钟定时任务定时扫描秒杀任务，设置秒杀任务状态（未开始、正在抢课中、已结束）
        //对于状态改变的（变为ONGOING的选课任务），模拟真是用户抢课
        // 去根据秒杀任务关联的课程代码查询预选表，这样就得到课程代码和学生id，参与秒杀（秒杀成功设置删除预选记录）。
        return R.success("预选成功");
    }

}
