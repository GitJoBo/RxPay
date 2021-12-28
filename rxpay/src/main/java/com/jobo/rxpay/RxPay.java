package com.jobo.rxpay;

import android.app.Activity;

import com.jobo.rxpay.base.IPayInfo;
import com.jobo.rxpay.base.IPayStrategy;
import com.jobo.rxpay.callback.IPayCallback;

/**
 * @Desc: 策略模式场景类。
 * @author: admin wsj
 * @Date: 2021/12/28 2:12 下午
 * example : 实例化支付策略payway,以及支付订单信息，作为参数直接传入。
 * 使用方法1：调用RxPay.pay()方法即可。
 * 使用方法2：实例化payStrategy,直接调用其pay方法。如：new Alipay().pay(...)
 */
public class RxPay {
    public static <T extends IPayInfo> void pay(IPayStrategy<T> payStrategy, Activity activity,
                                                T payInfo, IPayCallback callback) {
        payStrategy.pay(activity, payInfo, callback);
    }
}
