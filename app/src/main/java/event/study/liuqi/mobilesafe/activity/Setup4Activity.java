package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.receiver.AdminReceiver;
import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.SpUtils;
import event.study.liuqi.mobilesafe.utils.ToastUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by liuqi on 2016/10/29.
 */
public class Setup4Activity extends BaseSetupActivity{

    private ComponentName componentName;
    private String tag ="Setup4Activity";
    private TextView tv_safe_on;
    private DevicePolicyManager mDPM;
    private CheckBox cb_over;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        //设备管理者
        componentName = new ComponentName(this, AdminReceiver.class);
        mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        initUi();
    }

    @Override
    public void showNextPage() {
        //1.获取开启状态
        Boolean safeon = SpUtils.getboolean(this, ConstansValue.SAFEON, false);
        if(safeon) {
            Intent intent = new Intent(this, SetupOverActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
            SpUtils.putboolean(this,ConstansValue.SETUPOVER,true);
        }else{
            ToastUtils.show(getApplicationContext(),"请开启防盗保护");
        }
    }

    @Override
    public void showPrePage() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    /**
     * 初始化Ui控件
     */
    private void initUi() {
        Boolean safeon = SpUtils.getboolean(getApplicationContext(), ConstansValue.SAFEON, false);
        cb_over = (CheckBox) findViewById(R.id.cb_over);
        if(mDPM.isAdminActive(componentName)){
            //禁止chekBox
            cb_over.setClickable(false);
        };
        tv_safe_on = (TextView) findViewById(R.id.tv_safe_on);
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
                   //开启设备root权限
                   Intent intent2 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                   intent2.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                   intent2.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "超级管理员");
                   startActivityForResult(intent2,1234);
           }
       });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //-1是激活，0是取消激活
        if(resultCode==-1){
            cb_over.setChecked(true);
            tv_safe_on.setText("防盗保护已开启");
            SpUtils.putboolean(getApplicationContext(),ConstansValue.SAFEON,true);
        }else{
            cb_over.setChecked(false);
            tv_safe_on.setText("防盗保护已关闭");
            SpUtils.putboolean(getApplicationContext(),ConstansValue.SAFEON,false);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
