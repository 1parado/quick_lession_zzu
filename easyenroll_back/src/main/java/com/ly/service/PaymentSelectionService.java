package com.ly.service;

import com.ly.po.PaymentPO;

public interface PaymentSelectionService {
    void generateSelection(PaymentPO paymentPO);

    void updateStatus(int payId, Long payerId);


    void deleteOne(Integer id, Long payerId);
}
