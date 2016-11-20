package event.study.liuqi.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.service.LocationService;
import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.SpUtils;
import event.study.liuqi.mobilesafe.utils.ToastUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by liuqi on 2016/11/1.
 */

public class SmsReceiver extends BroadcastReceiver {
    private String tag ="SmsReceiver";
    private ComponentName mDeviceAdminSample;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(tag,"接收短信广播");
//        1.判断安全防盗已开启
        Boolean ison = SpUtils.getboolean(context, ConstansValue.SAFEON, false);
        mDeviceAdminSample = new ComponentName(context, AdminReceiver.class);
        DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //2.接收短信
        if(ison){
            Log.i(tag,"安全防盗开启");
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            //3.遍历循环短信
            for (Object pdu:pdus) {
                //4.Sms获取短信对象
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                //5.获取短信对象的基本内容
                String originatingAddress = sms.getOriginatingAddress();
                String messageBody = sms.getMessageBody();
                //6.判断内容是否包含#*alarm*#
                String safenumber = "+86"+SpUtils.getString(context, ConstansValue.SAFENUMBER, "");
                if(messageBody.contains("#*alarm*#")){
                    //7.播放音乐
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    Log.i(tag,"播放报警音乐");
                }
                if(messageBody.contains("#*location*#")){
                    //开启定位服务
                    Log.i(tag,"定位开启");
                    Intent intent1 = new Intent(context,LocationService.class);
                    context.startService(intent1);
                }
                if(messageBody.contains("#*wipedata*#")){
                    //开启定位服务
                    Log.i(tag,"数据销毁");
                    //判断是否激活设备
                    if(mDPM.isAdminActive(mDeviceAdminSample) && SpUtils.getboolean(context,ConstansValue.SAFEON,false)){
                        Log.i(tag,"清除数据");
                        mDPM.wipeData(0);
                    }
                }
                if(messageBody.contains("#*lockscreen*#")){
                    //一键锁屏
                    Log.i(tag,"锁屏加密");
                    if(mDPM.isAdminActive(mDeviceAdminSample) && SpUtils.getboolean(context,ConstansValue.SAFEON,false)){
                        mDPM.lockNow();
                        mDPM.resetPassword("",0);
                    }
                }
            }
        }
    }
}
