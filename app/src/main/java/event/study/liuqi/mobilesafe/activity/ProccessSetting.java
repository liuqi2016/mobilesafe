package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.service.AutoKillService;
import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.ServiceUtils;
import event.study.liuqi.mobilesafe.utils.SpUtils;

/**
 * Created by liuqi on 2016/11/20.
 */
public class ProccessSetting extends Activity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox cb_screen_off_clean;
    private CheckBox cb_show_system_proccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proccess_setting);
        initUI();
    }

    private void initUI() {
        cb_show_system_proccess = (CheckBox) findViewById(R.id.cb_show_system_proccess);
        //注册事件
        cb_show_system_proccess.setOnCheckedChangeListener(this);
        cb_screen_off_clean = (CheckBox) findViewById(R.id.cb_screen_off_clean);
        cb_screen_off_clean.setOnCheckedChangeListener(this);
        Boolean isshow = SpUtils.getboolean(getApplicationContext(), ConstansValue.SHOWSYSTEMPROCCESS, false);
        //回显
        cb_show_system_proccess.setChecked(isshow);
        if (isshow) {
            cb_show_system_proccess.setText("显示系统进程");
        } else {
            cb_show_system_proccess.setText("不显示系统进程");
        }
        boolean serviceRunning = ServiceUtils.isServiceRunning(getApplicationContext(), "event.study.liuqi.mobilesafe.service.AutoKillService");
        cb_screen_off_clean.setChecked(serviceRunning);
        if(serviceRunning){
            cb_screen_off_clean.setText("锁屏清理已开启");
        }else{
            cb_screen_off_clean.setText("锁屏清理已关闭");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_show_system_proccess:
                //记录状态
                SpUtils.putboolean(getApplicationContext(), ConstansValue.SHOWSYSTEMPROCCESS, isChecked);
                if (isChecked) {
                    cb_show_system_proccess.setText("显示系统进程");
                } else {
                    cb_show_system_proccess.setText("不显示系统进程");
                }
                break;
            case R.id.cb_screen_off_clean:
                Intent intent = new Intent(ProccessSetting.this, AutoKillService.class);
                if (isChecked) {
                    //开启锁屏清理服务
                    startService(intent);
                    cb_screen_off_clean.setText("锁屏清理已开启");
                } else {
                    //关闭锁屏清理服务
                    stopService(intent);
                    cb_screen_off_clean.setText("锁屏清理已关闭");
                    break;
                }
        }
    }
}
