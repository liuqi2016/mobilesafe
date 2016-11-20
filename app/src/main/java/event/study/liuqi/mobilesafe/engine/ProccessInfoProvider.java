package event.study.liuqi.mobilesafe.engine;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import java.util.ArrayList;
import java.util.List;

import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.db.domain.ProcessInfo;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by liuqi on 2016/11/19.
 */
public class ProccessInfoProvider {
    /**
     * 获取总共的进程数
     *
     * @param context
     * @return
     */
    public static int getProccessCount(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        return runningAppProcesses.size();
    }

    /**
     * 获取可用内存大小
     * @param context
     * @return
     */
    public static long getAvailSpace(Context context) {
        //1.获取ActivityManager对象
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        //2.构建储存可用的内存对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //3.给内存对象赋值
        am.getMemoryInfo(memoryInfo);
        //4.获取可用大小
        return memoryInfo.availMem;
    }

    /**
     * 获取总共的内存大小
     * @param context
     * @return
     */
    public static  long getAllSpace(Context context){
        //1.获取ActivityManager对象
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        //2.构建储存可用的内存对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //3.给内存对象赋值
        am.getMemoryInfo(memoryInfo);
        //4.获取可用大小
        return memoryInfo.totalMem;
    }
    public static List<ProcessInfo> getProccessInfos(Context context){
        //1.获取activityManager对象
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        //2.获取正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        //3.遍历循环
         List<ProcessInfo> lists =new ArrayList<>();
        for(ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses){
            ProcessInfo info = new ProcessInfo();
            info.packageName = processInfo.processName;//进程名称==包名
            int pid = processInfo.pid;
            //获取进程大小
            Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{pid});
            Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
            info.memSize  = memoryInfo.getTotalPrivateDirty() * 1024;
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(processInfo.processName,0);
                info.icon=applicationInfo.loadIcon(pm);
                info.name= (String) applicationInfo.loadLabel(pm);
                //判断是否为系统进程
                if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
                    info.isSystem=true;
                }else{
                    info.isSystem=false;
                }
            } catch (PackageManager.NameNotFoundException e) {
                //需要处理
                info.name = processInfo.processName;
                info.icon = context.getResources().getDrawable(R.drawable.ic_launcher);
                info.isSystem = true;
                info.isCheck=false;
                e.printStackTrace();
            }
            lists.add(info);
        }
        return lists;
    }

    /**
     * 杀死进程
     * @param context
     * @param processInfo
     */
    public static void killproccess(Context context, ProcessInfo processInfo) {
        //1.获取activitymanage
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        //2.杀死指定包名的进程
        am.killBackgroundProcesses(processInfo.getPackageName());
    }
}
