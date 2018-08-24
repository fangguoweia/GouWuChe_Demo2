package com.bwei.GouWuChe_Demo2.mvp;

import com.bwei.GouWuChe_Demo2.NewsBean;

import java.util.HashMap;

/**
 * p层
 * 逻辑处理层
 */
public class NewsPresent {

    private NewsModel newsModel;
    private NewsView newsView;

    public NewsPresent(NewsView newsView) {
        this.newsModel = new NewsModel();
        attachView(newsView);
    }
    //把view层绑定到p层
    private void attachView(NewsView newsView) {
        this.newsView=newsView;
    }

    public void getNews(HashMap<String,String> params,String url){
        newsModel.getNews(params, url, new NewsModel.NewsCallback() {
            @Override
            public void success(NewsBean newsBean) {
               //成功
                if (newsView!=null){
                    newsView.success(newsBean);
                }
            }

            @Override
            public void fail(String msg) {
                //失败
                newsView.failure(msg);
            }
        });
    }
    //解除绑定
    public void datachView(){
        this.newsView=null;
    }
}
