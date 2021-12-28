package com.jobo.ap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.jobo.alipay.AliPay
import com.jobo.alipay.AlipayInfoImpl
import com.jobo.rxpay.RxPay
import com.jobo.rxpay.callback.IPayCallback
import com.jobo.wxpay.WXPay
import com.jobo.wxpay.WXPayInfoImpl
import com.xgr.unionpay.unionpay.UnionPay
import com.xgr.unionpay.unionpay.UnionPayInfoImpl

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.unionpay).setOnClickListener(View.OnClickListener { unionpay() })
        findViewById<Button>(R.id.wxpay).setOnClickListener(View.OnClickListener { wxpay() })
        findViewById<Button>(R.id.alipay).setOnClickListener(View.OnClickListener { alipay() })
    }

    /**
     * 银联提供了测试帐号：
     * 测试卡号信息：
     * 借记卡：6226090000000048
     * 手机号：18100000000
     * 密码：111101
     * 短信验证码：123456
     * （短信验证码记得点下获取验证码之后再输入）
     * 测试订单生成网址：http://101.231.204.84:8091/sim/getacptn，生成后直接传入setTn()。
     */
    private fun unionpay() {
        //实例化银联支付策略
        val unionPay = UnionPay()
        //构造银联订单实体。一般都是由服务端直接返回。测试时可以用Mode.TEST,发布时用Mode.RELEASE。
        val unionPayInfoImpl = UnionPayInfoImpl()
        unionPayInfoImpl.setTn("625623784203097901200")
        unionPayInfoImpl.setMode(com.xgr.unionpay.unionpay.Mode.TEST)
        //策略场景类调起支付方法开始支付，以及接收回调。
        RxPay.pay(unionPay, this, unionPayInfoImpl, object : IPayCallback {
            override fun success() {
                toast("支付成功")
            }

            override fun failed(code: Int, message: String?) {
                toast("支付失败")
            }

            override fun cancel() {
                toast("支付取消")
            }
        })
    }

    private fun wxpay() {
        //实例化微信支付策略
        val wxPay: WXPay = WXPay.getInstance()
        //构造微信订单实体。一般都是由服务端直接返回。
        val wxPayInfoImpl = WXPayInfoImpl()
        wxPayInfoImpl.setTimestamp("")
        wxPayInfoImpl.setSign("")
        wxPayInfoImpl.setPrepayId("")
        wxPayInfoImpl.setPartnerid("")
        wxPayInfoImpl.setAppid("")
        wxPayInfoImpl.setNonceStr("")
        wxPayInfoImpl.setPackageValue("")
        //策略场景类调起支付方法开始支付，以及接收回调。
        RxPay.pay(wxPay, this, wxPayInfoImpl, object : IPayCallback {
            override fun success() {
                toast("支付成功")
            }

            override fun failed(code: Int, message: String?) {
                toast("支付失败")
            }

            override fun cancel() {
                toast("支付取消")
            }
        })
    }

    private fun alipay() {
        //实例化支付宝支付策略
        val aliPay = AliPay()
        //构造支付宝订单实体。一般都是由服务端直接返回。
        val alipayInfoImpl = AlipayInfoImpl()
        alipayInfoImpl.setOrderInfo("alipay_sdk=alipay-sdk-java-3.7.4.ALL&app_id=2019050964424243&biz_content=%7B%22body%22%3A%22%E4%BA%91%E6%8E%A7%E7%8C%AB%22%2C%22out_trade_no%22%3A%221475641535289749504%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E4%BA%91%E6%8E%A7%E7%8C%AB%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%224680.0%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F122.112.142.215%2Fmedical%2Fpay%2FandroidNotify&sign=kw7Y%2FPfmyH%2FWzI1zVMFMKsOZc3AGffyhSgX466Q4AOCacuvK1t2wkEL2iCBIrr4k%2BJKPYPbit6N0FsdSE1eNUvZATBjKwOOYcxsay3CMHtkMjdgfeOgyiH9%2Bk5e%2BMH3DG0SHRedC6EtCVaJPGuccVxe6Znc8UUe10Fe8U4GE8rRDlVNLAYyxD%2BarMGSAOdGHpbLaVWMWSTt8iw4JIFelTPWjc78zGjgmahwPzQvXPG0Xm6QHBmZxY2lF4JALu4RznyxYty3oZudaZLKgNNS9mQuos8FS22QqAG6QrNQN%2BFkasKTQV2HSjUDci9aXOrcwJSErWLkJaI7Vsk71CJRUKA%3D%3D&sign_type=RSA2&timestamp=2021-12-28+09%3A35%3A32&version=1.0")
        //策略场景类调起支付方法开始支付，以及接收回调。
        RxPay.pay(aliPay, this, alipayInfoImpl, object : IPayCallback {
            override fun success() {
                toast("支付成功")
            }

            override fun failed(code: Int, message: String?) {
                toast("支付失败")
            }

            override fun cancel() {
                toast("支付取消")
            }
        })
    }

    private fun toast(context: String) {
        Toast.makeText(this, context, Toast.LENGTH_LONG).show()
    }
}