/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2017, King, china
**                          All Rights Reserved
**                                
**                               By(King)
**                         
**------------------------------------------------------------------------------
*/
package com.jobo.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.jobo.rxpay.base.IPayStrategy;
import com.jobo.rxpay.callback.IPayCallback;

import java.util.Map;

/**
 * @Desc:
 * @author: admin wsj
 * @Date: 2021/12/28 2:34 下午
 * @see <a href="https://docs.open.alipay.com/204/">Des</a>
 */
public class AliPay implements IPayStrategy<AlipayInfoImpl> {

    private static final int SDK_PAY_FLAG = 6406;
    private Activity mActivity;
    private AlipayInfoImpl alipayInfoImpli;
    private static IPayCallback sPayCallback;

    @Override
    public void pay(Activity activity, AlipayInfoImpl payInfo, IPayCallback payCallback) {
        this.mActivity = activity;
        this.alipayInfoImpli = payInfo;
        sPayCallback = payCallback;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果
                Map<String,String> result = alipay.payV2(alipayInfoImpli.getOrderInfo(),true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档:
                    //https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.IXE2Zj&treeId=59&articleId=103671&docType=1
                    if (TextUtils.equals(resultStatus, ResultCode.CODE_SUCCESS)) {
                        if (sPayCallback != null) {
                            sPayCallback.success();
                        }
                    } else if(TextUtils.equals(resultStatus, ResultCode.CODE_CANCEL)){
                        if(sPayCallback != null){
                            sPayCallback.cancel();
                        }
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        if (sPayCallback != null) {
                            sPayCallback.failed(ResultCode.getIntCodeByString(resultStatus), ResultCode.getTextByCode(resultStatus));
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

}
