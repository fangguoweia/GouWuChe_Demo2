package com.bwei.GouWuChe_Demo2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.bwei.GouWuChe_Demo2.mvp.Api;
import com.bwei.GouWuChe_Demo2.mvp.NewsPresent;
import com.bwei.GouWuChe_Demo2.mvp.NewsView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NewsView,NewsAllCheckboxListener {

    private XRecyclerView xrecycler_view;
    private CheckBox allCheckbox;
    private TextView txt_zongjia;
    private int page=1;//默认第一页
    private NewsPresent newsPresent;
    private NewsAdapter newsAdapter;
    private List<NewsBean.DataBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initView();
        //初始化数据
        initData();
    }
    private void initData() {
        //请求数据
        loadData();
    }
    //请求数据
    private void loadData() {
        HashMap<String,String> params = new HashMap<>();
        params.put("uid","88");
        params.put("page",page+"");
        //传到p层
        newsPresent = new NewsPresent(this);
        newsPresent.getNews(params, Api.GETCARTS);
    }

    private void initView() {
        xrecycler_view = findViewById(R.id.xrecycler_view);
        //txt总价
        txt_zongjia = findViewById(R.id.zongjia);
        //全选按钮
        allCheckbox = findViewById(R.id.allcheckbox);
        //设置布局管理器，线性
        xrecycler_view.setLayoutManager(new LinearLayoutManager(this));
        //设置刷新加载
        xrecycler_view.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //下拉刷新;
                page=1;
                loadData();
                double jisuanzongjia = 0;
                txt_zongjia.setText("总价：￥"+jisuanzongjia);
            }
            @Override
            public void onLoadMore() {
                //上拉加载
                page++;
                loadData();
                double jisuanzongjia = 0;
                txt_zongjia.setText("总价：￥"+jisuanzongjia);
            }
        });
        //设置全选监听01
        allCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allCheckbox.isChecked()) {//
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSelected(true);
                            for (int i1 = 0; i1 < list.get(i).getList().size(); i1++) {
                                list.get(i).getList().get(i1).setSelected(true);
                            }
                        }
                    }
                } else {
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSelected(false);
                            for (int i1 = 0; i1 < list.get(i).getList().size(); i1++) {
                                list.get(i).getList().get(i1).setSelected(false);
                            }
                        }
                    }
                }
                newsAdapter.notifyDataSetChanged();//全部刷新
                //计算总价
                totalPrice();
            }
        });
    }
    //计算总价
    private void totalPrice() {
        double jisuanzongjia = 0;
        for (int i = 0; i < newsAdapter.getNewsList().size(); i++) {
            for (int j=0;j<newsAdapter.getNewsList().get(i).getList().size();j++){
                if (newsAdapter.getNewsList().get(i).getList().get(j).isSelected()){
                    NewsBean.DataBean.ListBean listBean = newsAdapter.getNewsList().get(i).getList().get(j);
                    jisuanzongjia+=listBean.getBargainPrice()*listBean.getJiajianNum();
                }
            }
        }
        txt_zongjia.setText("总价：￥"+jisuanzongjia);
    }
    //请求成功
    @Override
    public void success(NewsBean newsBean) {
        if (newsBean !=null && newsBean.getData() !=null){
            list = newsBean.getData();
            //设置适配器
            newsAdapter = new NewsAdapter(MainActivity.this,list);
            xrecycler_view.setAdapter(newsAdapter);
            xrecycler_view.refreshComplete();//停止刷新
        }else {
            if (newsAdapter!=null){
                newsAdapter.addPageData(newsBean.getData());
            }
            xrecycler_view.loadMoreComplete();//停止加载
        }
        //设置商家按钮的点击
        newsAdapter.setNewsAllCheckboxListener(this);
    }
    //请求失败
    @Override
    public void failure(String msg) {
        Toast.makeText(MainActivity.this,msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置全选监听02
     * 全选按钮是否选中的回调
     */
    @Override
    public void notifyAllCheckboxStatus() {

        StringBuilder stringBuilder = new StringBuilder();
        if (newsAdapter != null) {
            for (int i = 0; i < newsAdapter.getNewsList().size(); i++) {

                stringBuilder.append(newsAdapter.getNewsList().get(i).isSelected());

                for (int i1 = 0; i1 < newsAdapter.getNewsList().get(i).getList().size(); i1++) {

                    stringBuilder.append(newsAdapter.getNewsList().get(i).getList().get(i1).isSelected());
                }
            }
        }
        //Log.i("aaa","stringbuilder=====" + stringBuilder.toString());
        if (stringBuilder.toString().contains("false")) {
            allCheckbox.setChecked(false);
        } else {
            allCheckbox.setChecked(true);
        }
        //取消商家按钮，改变总价
        //计算总价
        totalPrice();
    }
}
