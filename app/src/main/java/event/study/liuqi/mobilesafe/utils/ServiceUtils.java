package event.study.liuqi.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 服务状态工具类
 * Created by liuqi on 2016/11/4.
 */

public class ServiceUtils {

    public static boolean isServiceRunning(Context context, String serviceName) {
        //获取服务开启的状态
        ActivityManager aM = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = aM.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo runningService : runningServices) {
            //判断服务是否存在
            if (runningService.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}