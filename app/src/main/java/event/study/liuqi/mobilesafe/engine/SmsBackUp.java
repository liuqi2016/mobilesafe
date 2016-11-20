package event.study.liuqi.mobilesafe.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liuqi on 2016/11/16.
 */
public class SmsBackUp {
    private static int index = 0;
    private static FileOutputStream of;

    public static void smsback(Context context,String path,CallBack callback) {
        try {
        //1.获取需要写入的文件
        File file = new File(path);
        //2.文件相应的输出流
            of = new FileOutputStream(file,true);
            //3.查询出所有的短信
            ContentResolver resolver = context.getContentResolver();
            Cursor cursor = resolver.query(Uri.parse("content://sms/"), new String[]{"address", "date", "type", "read", "body"}, null, null, "date desc");
            //4.对比xml中的备份短信
            int count = cursor.getCount();
            int index =0;
            if(callback!=null){
                callback.setMax(cursor.getCount());
            }
            //xml准备
            //1.序列化数据库中获取的数据，放到xml中去
            XmlSerializer xmlSerializer = Xml.newSerializer();
            //2.写入设置
                xmlSerializer.setOutput(of,"utf-8");
            //3.xml规范
            xmlSerializer.startDocument("utf-8",true);
            xmlSerializer.startTag(null,"smss");
            while(cursor.moveToNext()){
                xmlSerializer.startTag(null,"sms");
                xmlSerializer.startTag(null,"address");
                String address = cursor.getString(0);
                xmlSerializer.text(address);
                xmlSerializer.endTag(null,"address");
                xmlSerializer.startTag(null,"date");
                int date = cursor.getInt(1);
                xmlSerializer.text(date+"");
                xmlSerializer.endTag(null,"date");
                xmlSerializer.startTag(null,"type");
                int type = cursor.getInt(2);
                xmlSerializer.text(type+"");
                xmlSerializer.endTag(null,"type");
                xmlSerializer.startTag(null,"read");
                int read = cursor.getInt(3);
                xmlSerializer.text(read+"");
                xmlSerializer.endTag(null,"read");
                xmlSerializer.startTag(null,"body");
                String body = cursor.getString(4);
                xmlSerializer.text(body);
                xmlSerializer.endTag(null,"body");
//                    Log.i("备份短信",address+date+type+read+body);
                xmlSerializer.endTag(null,"sms");
                index++;
                    Thread.sleep(500);
                if(callback!=null){
                    callback.setProgress(index);
                }
            }
            xmlSerializer.endTag(null,"smss");
            xmlSerializer.endDocument();
            //2.保存到xml文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public interface CallBack{
        public void setMax(int max);
        public void setProgress(int index);
    }
}
