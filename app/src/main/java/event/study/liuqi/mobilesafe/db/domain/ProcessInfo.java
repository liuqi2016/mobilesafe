package event.study.liuqi.mobilesafe.db.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by liuqi on 2016/11/19.
 */
public class ProcessInfo {
    public String name;//名称
    public String packageName;//包名
    public Drawable icon;//图标
    public long memSize;//应用已使用的内存数
    public boolean isCheck;//是否被选中
    public boolean isSystem;//是否为系统应用

    public long getMemSize() {
        return memSize;
    }

    public void setMemSize(long memSize) {
        this.memSize = memSize;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
