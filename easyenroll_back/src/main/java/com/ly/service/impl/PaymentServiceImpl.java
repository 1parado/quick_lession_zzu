package com.ly.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.mapper.PaymentMapper;
import com.ly.mapper.PaymentSelectionMapper;
import com.ly.mapper.StudentsMapper;
import com.ly.po.PaymentPO;
import com.ly.pojo.Payment;
import com.ly.pojo.PaymentSelection;
import com.ly.pojo.Students;
import com.ly.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private PaymentSelectionMapper paymentSelectionMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    @Override
    public PageInfo<Payment> getNoPayProjectList(int studentId, int page, int size) {
        PageHelper.startPage(page, size);
        //  根据学生id查找学生学号
        Long sno = studentsMapper.selectSnoById(studentId);
        //注意此处的payerid是学生学号并非id
        //根据学生学号，在缴费记录表中查找对应的缴费项目id集合
        List<Integer> payIds = paymentSelectionMapper.selectPayIdByPayerId(sno);
        List<Payment> paymentList = new ArrayList<>();
        //如果集合不为空，则说明有项目需要排除，不显示在页面
        if (payIds != null && !payIds.isEmpty()) {
            //  不为空，即代表当前学生已经缴费过部分缴费项目，或者全部缴费完毕
            //  将这部分缴费项目拿到，在页面中排除展示
            paymentList = paymentMapper.selectByNoId(payIds);
        } else {
            //若集合为空，则显示所有项目
            paymentList = paymentMapper.selectAll();
        }
        PageInfo<Payment> pageInfo = PageInfo.of(paymentList);
        return pageInfo;
    }

    @Override
    public PageInfo<PaymentPO> getPayedProjectList(int studentId, int page, int size) {
        PageHelper.startPage(page, size);
        //  根据学生id查找学生学号
        Long sno = studentsMapper.selectSnoById(studentId);
        //注意此处的payerid是学生学号并非id
        //根据学生学号，在缴费记录表中查找对应的缴费项目id集合
        List<Integer> payIds = paymentSelectionMapper.selectPayIdByPayerId(sno);
        List<PaymentPO> paymentList = new ArrayList<>();
        //如果集合不为空，说明集合中对应的id就是要显示在页面的数据
        if (payIds != null && !payIds.isEmpty()) {
            //  不为空，即代表当前学生已经缴费过部分缴费项目，或者全部缴费完毕
            //  将这部分缴费项目拿到，在页面中排除展示
            paymentList = paymentMapper.selectByIdBySno(payIds, sno);
        }
        PageInfo<PaymentPO> pageInfo = PageInfo.of(paymentList);
        return pageInfo;
    }

    @Override
    public PageInfo<Payment> getAllProject(int page, int size) {
        PageHelper.startPage(page, size);
        List<Payment> paymentList = paymentMapper.selectAll();
        List<Payment> realPaymntList = new ArrayList<>();
        //遍历paymentList，对每一个payment的id在payment_selection中查找，统计个数，该个数就是已缴费人数
        for (Payment payment : paymentList) {
            Integer count = paymentSelectionMapper.selectByPayId(payment.getId());
            payment.setPaidCount(count);
            realPaymntList.add(payment);
        }
        //无需判断缴费人数的正确，已缴费人数/应缴费人数，只需要注意不重复缴费
        PageInfo<Payment> pageInfo = PageInfo.of(realPaymntList);
        return pageInfo;
    }

    @Override
    public int updatePayment(Payment payment) {
        //判断缴费时间的正确性
        if (payment.getStartTime().after(payment.getEndTime())) {
            return 2;//表示时间设置有误
        }
        if (payment.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            return 3; //表示金额设置有误
        }
        if (payment.getPayableCount() < 0) {
            return 4; //表示应缴费人数设置有误
        }
        int i = paymentMapper.updateByPrimaryKeySelective(payment);
        return i == 1 ? 1 : 0;
    }

    @Override
    public int savePayment(Payment payment) {
        //判断缴费时间的正确性
        if (payment.getStartTime().after(payment.getEndTime())) {
            return 2;//表示时间设置有误
        }
        if (payment.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            return 3; //表示金额设置有误
        }
        if (payment.getPayableCount() < 0) {
            return 4; //表示应缴费人数设置有误
        }
        payment.setPaidCount(0);
        int i = paymentMapper.insertSelective(payment);
        return i == 1 ? 1 : 0;
    }

    @Override
    public PageInfo<Students> getStudentList(int page, int size, Integer payId) {
        PageHelper.startPage(page, size);
        //根据缴费项目id，在payment_selection表中查找对应的payerId即学生学号集合
        List<Long> snos = paymentSelectionMapper.selectPayerIdByPayId(payId);
        List<Students> studentsList = new ArrayList<>();
        if (snos != null && !snos.isEmpty()) {
            //根据学生学号集合查找学生列表返回
           studentsList = studentsMapper.selectBySnos(snos);
        }
        PageInfo<Students> pageInfo = PageInfo.of(studentsList);
        return pageInfo;
    }

    @Override
    public int deletePayment(int id) {
        //id是paymentId

        //先根据该id，在payment_selection表中根据pay_id去删除记录
        int i = paymentSelectionMapper.deleteByPayId(id);
        //再根据id删除payment表
        int i1 = paymentMapper.deleteByPrimaryKey(id);
        return i1;
    }

    @Override
    public void updatePaid_count(Integer id) {
        Integer i = paymentSelectionMapper.selectByPayIdByPay(id);
        paymentMapper.updatePaidCountById(id, i);
    }

}
