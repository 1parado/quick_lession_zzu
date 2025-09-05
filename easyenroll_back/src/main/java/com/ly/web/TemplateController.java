package com.ly.web;

import com.github.pagehelper.PageInfo;
import com.ly.annotation.RoleRequire;
import com.ly.po.DraftPO;
import com.ly.pojo.Announcement;
import com.ly.pojo.SmsTemplate;
import com.ly.result.R;
import com.ly.service.RedisService;
import com.ly.service.SmsTemplateService;
import com.ly.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private SmsTemplateService smsTemplateService;

    @Autowired
    private RedisService redisService;

    @PostMapping("/template")
    @RoleRequire("ADMIN")
    public R saveTemplate(@RequestBody SmsTemplate smsTemplate) {
        int i = smsTemplateService.saveTemplate(smsTemplate);
        if (i != 1) {
            return R.error("保存失败");
        }
        return R.success("保存成功");
    }

    @GetMapping("/template/student")
    @RoleRequire("ADMIN")
    public R getStudentTemplate() {
        List<SmsTemplate> studentSmsTemplate = smsTemplateService.getStudentTemplate();
        return R.success(studentSmsTemplate);
    }

    @GetMapping("/template/teacher")
    @RoleRequire("ADMIN")
    public R getTeacherTemplate() {
        List<SmsTemplate> teacherSmsTemplate = smsTemplateService.getTeacherTemplate();
        return R.success(teacherSmsTemplate);
    }

    /**
     * 获取sms相关的草稿箱公告（不要admin发布的）
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/template/draft")
    @RoleRequire("ADMIN")
    public R getDraft(@RequestParam int page, @RequestParam int size) {
        PageInfo<DraftPO> pageInfo = smsTemplateService.getDraftPO(page, size, 1);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));

    }

    @PutMapping("/template/student/status")
    @RoleRequire("ADMIN")
    public R updateStudentTemplateStatus(@RequestBody SmsTemplate smsTemplate) {
        smsTemplateService.updateStatusByStudent(smsTemplate);
        return R.success("选中成功");
    }

    @PutMapping("/template/teacher/status")
    @RoleRequire("ADMIN")
    public R updateTeacherTemplateStatus(@RequestBody SmsTemplate smsTemplate) {
        smsTemplateService.updateStatusByTeacher(smsTemplate);
        return R.success("选中成功");
    }


    @PutMapping("/template/student")
    @RoleRequire("ADMIN")
    public R updateStudentTemplate(@RequestBody SmsTemplate smsTemplate) {
        int i = smsTemplateService.updateByStudentTemplate(smsTemplate);
        if (i != 1) {
            return R.error("修改失败");
        }
        return R.success("修改成功");
    }

    @PutMapping("/template/teacher")
    @RoleRequire("ADMIN")
    public R updateTeacherTemplate(@RequestBody SmsTemplate smsTemplate) {
        int i = smsTemplateService.updateByTeacherTemplate(smsTemplate);
        if (i != 1) {
            return R.error("修改失败");
        }
        return R.success("修改成功");
    }

    @PutMapping("/template/time/{time}")
    @RoleRequire("ADMIN")
    public R setNotifyTime(@PathVariable int time, @RequestBody DraftPO draftPO) {
        int i = smsTemplateService.setNotifyTime(draftPO,time);
        if (i != 1) {
            return R.error("设置失败");
        }
        return R.success("设置成功");
    }

    @DeleteMapping("/template/student/{id}")
    @RoleRequire("ADMIN")
    public R deleteStudentTemplate(@PathVariable int id) {
        int i = smsTemplateService.deleteStudentTemplate(id);
        if (i != 1) {
            return R.error("删除失败");
        }
        return R.success("删除成功");
    }

    @DeleteMapping("/template/teacher/{id}")
    @RoleRequire("ADMIN")
    public R deleteTeacherTemplate(@PathVariable int id) {
        int i = smsTemplateService.deleteTeacherTemplate(id);
        if (i != 1) {
            return R.error("删除失败");
        }
        return R.success("删除成功");
    }

    @GetMapping("/template/smsSwitch/{smsSwitch}")
    @RoleRequire("ADMIN")
    public R changeSmsSwitch(@PathVariable String smsSwitch) {
        System.out.println(smsSwitch);
        redisService.setValue(Constant.SWITCH_SMS, smsSwitch);
        return R.success("设置成功");
    }

}
