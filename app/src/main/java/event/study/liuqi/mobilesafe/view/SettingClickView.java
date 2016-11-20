package event.study.liuqi.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import event.study.liuqi.mobilesafe.R;

/**
 * Created by liuqi on 2016/10/25.
 */
public class SettingClickView extends RelativeLayout {

    private static final String NAME_SPACE = "http://schemas.android.com/apk/res/event.study.liuqi.mobilesafe";
    private TextView tv_title;
    private TextView tv_parms;
    private String itemtitle;
    private String itemparm;
    private ImageView iv_click;

    public SettingClickView(Context context) {
        this(context,null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml ----> view 将设置界面的一个条目转换成view对象,直接添加到了当前SettingItemView对应的view中
        View.inflate(context, R.layout.setting_click_view,this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_parms = (TextView) findViewById(R.id.tv_parms);
        iv_click = (ImageView) findViewById(R.id.iv_click);
        //获取自定义及原生属性的操作
        initAttr(attrs);
        tv_title.setText(itemtitle);
        tv_parms.setText(itemparm);
    }
    /**
     * 返回属性集合中的自定义属性值
     * @param attrs
     */
    private void initAttr(AttributeSet attrs) {
        itemtitle = attrs.getAttributeValue(NAME_SPACE, "itemtitle");
        itemparm = attrs.getAttributeValue(NAME_SPACE, "itemparm");
    }
    public void setText(String st){
     if(tv_parms!=null) {
         tv_parms.setText(st);
     }
    }
}
