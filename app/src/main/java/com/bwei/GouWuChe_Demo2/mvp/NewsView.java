package com.bwei.GouWuChe_Demo2.mvp;

/**
 * view视图层
 */
public interface NewsView {

    void success(NewsBean newsBean);//请求成功
    void failure(String msg);//请求失败
}
