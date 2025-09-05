package com.ly.web;

import com.ly.pojo.Students;
import com.ly.pojo.Teachers;
import com.ly.pojo.Users;
import com.ly.result.R;
import com.ly.service.StudentService;
import com.ly.service.TeacherService;
import com.ly.service.UserService;
import com.ly.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;
    public static String role;

    @RequestMapping("/test")
    public R test() {
        return R.success("哈哈哈");
    }

    /**
     * 登录
     * @param logUser
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody Users logUser) {
        //获取用户
        Users user = userService.getUserByAccount(logUser.getAccount());

        //缓存用户的角色
        role = (String) user.getRole();

        //验证用户
        userService.checkUserLogin(user,logUser.getPassword());

        //生成token
        String token = jwtUtils.generateToken(String.valueOf(user.getAccount()), (String) user.getRole());

        //返回结果
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);

        return R.success(data);
    }

    /**
     * 根据账号去查找学生或者教师
     * @param account
     * @param role
     * @return
     */
    @GetMapping("/user/{account}/{role}")
    public R getUserByAccount(@PathVariable Long account, @PathVariable String role) {
        if (role.equals("STUDENT")) {
            Students student = studentService.getStudentBySno(account);
            return R.success(student);
        } else if (role.equals("TEACHER")) {
            Teachers teacher = teacherService.getTeacherByTno(account);
            return R.success(teacher);
        } else if (role.equals("ADMIN")) {
            return R.error("无个人信息");
        }
        return R.error("获取失败");
    }

    /**
     * 修改手机号
     * @param account
     * @param role
     * @param phone
     * @return
     */
    @PostMapping("/user/updatePhone/{account}/{role}")
    public R updatePhone(
            @PathVariable Long account,
            @PathVariable String role,
            @RequestBody String phone){
        System.out.println(phone);
        String StrPhone = phone.replace('=',' ').trim();
        System.out.println("|" + StrPhone + "|");
        int i = userService.updatePhone(account,role,StrPhone);
        System.out.println(phone);
        if (i == 1){
            return R.success("手机号更新成功成功");
        }
        return R.error("更新手机号失败");
    }

    /**
     * 修改密码
     * @param account
     * @param role
     * @param password
     * @return
     */
    @PutMapping("/user/updatePassword/{account}/{role}")
    public R updatePassword(
            @PathVariable Long account,
            @PathVariable String role,
            @RequestBody String password
    ) {
        System.out.println(password);
        int i = userService.updatePassword(account, role, password);
        if (i != 1) {
            return R.error("更新失败");
        }
        return R.success("更新成功");
    }



}
