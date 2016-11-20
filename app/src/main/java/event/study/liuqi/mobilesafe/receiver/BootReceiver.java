package event.study.liuqi.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.SpUtils;
/**
 * Created by liuqi on 2016/11/1.
 */

public class BootReceiver extends BroadcastReceiver {
    private String tag ="BootReceiver" ;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(tag,"手机重启了，监听广播");
        //1.获取机开机的sim卡号
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = tm.getSimSerialNumber();
        //2.sp中存储的sim卡号
        String spnumber = SpUtils.getString(context, ConstansValue.SIMNUMBER, "");
        //3.对比是否一致
        if(!simSerialNumber.equals(spnumber)&&SpUtils.getboolean(context,ConstansValue.SETUPOVER,false)){
            //4.发送短信
            SmsManager sms = SmsManager.getDefault();
            String safenumber = SpUtils.getString(context, ConstansValue.SAFENUMBER, "");
            sms.sendTextMessage(safenumber,null,"sim change!",null,null);
        }
    }
}
