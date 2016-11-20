package event.study.liuqi.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liuqi on 2016/11/10.
 */

public class BlackNumberOpenHelper extends SQLiteOpenHelper {
    public BlackNumberOpenHelper(Context context) {
        super(context, "blacknumber.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        db.execSQL("create table blacknumber (_id integer primary key autoincrement,number varchar(20),type integer)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
