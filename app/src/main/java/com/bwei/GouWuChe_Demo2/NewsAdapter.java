package com.bwei.GouWuChe_Demo2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 房国伟 on 2018/8/24.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> implements NewsCheckListener {

    private Context context;
    private List<NewsBean.DataBean> list;
    private NewsAllCheckboxListener allCheckboxListener;

    public NewsAdapter(Context context, List<NewsBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.news_fu_item_layout, parent, false);
        NewsViewHolder viewHolder = new NewsViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, int position) {
        //获取每个属性
        final NewsBean.DataBean bean = list.get(position);
        holder.shangjianame.setText(bean.getSellerName());
        //全选按钮
        holder.shangjiabox.setChecked(bean.isSelected());
        //设置子布局
        holder.fuitemRecy.setLayoutManager(new LinearLayoutManager(context));
        NewsItemAdapter newsItemAdapter = new NewsItemAdapter(context,bean.getList());
        holder.fuitemRecy.setAdapter(newsItemAdapter);
        //设置子条目点击事件01
        newsItemAdapter.setCheckListener(this);
        //点击商家下的子条目按钮，改变商家按钮的选中状态02 完结
        for (int i = 0; i < bean.getList().size(); i++) {
            if (!bean.getList().get(i).isSelected()){
                holder.shangjiabox.setChecked(false);
                break;//必须跳出循环
            }else {
                holder.shangjiabox.setChecked(true);
            }
        }
        //商家按钮-3-设置商家的checkbox点击事件，完结
        holder.shangjiabox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断商家按钮是否选中，
                if (holder.shangjiabox.isChecked()){
                    //如果是，
                    bean.setSelected(true);
                    //则该商家下所有子条目都选中。
                    for (int i=0; i<bean.getList().size(); i++){
                        bean.getList().get(i).setSelected(true);
                    }
                }else {
                    //如果不是，
                    bean.setSelected(false);
                    //则该商家下所有子条目都为不选中。
                    for (int i=0; i<bean.getList().size(); i++){
                        bean.getList().get(i).setSelected(false);
                    }
                }
                notifyDataSetChanged();
                if (allCheckboxListener!=null){
                    allCheckboxListener.notifyAllCheckboxStatus();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 0 : list.size();
    }
    //上拉加载
    public void addPageData(List<NewsBean.DataBean> data) {
        if (list!=null){
            list.addAll(list);
            notifyDataSetChanged();
        }
    }
    //商家按钮-1-暴露给购物车页面，进行回调
    public void setNewsAllCheckboxListener(NewsAllCheckboxListener newsAllCheckboxListener){
        allCheckboxListener=newsAllCheckboxListener;
    }
    //商家按钮-2-刷新NewsAdapter适配器的回调
    @Override
    public void notifyParent() {
        notifyDataSetChanged();
        if (allCheckboxListener !=null){
            allCheckboxListener.notifyAllCheckboxStatus();
        }
    }

    public List<NewsBean.DataBean> getNewsList() {
        return list;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        private CheckBox shangjiabox;
        private TextView shangjianame;
        private RecyclerView fuitemRecy;

        public NewsViewHolder(View itemView) {
            super(itemView);
            shangjiabox = itemView.findViewById(R.id.shangjiabox);
            shangjianame = itemView.findViewById(R.id.shangjianame);
            fuitemRecy = itemView.findViewById(R.id.fuitemRecy);
        }
    }
}
