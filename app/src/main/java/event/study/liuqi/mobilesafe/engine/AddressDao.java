package event.study.liuqi.mobilesafe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static event.study.liuqi.mobilesafe.R.drawable.phone;

/**
 * Created by liuqi on 2016/11/3.
 */

public class AddressDao {
    private static SQLiteDatabase db;
    static String PATH = "/data/data/event.study.liuqi.mobilesafe/files/address.db";
    private static String location;

    public static String getAddress(String phone) {
        //判断手机号码
        String regularExpression = "^1[3-8]\\d{9}";
        db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
        if (phone.matches(regularExpression)) {
            Cursor cursor = db.rawQuery("select location from data2 where id =(select outkey from data1 where id=?)", new String[]{phone.substring(0,7)});
            if (cursor.moveToNext()) {
                location = cursor.getString(0);
//                    Log.i(tag,"localtion:"+location);
            }else{
                location="未知号码";
            }
            cursor.close();
        } else {
            int length = phone.length();
            switch (length) {
                case 3: //119 110 120 114
                    location = "报警电话";
                case 4://119 110 120 114
                    location = "模拟器";
                    break;
                case 5://10086 99555
                    location = "服务电话";
                    break;
                case 7:
                    location = "本地电话";
                    break;
                case 8:
                    location = "本地电话";
                    break;
                case 11:
                    //(3+8) 区号+座机号码(外地),查询data2
                    String area = phone.substring(1, 3);
                    Cursor cursor = db.query("data2", new String[]{"location"}, "area = ?", new String[]{area}, null, null, null);
                    if (cursor.moveToNext()) {
                        location = cursor.getString(0);
                    } else {
                        location = "未知号码";
                    }
                    break;
                case 12:
                    //(4+8) 区号(0791(江西南昌))+座机号码(外地),查询data2
                    String area1 = phone.substring(1, 4);
                    Cursor cursor1 = db.query("data2", new String[]{"location"}, "area = ?", new String[]{area1}, null, null, null);
                    if (cursor1.moveToNext()) {
                        location = cursor1.getString(0);
                    } else {
                        location = "未知号码";
                    }
                    break;
                default:
                    location = "未知号码";
                    break;
            }
            }
        db.close();
        return location;
    }

}
