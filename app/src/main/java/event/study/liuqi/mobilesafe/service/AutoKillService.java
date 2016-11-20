package event.study.liuqi.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

/**
 * Created by liuqi on 2016/11/20.
 */
public class AutoKillService extends Service{


    private InnerScreenOffReceiver innerScreenOffReceiver;

    @Override
    public void onCreate() {
//        Log.i("AutoKillService","开启服务");
        //监听屏幕关闭的广播, 注意,该广播只能在代码中注册,不能在清单文件中注册
        innerScreenOffReceiver = new InnerScreenOffReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(innerScreenOffReceiver,intentFilter);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i("AutoKillService","关闭服务");
        unregisterReceiver(innerScreenOffReceiver);
        super.onDestroy();
    }

    /**
     * 锁屏关闭广播接收者
     */
    private class InnerScreenOffReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("屏幕关闭...");
            // 杀死后台所有运行的进程
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                // 跳过手机卫士的服务
                if (runningAppProcessInfo.processName.equals(context.getPackageName())) {
                    continue;
                }
                am.killBackgroundProcesses(runningAppProcessInfo.processName);
            }
        }

    }
        }
