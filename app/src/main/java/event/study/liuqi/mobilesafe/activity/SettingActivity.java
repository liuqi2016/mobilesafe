package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.service.AddressService;
import event.study.liuqi.mobilesafe.service.BlackNumberService;
import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.ServiceUtils;
import event.study.liuqi.mobilesafe.utils.SpUtils;
import event.study.liuqi.mobilesafe.view.SettingClickView;
import event.study.liuqi.mobilesafe.view.SettingItemView;
/**
 * Created by liuqi on 2016/10/25.
 */
public class SettingActivity extends Activity {
    private String tag="SettingActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initUpdate();
        initShowAddress();
        initAddressStyle();
        initAddressPlace();
        initBlackNumber();
    }

    /**
     * 黑名单拦截设置
     */
    private void initBlackNumber() {
        //初始化状态，判断服务是否开启
        String serviceName = "event.study.liuqi.mobilesafe.service.BlackNumberService";
        boolean isserviceopen = ServiceUtils.isServiceRunning(getApplicationContext(), serviceName);
        final SettingItemView black_number = (SettingItemView) findViewById(R.id.black_number);
        black_number.setCheck(isserviceopen);
        black_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischeck = black_number.isCheck();
                black_number.setCheck(!ischeck);
                if(!ischeck){
                    //开启服务
                    Intent intent = new Intent(getApplicationContext(), BlackNumberService.class);
                    startService(intent);
                }else{
                    //关闭服务
                    Intent intent = new Intent(getApplicationContext(), BlackNumberService.class);
                    stopService(intent);
                }
            }
        });
    }

    /**
     * 设置归属地框提示位置
     */
    private void initAddressPlace() {
        SettingClickView address_place = (SettingClickView) findViewById(R.id.address_place);
        address_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddressPlaceActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 切换归属地显示风格
     */
    private void initAddressStyle() {
        final SettingClickView address_style = (SettingClickView) findViewById(R.id.address_style);
        //样式数组
        final String [] styles= new String[]{
                "透明",
                "橙色",
                "蓝色",
                "灰色",
                "绿色",
        };
        int initstyle = SpUtils.getInt(getApplicationContext(), ConstansValue.ADDRESSSTYLE, 0);
        address_style.setText(styles[initstyle]);
        //点击事件
        address_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取初始化的样式
                //弹出对话框
                int newstyle = SpUtils.getInt(getApplicationContext(), ConstansValue.ADDRESSSTYLE, 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("请选择归属地样式");
                builder.setIcon(R.drawable.ic_launcher);
                builder.setSingleChoiceItems(styles, newstyle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    //单击选项
                        SpUtils.putInt(getApplicationContext(),ConstansValue.ADDRESSSTYLE,which);
                        address_style.setText(styles[which]);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    /**
     * 版本更新开关
     */
    private void initUpdate() {
        final SettingItemView update_set = (SettingItemView) findViewById(R.id.update_set);
        //获取sp中open_update的状态
         Boolean open_update = SpUtils.getboolean(this, ConstansValue.OPEN_UPDATE, false);
        //设置内容
        update_set.setCheck(open_update);
        update_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果之前是选中的，点击后变成未选中
                //如果之前是未选中，点击后变成选中
                //获取之前选中的状态
                boolean ischeck = update_set.isCheck();
                //取反
                update_set.setCheck(!ischeck);
                //保存数据到sp中,取反后保存
                SpUtils.putboolean(SettingActivity.this,ConstansValue.OPEN_UPDATE,!ischeck);
            }
        });
    }

    /**
     * 电话归属地显示
     */
    private void initShowAddress() {
        String serviceName = "event.study.liuqi.mobilesafe.service.AddressService";
        boolean serviceRunning = ServiceUtils.isServiceRunning(getApplicationContext(), serviceName);
        Log.i(tag,"serviceRunning:"+serviceRunning);
        final SettingItemView show_address = (SettingItemView) findViewById(R.id.show_address);
         if(serviceRunning){
                show_address.setCheck(true);
            }else{
                show_address.setCheck(false);
            }
        show_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取点击前的状态
                boolean ischeck = show_address.isCheck();
                show_address.setCheck(!ischeck);
                if(!ischeck){
                    //开启服务
                    startService(new Intent(getApplicationContext(),AddressService.class));
                }else{
                    //关闭服务
                    stopService(new Intent(getApplicationContext(),AddressService.class));
                }
            }
        });
    }

}
