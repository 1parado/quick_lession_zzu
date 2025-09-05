package com.ly.mapper;

import com.ly.pojo.PaymentSelection;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentSelectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PaymentSelection record);


    PaymentSelection selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaymentSelection record);

    int updateByPrimaryKey(PaymentSelection record);

    List<Integer> selectPayIdByPayerId(Long sno);

    Integer selectByPayId(Integer id);

    List<Long> selectPayerIdByPayId(Integer payId);

    void updateByPayIdAndPayerId(PaymentSelection paymentSelection);

    int deleteByPayId(int id);

    void deleteOne(Integer id, Long payerId);

    PaymentSelection selectByPayIdAndPayerId(Integer id, Long payerId);

    Integer selectByPayIdByPay(Integer id);
}