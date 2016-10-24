package event.study.liuqi.mobilesafe.utils;
import java.io.IOException;
import java.io.InputStream;

/**
 * 将流转换成字符串(工具类封装)
 * Created by liuqi on 2016/10/23.
 */
public class StreamUtil {
    /**
     * 流转换成字符串
     * @param is 流
     * @return 字符串
     */
    public static String streamToString(InputStream is) {
        //1.读取的过程中,将读取的内容储存在缓存中，然后一次性转换成字符串
        StringBuffer sb = new StringBuffer();
        //2.读流操作，读到没有为止
        byte[] bytes = new byte[1024];
        //3.记录读取内容的临时变量
        try {
            for (int n ;(n =is.read(bytes)) !=-1;){
                sb.append(new String(bytes,0,n));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
