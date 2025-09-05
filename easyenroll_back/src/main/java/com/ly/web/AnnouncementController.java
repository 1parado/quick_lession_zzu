package com.ly.web;

import com.github.pagehelper.PageInfo;
import com.ly.annotation.RoleRequire;
import com.ly.pojo.Announcement;
import com.ly.pojo.Students;
import com.ly.result.R;
import com.ly.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ann")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 新增公告(保存在草稿箱/发送/删除状态)
     * @param announcement
     * @return
     */
    @PostMapping("/announcement/{account}/{is}")
    @RoleRequire({"ADMIN","TEACHER"})
    public R insertAnnouncement(
            @PathVariable Long account,
            @PathVariable int is,
            @RequestBody Announcement announcement) {
        System.out.println(is);
        System.out.println(announcement);
        //当is是1时，表示状态为DRAFT
        //当is是2时，表示状态为PUBLISHED
        //当is是0时，表示状态为DELETED

        int i = announcementService.insertAnnouncement(announcement, is, account);
        if (i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 获取公告列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/announcement/{status}")
    public R getAnnouncementList(@PathVariable int status, @RequestParam int page, @RequestParam int size) {
        /*
                status
                   1    草稿
                   2    已发布
                   0    已删除
         */
        if (status == 3) {
            Announcement announcement = announcementService.getAnnouncementOne(status);
            return R.success(announcement);
        }

        PageInfo<Announcement> pageInfo = announcementService.getAnnouncement(page, size, status);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 获取公告列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/announcement/teacher/{status}/{tno}")
    public R getTeacherAnnouncementList(@PathVariable int status, @PathVariable Long tno, @RequestParam int page, @RequestParam int size) {
        /*
                status
                   1    草稿
                   2    已发布
                   0    已删除
         */

        PageInfo<Announcement> pageInfo = announcementService.getTeacherAnnouncement(page, size, status, tno);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }


    /**
     * 获取所有已发布的公告列表
     * @return
     */
    @GetMapping("/announcement/allPublish")
    public R getAllAnnouncement() {
        List<Announcement> announcementList = announcementService.getAllPublish();
        return R.success(announcementList);
    }

    /**
     * 删除公告(修改状态为已删除)
     * @param id
     * @return
     */
    @DeleteMapping("/announcement/{id}")
    @RoleRequire({"ADMIN","TEACHER"})
    public R deleteAnnouncement(@PathVariable int id) {
        int i = announcementService.deleteAnnouncementById(id);
        if(i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 更新公告信息
     * @param announcement
     * @return
     */
    @PutMapping("/announcement")
    @RoleRequire({"ADMIN","TEACHER"})
    public R updateAnnouncement(@RequestBody Announcement announcement) {
        int i = announcementService.updateAnnouncement(announcement);
        if (i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 彻底删除
     * @param id
     * @return
     */
    @DeleteMapping("/announcement/true/{id}")
    @RoleRequire({"ADMIN","TEACHER"})
    public R deleteAnnouncementTrue(@PathVariable int id) {
        int i = announcementService.deleteAnnouncementByIdTrue(id);
        if(i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 恢复数据（已删除 -> 草稿）
     * @param id
     * @return
     */
    @PostMapping("/announcement/re/{id}")
    @RoleRequire({"ADMIN","TEACHER"})
    public R reAnnouncement(@PathVariable int id) {
        int i = announcementService.reAnnouncement(id);
        if(i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 发布公告（草稿 -> 已发布，时间修改为当前时间）
     * @param announcement
     * @return
     */
    @PutMapping("/announcement/send")
    @RoleRequire({"ADMIN","TEACHER"})
    public R sendAnnouncement(@RequestBody Announcement announcement) {
        int i = announcementService.updateAnnouncementToPublished(announcement);
        if (i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

}
