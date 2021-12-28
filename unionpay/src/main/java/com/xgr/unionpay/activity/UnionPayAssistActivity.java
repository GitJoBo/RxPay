package com.xgr.unionpay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.xgr.unionpay.unionpay.UnionPay;
import com.xgr.unionpay.unionpay.UnionPayInfoImpl;

/**
 * 银联辅助类。由于银联回调必须在onActivityResult中获取，为了使用方便，故拉起一个透明的activity来处理该问题。
 */
public class UnionPayAssistActivity extends AppCompatActivity {

    UnionPayInfoImpl mUnionPayInfoImpli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mUnionPayInfoImpli = getIntent().getParcelableExtra(UnionPay.EXTRA_UNIONPAYINFO);
        if(mUnionPayInfoImpli == null){
            finish();
        }else {
            UnionPay.pay(this,mUnionPayInfoImpli);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UnionPay.handleResult(this,data);
    }
}
