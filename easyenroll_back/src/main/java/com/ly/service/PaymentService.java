package com.ly.service;

import com.github.pagehelper.PageInfo;
import com.ly.po.PaymentPO;
import com.ly.pojo.Payment;
import com.ly.pojo.Students;

public interface PaymentService {
    PageInfo<Payment> getNoPayProjectList(int studentId, int page, int size);

    PageInfo<PaymentPO> getPayedProjectList(int studentId, int page, int size);

    PageInfo<Payment> getAllProject(int page, int size);

    int updatePayment(Payment payment);

    int savePayment(Payment payment);

    PageInfo<Students> getStudentList(int page, int size, Integer id);

    int deletePayment(int id);

    void updatePaid_count(Integer id);

}
