package com.bwei.GouWuChe_Demo2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 房国伟 on 2018/8/24.
 */
public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.News2ViewHolder> {

    private Context context;
    private List<NewsBean.DataBean.ListBean> listBeanlist;
    private NewsCheckListener checkListener;//接口回调引用
    private NewsAllCheckboxListener newsAllCheckboxListener;

    public NewsItemAdapter(Context context, List<NewsBean.DataBean.ListBean> listBeanlist) {
        this.context = context;
        this.listBeanlist = listBeanlist;
    }
    //暴露给调用者去进行回调：对checklisener进行初始化
    public void setCheckListener(NewsCheckListener newsCheckListener){
        this.checkListener=newsCheckListener;
    }
    public void setNewsAllCheckboxListener(NewsAllCheckboxListener newsAllCheckboxListener){
        this.newsAllCheckboxListener=newsAllCheckboxListener;
    }

    @NonNull
    @Override
    public News2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.news_zi_item_layout, parent, false);
        News2ViewHolder viewHolder = new News2ViewHolder(itemview);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final News2ViewHolder holder, int position) {
        final NewsBean.DataBean.ListBean bean = listBeanlist.get(position);
        holder.pricetxt.setText("优惠价:$"+bean.getBargainPrice());
        holder.titletxt.setText(bean.getTitle());
        String[] imges = bean.getImages().split("\\|");
        //判断数组大小，防止空指针
        if (imges!=null && imges.length>0){
            Glide.with(context).load(imges[0]).into(holder.ziimageview);
        }else {
            holder.ziimageview.setImageResource(R.mipmap.ic_launcher);
        }
        holder.zicheckbox.setChecked(bean.isSelected());
        holder.ButtonView.setNumEt(bean.getJiajianNum());
        //设置子条目按钮改变商家按钮
        holder.zicheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.zicheckbox.isChecked()){
                    bean.setSelected(true);
                }else {
                    bean.setSelected(false);
                }
                if (checkListener!=null){
                    checkListener.notifyParent();//通知一级列表适配器刷新
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBeanlist.size() == 0 ? 0 : listBeanlist.size();
    }

    public class News2ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ziimageview;
        private CheckBox zicheckbox;
        private TextView titletxt;
        private TextView pricetxt;
        private ButtonView ButtonView;

        public News2ViewHolder(View itemView) {
            super(itemView);
            zicheckbox = itemView.findViewById(R.id.zicheckbox);
            titletxt = itemView.findViewById(R.id.titletxt);
            pricetxt = itemView.findViewById(R.id.pricetxt);
            ziimageview = itemView.findViewById(R.id.ziimageview);
            ButtonView = itemView.findViewById(R.id.jiajianqi);
        }
    }
}
