package com.bwei.GouWuChe_Demo2.mvp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 接口回调
 */
public interface RequestCallback {

    void failure(Call call, IOException e);//请求失败
    void onResponse(Call call, Response response);//请求成功

}
