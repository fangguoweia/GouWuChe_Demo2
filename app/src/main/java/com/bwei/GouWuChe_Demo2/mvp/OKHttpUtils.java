package com.bwei.GouWuChe_Demo2.mvp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络工具类
 */
public class OKHttpUtils {
    private static OKHttpUtils okHttpUtils;
    private OkHttpClient okHttpClient;

    //构建私有方法
    private OKHttpUtils(){
        okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS)
                .connectTimeout(5,TimeUnit.SECONDS)
                .build();
    }

    //暴露给调用者,进行双重检验
    public static OKHttpUtils getInstance(){
        if (okHttpUtils ==null){
            synchronized (OKHttpUtils.class){
                if (okHttpUtils ==null){
                    okHttpUtils =new OKHttpUtils();
                }
            }
        }
        return okHttpUtils;
    }

    //get封装
    public void getData(String url, HashMap<String, String> params, final RequestCallback requestCallback) {

        StringBuilder urlsb = new StringBuilder();
        String allUrl = "";
        for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
            urlsb.append("?").append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue()).append("&");
        }

        allUrl = url + urlsb.toString().substring(0, urlsb.length() - 1);
        System.out.println("url:" + allUrl);

        final Request request = new Request.Builder()
                .url(allUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (requestCallback != null) {
                    requestCallback.failure(call, e);
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (requestCallback != null) {
                    requestCallback.onResponse(call, response);
                }
            }
        });
    }

    //封装post
    public void postData(String url, HashMap<String,String> params,final RequestCallback requestCallback){
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        if (params!=null&&params.size()>0){
            for (Map.Entry<String,String> stringStringEntry : params.entrySet()){
                formBodyBuilder.add(stringStringEntry.getKey(),
                        stringStringEntry.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url).post(formBodyBuilder.build()).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            //请求失败
            @Override
            public void onFailure(Call call, IOException e) {
                if (requestCallback!=null){
                    requestCallback.failure(call,e);
                }
            }
            //请求成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (requestCallback !=null){
                    requestCallback.onResponse(call,response);
                }
            }
        });
    }
}
