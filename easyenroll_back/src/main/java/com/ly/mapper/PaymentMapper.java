package com.ly.mapper;

import com.ly.po.PaymentPO;
import com.ly.pojo.Payment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Payment record);

    int insertSelective(Payment record);

    Payment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Payment record);

    int updateByPrimaryKey(Payment record);

    List<Payment> selectAll();

    List<Payment> selectByNoId(List<Integer> payIds);

    List<PaymentPO> selectByIdBySno(List<Integer> payIds, Long sno);

    void updatePaidCountById(Integer id, int count);

}