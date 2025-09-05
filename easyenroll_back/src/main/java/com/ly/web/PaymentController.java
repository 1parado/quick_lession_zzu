package com.ly.web;

import com.alipay.api.internal.util.AlipaySignature;
import com.github.pagehelper.PageInfo;
import com.ly.annotation.RoleRequire;
import com.ly.mapper.StudentsMapper;
import com.ly.po.PaymentPO;
import com.ly.pojo.Payment;
import com.ly.pojo.Students;
import com.ly.result.R;
import com.ly.service.PaymentSelectionService;
import com.ly.service.PaymentService;
import com.ly.util.PayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay")
public class PaymentController {

    @Autowired
    private PayUtil payUtil;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private PaymentSelectionService paymentSelectionService;

    /**
     * 获取未缴费项目（分页）
     * @param studentId
     * @return
     */
    @GetMapping("/payment/{studentId}")
    @RoleRequire("STUDENT")
    public R getNoPayProject(@PathVariable int studentId, @RequestParam int page, @RequestParam int size) {
        PageInfo<Payment> pageInfo = paymentService.getNoPayProjectList(studentId, page, size);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 获取对应学生已缴费项目（分页）
     * @param studentId
     * @return
     */
    @GetMapping("/payment/payed/{studentId}")
    @RoleRequire("STUDENT")
    public R getPayedProject(@PathVariable int studentId, @RequestParam int page, @RequestParam int size) {
        PageInfo<PaymentPO> pageInfo = paymentService.getPayedProjectList(studentId, page, size);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 获取所有缴费项目
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/payment/all")
    @RoleRequire("ADMIN")
    public R getAllProject(@RequestParam int page, @RequestParam int size) {
        PageInfo<Payment> pageInfo = paymentService.getAllProject(page, size);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 获取对应缴费项目的所有缴费人（学生列表）
     * @param id
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/payment/student/{id}")
    @RoleRequire("ADMIN")
    public R getStudentList (@PathVariable int id, @RequestParam int page, @RequestParam int size) {
        PageInfo<Students> pageInfo = paymentService.getStudentList(page, size, id);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 修改缴费项目
     * @param payment
     * @return
     */
    @PutMapping("/payment")
    @RoleRequire("ADMIN")
    public R updateProject(@RequestBody Payment payment) {
        int i = paymentService.updatePayment(payment);
        if (i == 1) {
            return R.success(payment);
        } else if (i == 2) {
            return R.error("时间设置有误");
        } else if (i == 3) {
            return R.error("金额设置有误");
        } else if (i == 4) {
            return R.error("应缴费人数设置有误");
        }
        return R.error("系统错误");
    }

    /**
     * 新增缴费项目
     * @param payment
     * @return
     */
    @PostMapping("/payment")
    @RoleRequire("ADMIN")
    public R saveProject(@RequestBody Payment payment) {
        int i = paymentService.savePayment(payment);
        if (i == 1) {
            return R.success(payment);
        } else if (i == 2) {
            return R.error("时间设置有误");
        } else if (i == 3) {
            return R.error("金额设置有误");
        } else if (i == 4) {
            return R.error("应缴费人数设置有误");
        }

        return R.error("系统错误");
    }

    /**
     * 改变信息状态（暂时无用）
     * @param
     * @return
     */
    @GetMapping("/alipay/{out_trade_no}")
    public R notify(@PathVariable String out_trade_no) {
        String[] s = out_trade_no.split("_");
        int payId = Integer.valueOf(s[0]);
        Long payerId = Long.valueOf(s[1]);
        // 判断是否调用成功
        boolean isSuccess = payUtil.query(out_trade_no);
        if (isSuccess) {
            System.out.println("支付成功");
            // 修改缴费信息状态
            paymentSelectionService.updateStatus(payId, payerId);
            // 使缴费项目的已缴费人数更新
            paymentService.updatePaid_count(payId);
        } else {
            System.out.println("支付失败");
            // 删除payment_selection表中的相关失败数据
            paymentSelectionService.deleteOne(payId, payerId);
            // 减少相关缴费项目的已缴费人数
            paymentService.updatePaid_count(payId);
        }
        return R.success(null);
    }

    /**
     * 删除缴费项目（级联删除缴费记录表）
     */
    @DeleteMapping("/payment/{id}")
    @RoleRequire("ADMIN")
    public R deletePayment(@PathVariable int id) {
        int i = paymentService.deletePayment(id);
        if (i != 1) {
            return R.error("删除失败");
        }
        return R.success("删除成功");
    }

}