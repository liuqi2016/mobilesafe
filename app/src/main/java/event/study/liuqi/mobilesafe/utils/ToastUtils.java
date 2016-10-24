package event.study.liuqi.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 打印吐絲
 * Created by liuqi on 2016/10/24.
 */
public class ToastUtils {
    /**
     * 打印吐司
     * @param context 上下文
     * @param str 字符串
     */
    public static void show(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
    }
}
