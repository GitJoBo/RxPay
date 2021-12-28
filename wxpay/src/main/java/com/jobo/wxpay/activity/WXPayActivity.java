package com.jobo.wxpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jobo.wxpay.WXPay;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * @Desc: 在调用方项目的 包名.wxapi.WXPayEntryActivity类直接继续本类，并在AndroidManifest.xml中声明即可。
 * @author: admin wsj
 * @Date: 2021/12/28 2:54 下午
 */
public class WXPayActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXPay.getInstance().getWXApi().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WXPay.getInstance().getWXApi().handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            WXPay.getInstance().onResp(baseResp.errCode, baseResp.errStr);
            finish();
        }
    }
}