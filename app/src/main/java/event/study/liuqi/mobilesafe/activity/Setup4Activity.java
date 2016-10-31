package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.SpUtils;
import event.study.liuqi.mobilesafe.utils.ToastUtils;

/**
 * Created by liuqi on 2016/10/29.
 */
public class Setup4Activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initUi();
    }

    /**
     * 初始化Ui控件
     */
    private void initUi() {
        Boolean safeon = SpUtils.getboolean(getApplicationContext(), ConstansValue.SAFEON, false);
        final CheckBox cb_over = (CheckBox) findViewById(R.id.cb_over);
        final TextView tv_safe_on = (TextView) findViewById(R.id.tv_safe_on);
        if (safeon){
         cb_over.setChecked(true);
            tv_safe_on.setText("防盗保护已开启");
        }else{
            cb_over.setChecked(false);
            tv_safe_on.setText("防盗保护已关闭");
        }
        //点击事件
       cb_over.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               //b为点击后的状态
                   SpUtils.putboolean(getApplicationContext(),ConstansValue.SAFEON,b);
                   cb_over.setChecked(b);
               if(b){
                   tv_safe_on.setText("防盗保护已开启");
               }else{
                   tv_safe_on.setText("防盗保护已关闭");
               }
           }
       });

    }

    /**
     * 上一页
     */
    public void prePage(View v){
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
    }
    /**
     * 下一页
     */
    public void nextPage(View v){
        //1.获取开启状态
        Boolean safeon = SpUtils.getboolean(this, ConstansValue.SAFEON, false);
        if(safeon) {
            Intent intent = new Intent(this, SetupOverActivity.class);
            startActivity(intent);
            finish();
            SpUtils.putboolean(this,ConstansValue.SETUPOVER,true);
        }else{
            ToastUtils.show(getApplicationContext(),"请开启防盗保护");
        }
    }
}
