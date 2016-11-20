package event.study.liuqi.mobilesafe.db.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by liuqi on 2016/11/17.
 */

public class AppInfo {
    public String name;//名称
    public String packgename;//包名
    public Drawable icon;//图标
    public boolean isUserApp;//是否是用户程序

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", packgename='" + packgename + '\'' +
                ", icon=" + icon +
                ", isUserApp=" + isUserApp +
                ", isRom=" + isRom +
                '}';
    }

    public boolean isRom;//是否在内置储存器中

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackgename() {
        return packgename;
    }

    public void setPackgename(String packgename) {
        this.packgename = packgename;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isUserApp() {
        return isUserApp;
    }

    public void setUserApp(boolean userApp) {
        isUserApp = userApp;
    }

    public boolean isRom() {
        return isRom;
    }

    public void setRom(boolean rom) {
        isRom = rom;
    }
}
