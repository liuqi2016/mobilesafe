package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.engine.AddressDao;

/**
 * Created by liuqi on 2016/11/3.
 */
public class QueryAddressActivity extends Activity{

    String PATH = "/data/data/event.study.liuqi.mobilesafe/files/address.db";
    private SQLiteDatabase db;
    private String tag="QueryAddress";
    private TextView tv_result;
    private String location;
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tv_result.setText(location);
        }
    };
    private Button bt_query;
    private EditText et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);
        initUI();
    }
    private void initUI() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        bt_query = (Button) findViewById(R.id.bt_query);
        tv_result = (TextView) findViewById(R.id.tv_result);
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1.获取电话号码
                String phone = et_phone.getText().toString();
                if(!phone.isEmpty()){
                    //2.查询归属地,耗时操作，子线程进行
                    String address = AddressDao.getAddress(phone);
                    location=address;
                    mHandler.sendEmptyMessage(0);
                }else{
                    //抖动
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    findViewById(R.id.et_phone).startAnimation(shake);
                    //手机震动
                    Vibrator vt = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    long [] pattern = {1000,2000,1000,2000};
                    vt.vibrate(pattern,-1);//重复两次
                }
            }
        });
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
              String phone = et_phone.getText().toString();
                String address = AddressDao.getAddress(phone);
                location=address;
                mHandler.sendEmptyMessage(0);
            }
        });
    }
}
