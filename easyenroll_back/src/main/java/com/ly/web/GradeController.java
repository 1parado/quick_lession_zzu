package com.ly.web;

import com.github.pagehelper.PageInfo;
import com.ly.annotation.RoleRequire;
import com.ly.po.CourseGradePO;
import com.ly.result.R;
import com.ly.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grade")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping("/grade/{sno}")
    @RoleRequire("STUDENT")
    public R getGradeBySno(@PathVariable Long sno, @RequestParam int page, @RequestParam int size) {
        PageInfo<CourseGradePO> pageInfo = gradeService.getGradePOBySno(sno, page, size);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 根据学号/工号和课程代码，判断该学生是否在这门课有成绩/判断该教师是否教授这门课
     */
    @GetMapping("/grade/check/{no}/{code}")
    public R check(@PathVariable Long no, @PathVariable String code) {
        int i = gradeService.check(no, code);
        if (i == 1) {
            return R.success("通过");
        } else if (i == 2) {
          return R.error("学号/工号不能为空");
        } else {
            return R.error("您未获得加入该课程专属小组的资格");
        }
    }
}
