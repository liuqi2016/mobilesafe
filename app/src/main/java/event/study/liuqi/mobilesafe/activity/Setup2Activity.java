package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.SpUtils;
import event.study.liuqi.mobilesafe.utils.ToastUtils;
import event.study.liuqi.mobilesafe.view.SettingItemView;

/**
 * Created by liuqi on 2016/10/29.
 */
public class Setup2Activity extends BaseSetupActivity{

    private SettingItemView bound_sim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUi();
    }
    @Override
    public void showNextPage() {
        //判断是否绑定sim卡
        String simnumber = SpUtils.getString(getApplicationContext(), ConstansValue.SIMNUMBER, "");
        if(!simnumber.isEmpty()) {
            Intent intent = new Intent(this, Setup3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else{
            ToastUtils.show(getApplicationContext(),"请先绑定sim卡");
        }
    }
    @Override
    public void showPrePage() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }
    /**
     * 初始化ui
     */
    private void initUi() {
        bound_sim = (SettingItemView) findViewById(R.id.bound_sim);
        //0.获取sim卡的绑定状态
        String simnumber = SpUtils.getString(getApplicationContext(), ConstansValue.SIMNUMBER, "");
        if(simnumber.isEmpty()){
            bound_sim.setCheck(false);
        }else{
            bound_sim.setCheck(true);
        }
        bound_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1.获取上次点击状态
                boolean isCheck = bound_sim.isCheck();
                bound_sim.setCheck(!isCheck);
                //2.根据点击状态处理业务逻辑
                if(isCheck){
                    //取消绑定
                    SpUtils.remove(getApplicationContext(),ConstansValue.SIMNUMBER);
                }else{
                    //绑定sim卡
                    TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simnumber = manager.getSimSerialNumber();
                    SpUtils.putString(getApplicationContext(),ConstansValue.SIMNUMBER,simnumber);
                }
            }
        });
    }
 }
