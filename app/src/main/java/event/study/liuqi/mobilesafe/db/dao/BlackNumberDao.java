package event.study.liuqi.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import event.study.liuqi.mobilesafe.db.BlackNumberOpenHelper;
import event.study.liuqi.mobilesafe.db.domain.BlackNumberInfo;

/**
 * 黑名单 接口
 * Created by liuqi on 2016/11/10.
 */

public class BlackNumberDao {

    private final BlackNumberOpenHelper blackNumberOpenHelper;

    //BlackNumberDao单例模式
    //1.私有化构造函数
    private  BlackNumberDao(Context context){
        blackNumberOpenHelper = new BlackNumberOpenHelper(context);
    }
    //2.声明一个当前类的静态变量
    private static BlackNumberDao blackNumberDao =null;
    //3.提供一个静态方法,如果当前类的对象为空，创建一个新的
    public static  BlackNumberDao getInstance(Context context){
     if(blackNumberDao ==null){
         blackNumberDao=new BlackNumberDao(context);
     }
        return blackNumberDao;
    }

    /**
     * 增加一个黑名单
     * @param number 电话号码
     * @param type 模式
     */
    public  void insert (String number,int type){
        //开启数据库，准备写入数据
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number",number);
        values.put("type",type);
        db.insert("blacknumber",null,values);
        db.close();
    }

    /**
     * 删除
     * @param number
     */
    public  void delect (String number){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        db.delete("blacknumber","number = ?",new String[]{number});
        db.close();
    }

    /**
     * 查询所有
     * @return
     */
    public List<BlackNumberInfo>  findAll (){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"number", "type"}, null, null, null, null, "_id desc");
        List<BlackNumberInfo> list = new ArrayList<>();
        while (cursor.moveToNext()){
            BlackNumberInfo bean = new BlackNumberInfo();
            bean.number=cursor.getString(0);
            bean.type=cursor.getInt(1);
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }
    public List<BlackNumberInfo> find(int index){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select number,type from blacknumber order by _id desc limit ?,20; ", new String[]{index+""});
        List<BlackNumberInfo> list = new ArrayList<>();
        while (cursor.moveToNext()){
            BlackNumberInfo bean = new BlackNumberInfo();
            bean.number=cursor.getString(0);
            bean.type=cursor.getInt(1);
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }
    public int count(){
        int count = 0;
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber",null);
        if(cursor.moveToNext()){
            count=cursor.getInt(0);
        }
        cursor.close();
        db.close();
      return count;
    };

    public int getMode(String number) {
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        int mode=0;
        Cursor cursor = db.rawQuery("select type from blacknumber where number = ?",new String[]{number});
        if (cursor.moveToNext()){
             mode = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return mode;
    }
}
