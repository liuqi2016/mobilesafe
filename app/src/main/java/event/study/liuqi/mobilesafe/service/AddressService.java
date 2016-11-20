package event.study.liuqi.mobilesafe.service;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.engine.AddressDao;
import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.SpUtils;
/**
 * Created by liuqi on 2016/11/4.
 */

public class AddressService extends Service {
    private String tag="AddressService";
    private TelephonyManager tM;
    private myPhoneStateListener listener;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private WindowManager wm;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tv_phone_come.setText(address);
        }
    };
    private String address;
    private TextView tv_phone_come;
    private View inflate;
    private int widthPixels;
    private int heightPixels;
    private InnerOutCallReceiver innerOutCallReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        //电话服务的监听，服务开启监听开始，服务关闭监听结束
        //1.电话对象管理者
        tM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //2.监听电话状态
        listener = new myPhoneStateListener();
        tM.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);
        //获取窗体管理对象
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        //获取屏幕宽高
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
        //动态开启广播，监听去电
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        innerOutCallReceiver = new InnerOutCallReceiver();
        registerReceiver(innerOutCallReceiver,intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    class myPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                //挂断
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(tag,"挂断。。。。。。。");
                    //去掉显示
                    if(wm!=null && inflate!=null){
                        wm.removeView(inflate);
                    }
                    break;
                //接通
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(tag,"接通。。。。。。。");
                    break;
                //响铃
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(tag,"响铃。。。。。。。");
                    showToast(incomingNumber);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    /**
     * 自定义toast
     */
    private void showToast(String s) {
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.gravity=Gravity.LEFT+Gravity.TOP;
        //读取sp中的位置
        int location_x = SpUtils.getInt(getApplicationContext(), ConstansValue.LOCATION_X, 0);
        int location_y = SpUtils.getInt(getApplicationContext(), ConstansValue.LOCATION_Y, 0);
        //左上角坐标
        params.x=location_x;
        params.y=location_y;
        inflate = View.inflate(getApplicationContext(), R.layout.toast_view, null);
        inflate.setOnTouchListener(new View.OnTouchListener() {
            private int startY;
            private int startX;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int stopX = (int) event.getRawX();
                        int stopY = (int) event.getRawY();
                        //偏移量
                        int offsetX = stopX - startX;
                        int offsetY = stopY - startY;
                        //当前控件在屏幕的位置
                        params.x=params.x+offsetX;
                        params.y=params.y+offsetY;
                        //容错处理
                        if(params.x<0){
                            params.x=0;
                        }
                        if(params.y<0){
                            params.y=0;
                        }
                        if(params.x>widthPixels-inflate.getWidth()){
                            params.x=widthPixels-inflate.getWidth();
                        }
                        if(params.y>heightPixels-inflate.getHeight()-22){
                            params.y=heightPixels-inflate.getHeight()-22;
                        }
                        wm.updateViewLayout(inflate,params);
                        startX = stopX;
                        startY = stopY;
                        break;
                    case MotionEvent.ACTION_UP:
                        SpUtils.putInt(getApplicationContext(), ConstansValue.LOCATION_X, params.x);
                        SpUtils.putInt(getApplicationContext(), ConstansValue.LOCATION_Y, params.y);
                        break;
                }
                //响应拖拽事件 false响应其他事件
                return true;
            }
        });
        int style_int = SpUtils.getInt(getApplicationContext(), ConstansValue.ADDRESSSTYLE, 0);
        tv_phone_come = (TextView) inflate.findViewById(R.id.tv_phone_come);
        switch (style_int){
            case 0:
                tv_phone_come.setBackgroundResource(R.drawable.call_locate_white);
                break;
            case 1:
                tv_phone_come.setBackgroundResource(R.drawable.call_locate_orange);
                break;
            case 2:
                tv_phone_come.setBackgroundResource(R.drawable.call_locate_blue);
                break;
            case 3:
                tv_phone_come.setBackgroundResource(R.drawable.call_locate_gray);
                break;
            case 4:
                tv_phone_come.setBackgroundResource(R.drawable.call_locate_green);
                break;
        }

        wm.addView(inflate,params);
        //查询归属地
        query(s);
    }
   //耗时操作，查询归属地
    private void query(final String s) {
        new Thread(){
            @Override
            public void run() {
                address = AddressDao.getAddress(s);
                Log.i(tag,"归属地为:"+address);
                mHandler.sendEmptyMessage(0);
                super.run();
            }
        }.start();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if(tM!=null && listener!=null){
         tM.listen(listener,PhoneStateListener.LISTEN_NONE);
            Log.i(tag,"关闭服务");
        };
        if(innerOutCallReceiver != null){
            //注销
            unregisterReceiver(innerOutCallReceiver);
        }
        super.onDestroy();
    }

    private class InnerOutCallReceiver  extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String phone = getResultData();
            Log.i(tag,"phone:"+phone);
            showToast(phone);
        }
    }
}
