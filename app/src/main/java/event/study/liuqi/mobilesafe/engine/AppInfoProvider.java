package event.study.liuqi.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import event.study.liuqi.mobilesafe.db.domain.AppInfo;

/**
 * Created by liuqi on 2016/11/17.
 */

public class AppInfoProvider {
    /**
     * 获取已安装应用的信息
     * @param context
     * @return
     */
    public static ArrayList<AppInfo> getAppInfos(Context context){
        ArrayList<AppInfo> listinfo = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        //获取所有已安装的包
        List<ApplicationInfo> packages = pm.getInstalledApplications(0);
        for(ApplicationInfo applicationInfo : packages){
            AppInfo info = new AppInfo();
            //获取包名
            String packageName = applicationInfo.packageName;
            //获取应用图标
            Drawable icon = applicationInfo.loadIcon(pm);
            //获取名称
            String name = (String) applicationInfo.loadLabel(pm);
            //判断应用类型
            if((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) == applicationInfo.FLAG_SYSTEM ){
                info.isUserApp =false;
            }else{
                info.isUserApp=true;
            }
            //判断应用的位置
            //8,是否为sd卡中安装应用
            if((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)==ApplicationInfo.FLAG_EXTERNAL_STORAGE){
                //sd卡应用
                info.isRom = true;
            }else{
                //非sd卡应用
                info.isRom = false;
            }
            info.name=name;
            info.icon=icon;
            info.packgename=packageName;
            listinfo.add(info);
        }
        return listinfo;
    }
}
