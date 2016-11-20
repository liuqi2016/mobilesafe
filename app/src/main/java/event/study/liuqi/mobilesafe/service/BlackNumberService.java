package event.study.liuqi.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

import event.study.liuqi.mobilesafe.db.dao.BlackNumberDao;

/**
 * Created by liuqi on 2016/11/14.
 */
public class BlackNumberService extends Service {

    private BlackNumberDao db;
    private TelephonyManager mTM;
    private MyPhoneStateListener mPhoneStateListener;
    private int mode = 0;
    private InnerSmsReceiver mSmsReceiver;
    private MyContentObserver myContentObserver;

    /**
     * 初始化服务
     */
    @Override
    public void onCreate() {
        db = BlackNumberDao.getInstance(getApplicationContext());
        //拦截短信
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(1000);
        mSmsReceiver = new InnerSmsReceiver();
        registerReceiver(mSmsReceiver, intentFilter);
        //拦截电话
        //监听电话的状态
        //1,电话管理者对象
        mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //2,监听电话状态
        mPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        //拦截所有
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private class InnerSmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取短信内容,获取发送短信电话号码,如果此电话号码在黑名单中,并且拦截模式也为1(短信)或者3(所有),拦截短信
            //1.获取短信内容
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            //2.遍历循环短信
            for (Object pdu : pdus) {
                //3.获取短信内容对象
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                //4,获取短信对象的基本信息
                String originatingAddress = sms.getOriginatingAddress();
                String messageBody = sms.getMessageBody();
                mode = db.getMode(originatingAddress);
                if (mode == 1 || mode == 3) {
                    //拦截短信(android 4.4版本失效	短信数据库,删除)
                    abortBroadcast();
                    deleteSms(originatingAddress);
                }

            }
        }
    }

    /**
     * 删除短信
     */
    private void deleteSms(String number) {
        Log.i("联系人电话:", number);
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "body"}, "address = ?", new String[]{number}, "_id desc");

        while (cursor.moveToNext()) {
            Log.i("要删除的短信id", cursor.getString(0));
            Log.i("要删除的短信联系", cursor.getString(1));
            Log.i("要删除的短信内容", cursor.getString(2));
            int id = cursor.getInt(0);
            resolver.delete(Uri.parse("content://sms/"), "_id=?", new String[]{id + ""});
        }
    }

    /**
     * 电话监听
     */
    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //挂断电话 	aidl文件中去了
                    mode = db.getMode(incomingNumber);
                    if (mode == 2 || mode == 3) {
                        endCall(incomingNumber);
                        //监听数据库变化
                        myContentObserver = new MyContentObserver(new Handler(), incomingNumber);
                        getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls"), true, myContentObserver);
                    }
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void endCall(String phone) {
        int mode = db.getMode(phone);

        if (mode == 2 || mode == 3) {
//			ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
            //ServiceManager此类android对开发者隐藏,所以不能去直接调用其方法,需要反射调用
            try {
                //1,获取ServiceManager字节码文件
                Class<?> clazz = Class.forName("android.os.ServiceManager");
                //2,获取方法
                Method method = clazz.getMethod("getService", String.class);
                //3,反射调用此方法
                IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
                //4,调用获取aidl文件对象方法
                ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
                //5,调用在aidl中隐藏的endCall方法
                iTelephony.endCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class MyContentObserver extends ContentObserver {
        private final String callnumber;

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler, String number) {
            super(handler);
            this.callnumber = number;
        }

        @Override
        public void onChange(boolean selfChange) {
            //删除通话记录
            ContentResolver resolver = getContentResolver();
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            resolver.delete(CallLog.Calls.CONTENT_URI, "number=?", new String[]{callnumber});
            //注销监听服务
            getContentResolver().unregisterContentObserver(myContentObserver);
            super.onChange(selfChange);
        }
    }
    /**
     * 销毁服务
     */
    @Override
    public void onDestroy() {
        //注销电话监听
        if(mPhoneStateListener!=null){
            mTM.listen(mPhoneStateListener, MyPhoneStateListener.LISTEN_NONE);
        }
        //注销广播
        if(mSmsReceiver!=null){
            unregisterReceiver(mSmsReceiver);
        }
        //注销观察者
        if(myContentObserver !=null ){
            getContentResolver().unregisterContentObserver(myContentObserver);
        }
        super.onDestroy();
    }
}
