package com.jobo.alipay;

import com.jobo.rxpay.base.IPayInfo;

/**
 * @Desc: 包含支付宝支付类型和支付信息
 * @author: admin wsj
 * @Date: 2021/12/28 2:33 下午
 */
public class AlipayInfoImpl implements IPayInfo {
    private String orderInfo;

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}
