package event.study.liuqi.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 读写sharepren
 * Created by liuqi on 2016/10/26.
 */

public class SpUtils {

    private  static SharedPreferences sp;
    /**
     * 写入boolean变量到sp中
     * @param context 上下文环境
     * @param key
     * @param value
     */
    public static void putboolean(Context context,String key,Boolean value){
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 读取sp中的boolean变量
     * @param context 上下文环境
     * @param key
     * @return boolean值
     */
    public  static Boolean getboolean(Context context,String key,Boolean defVaule){
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return  sp.getBoolean(key,defVaule);
    }
    /**
     * 写入String变量到sp中
     * @param context 上下文环境
     * @param key
     * @param value
     */
    public static void putString(Context context,String key,String value){
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }
    /**
     * 读取sp中的String变量
     * @param context 上下文环境
     * @param key
     * @return boolean值
     */
    public  static String getString(Context context,String key,String defVaule){
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return  sp.getString(key,defVaule);
    }

    public static void remove(Context context, String simnumber) {
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(simnumber).commit();
    }
}
