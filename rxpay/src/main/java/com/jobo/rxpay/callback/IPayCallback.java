package com.jobo.rxpay.callback;

import androidx.annotation.Nullable;

public interface IPayCallback {
    void success();

    void failed(int code, @Nullable String message);

    void cancel();
}
