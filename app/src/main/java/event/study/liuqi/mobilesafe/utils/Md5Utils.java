package event.study.liuqi.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liuqi on 2016/10/28.
 */

public class Md5Utils {

    private static StringBuffer sb;

    public static String encoder(String psd){
        try {
            //1.指定加密算法,加盐处理
            psd = psd +"mobilesafe";
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //2.将加密算法的字符串转换成byte类型的数组,然后进行随机哈希过程
            byte[] bs =digest.digest(psd.getBytes());
            //3.遍历循环bs，让其生成32位字符串，固定写法
            //4.拼接字符串过程
            sb = new StringBuffer();
            for(byte b : bs){
                int i =b & 0xff;
                //int 类型的i 需要转换成16机制字符
                String hexString = Integer.toHexString(i);
                if(hexString.length()<2){
                    hexString="0"+hexString;
                }
                sb.append(hexString);

            }

            //5.打印测试
//            System.out.println(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return  sb.toString();
    }
}
