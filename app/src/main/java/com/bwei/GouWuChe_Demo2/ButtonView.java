package com.bwei.GouWuChe_Demo2;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 房国伟 on 2018/8/24.
 */
public class ButtonView extends LinearLayout {
    private TextView jiaTv,jiantv;
    private EditText numEt;
    private int num = 1;
    public ButtonView(Context context) {
        this(context,null);
    }

    public ButtonView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化自定义的布局
     */
    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.jia_jian_layout,this,true);
//        addView(view);
        jiantv = view.findViewById(R.id.jian);
        jiaTv = view.findViewById(R.id.jia);
        numEt = view.findViewById(R.id.num);

        numEt.setText(num+"");

        jiaTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                numEt.setText(num+"");
                if (jiaJianListener!=null){
                    jiaJianListener.getNum(num);
                }
            }
        });
        //设置最大值最小值
        jiantv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                num--;
                if (num<=0){
                    Toast.makeText(getContext(), "数量不能小于1", Toast.LENGTH_SHORT).show();
                    num = 1;
                }

                numEt.setText(num+"");

                if (jiaJianListener!=null){
                    jiaJianListener.getNum(num);
                }

            }
        });

    }

    /**
     * 设置editext数量
     * @param
     */
    public void setNumEt(int n) {
        numEt.setText(n+"");
        num = Integer.parseInt(numEt.getText().toString());
    }

    private JiaJianListener jiaJianListener;

    public void setJiaJianListener(JiaJianListener jiaJianListener) {
        this.jiaJianListener = jiaJianListener;
    }

    public interface JiaJianListener{
        void getNum(int num);
    }

}
