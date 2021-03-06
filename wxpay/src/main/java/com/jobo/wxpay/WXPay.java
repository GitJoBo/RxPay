/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2017, King, china
**                          All Rights Reserved
**                                
**                              By(King)
**                         
**------------------------------------------------------------------------------
*/
package com.jobo.wxpay;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.jobo.rxpay.base.IPayStrategy;
import com.jobo.rxpay.callback.IPayCallback;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @Desc:
 * @author: admin wsj
 * @Date: 2021/12/28 2:54 下午
 * @see <a href="https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=1417751808&token=&lang=zh_CN">Des</a>
 */
public class WXPay implements IPayStrategy<WXPayInfoImpl> {

    private static WXPay mWXPay;
    private WXPayInfoImpl payInfoImpli;
    private static IPayCallback sPayCallback;
    private IWXAPI mWXApi;
    private boolean initializated;

    private WXPay() {

    }

    public static WXPay getInstance(){
        if(mWXPay == null){
            synchronized (WXPay.class){
                if(mWXPay == null) {
                    mWXPay = new WXPay();
                }
            }
        }
        return mWXPay;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    private void initWXApi(Context context, String appId) {
        mWXApi = WXAPIFactory.createWXAPI(context.getApplicationContext(), appId);
        mWXApi.registerApp(appId);
        initializated = true;
    }

    @Override
    public void pay(Activity activity, WXPayInfoImpl payInfo, IPayCallback payCallback) {
        this.payInfoImpli = payInfo;
        sPayCallback = payCallback;

        if(payInfoImpli == null || TextUtils.isEmpty(payInfoImpli.getAppid()) || TextUtils.isEmpty(payInfoImpli.getPartnerid())
                || TextUtils.isEmpty(payInfoImpli.getPrepayId()) || TextUtils.isEmpty(payInfoImpli.getPackageValue()) ||
                TextUtils.isEmpty(payInfoImpli.getNonceStr()) || TextUtils.isEmpty(payInfoImpli.getTimestamp()) ||
                TextUtils.isEmpty(payInfoImpli.getSign())) {
            if(payCallback != null) {
                payCallback.failed(WXErrCodeEx.CODE_ILLEGAL_ARGURE, WXErrCodeEx.getMessageByCode(WXErrCodeEx.CODE_ILLEGAL_ARGURE));
            }
            return;
        }

        if (!initializated) {
            initWXApi(activity.getApplicationContext(), payInfoImpli.getAppid());
        }

        if(!check()) {
            if(payCallback != null) {
                payCallback.failed(WXErrCodeEx.CODE_UNSUPPORT, WXErrCodeEx.getMessageByCode(WXErrCodeEx.CODE_UNSUPPORT));
            }
            return;
        }

        PayReq req = new PayReq();
        req.appId = payInfoImpli.getAppid();
        req.partnerId = payInfoImpli.getPartnerid();
        req.prepayId = payInfoImpli.getPrepayId();
        req.packageValue = payInfoImpli.getPackageValue();
        req.nonceStr = payInfoImpli.getNonceStr();
        req.timeStamp = payInfoImpli.getTimestamp();
        req.sign = payInfoImpli.getSign();

        mWXApi.sendReq(req);
    }

    /**
     * 支付回调响应
     */
    public void onResp(int errorCode, String errorMsg) {
        if(sPayCallback == null) {
            return;
        }

        if(errorCode == BaseResp.ErrCode.ERR_OK) {
            sPayCallback.success();
        } else if(errorCode == BaseResp.ErrCode.ERR_COMM) {
            sPayCallback.failed(errorCode, errorMsg);
        } else if(errorCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            sPayCallback.cancel();
        } else {
            sPayCallback.failed(errorCode, errorMsg);
        }

        sPayCallback = null;
    }

    /**
     * 检测是否支持微信支付
     */
    private boolean check() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }
}
