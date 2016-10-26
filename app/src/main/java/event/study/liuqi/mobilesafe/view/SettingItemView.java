package event.study.liuqi.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import event.study.liuqi.mobilesafe.R;

/**
 * Created by liuqi on 2016/10/25.
 */
public class SettingItemView extends RelativeLayout {

    private TextView tv_title;
    private TextView tv_des;
    private CheckBox cb_box;
    private String mDeson;
    private String mDesoff;

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml ----> view 将设置界面的一个条目转换成view对象,直接添加到了当前SettingItemView对应的view中
        View.inflate(context, R.layout.setting_item_view,this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);
    }

    /**
     * 判断是否开启的方法
     * @return 当前的选中状态
     */
    public boolean isCheck(){
        return cb_box.isChecked();
    }
    public void setCheck(boolean isCheck){
        //当前条目在选择的过程中，cb_box选中状态也在跟随isCheck变化
        cb_box.setChecked(isCheck);
        if(isCheck){
            //开启
            tv_des.setText("自动更新已开启");
        }else{
            //关闭
            tv_des.setText("自动更新已关闭");
        }
    }
}
