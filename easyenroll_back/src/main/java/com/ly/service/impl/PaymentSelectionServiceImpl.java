package com.ly.service.impl;

import com.ly.mapper.PaymentSelectionMapper;
import com.ly.po.PaymentPO;
import com.ly.pojo.PaymentSelection;
import com.ly.service.PaymentSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class PaymentSelectionServiceImpl implements PaymentSelectionService {

    @Autowired
    private PaymentSelectionMapper paymentSelectionMapper;

    @Override
    public void generateSelection(PaymentPO paymentPO) {
        //  插入之前先查询，若有则删除
        PaymentSelection paymentSelection1 = paymentSelectionMapper.selectByPayIdAndPayerId(paymentPO.getId(), paymentPO.getPayerId());
        if (paymentSelection1 != null) {
            //说明有，删除
            paymentSelectionMapper.deleteOne(paymentPO.getId(), paymentPO.getPayerId());
        }
        PaymentSelection paymentSelection = new PaymentSelection();
        paymentSelection.setPayerId(paymentPO.getPayerId());
        paymentSelection.setPayId(paymentPO.getId());
        paymentSelection.setPayStatus(0);
        paymentSelection.setPayTime(new Date());
        paymentSelection.setPayMethod("支付宝");
        paymentSelectionMapper.insert(paymentSelection);
    }

    @Override
    public void updateStatus(int payId, Long payerId) {
        PaymentSelection paymentSelection = new PaymentSelection();
        paymentSelection.setPayId(payId);
        paymentSelection.setPayerId(payerId);
        paymentSelection.setPayMethod("支付宝");
        paymentSelection.setPayTime(new Date());
        paymentSelection.setPayStatus(1);
        paymentSelectionMapper.updateByPayIdAndPayerId(paymentSelection);
    }

    @Override
    public void deleteOne(Integer id, Long payerId) {
        paymentSelectionMapper.deleteOne(id, payerId);
    }
}
