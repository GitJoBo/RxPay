package com.jobo.rxpay.base;

import android.app.Activity;

import com.jobo.rxpay.callback.IPayCallback;

/**
 * @Desc: 统一支付接口。策略模式中统一算法接口。
 * @author: admin wsj
 * @Date: 2021/12/28 2:11 下午
 */
public interface IPayStrategy<T extends IPayInfo> {
    void pay(Activity activity, T payInfo, IPayCallback callback);
}
