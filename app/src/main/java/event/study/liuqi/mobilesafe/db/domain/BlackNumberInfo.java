package event.study.liuqi.mobilesafe.db.domain;

import android.content.Intent;

/**
 * Created by liuqi on 2016/11/10.
 */
public class BlackNumberInfo {
    public String number ;
    public int type;
    public String getnumber() {
        return number;
    }
    public void setnumber(String number) {
        this.number = number;
    }
    public int gettype() {
        return type;
    }
    public void settype(int type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return "BlackNumberInfo{" +
                "number='" + number + '\'' +
                ", type=" + type +
                '}';
    }
}
