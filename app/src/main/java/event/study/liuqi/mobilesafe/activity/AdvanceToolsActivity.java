package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.engine.SmsBackUp;

/**
 * Created by liuqi on 2016/11/3.
 */
public class AdvanceToolsActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advancetools);
        initUI();
    }
    private void initUI() {
        Button click1 = (Button) findViewById(R.id.click1);
        Button click2 = (Button) findViewById(R.id.click2);
        Button click3 = (Button) findViewById(R.id.click3);
        Button click4 = (Button) findViewById(R.id.click4);
        //归属地查询
        click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvanceToolsActivity.this,QueryAddressActivity.class);
                startActivity(intent);
            }
        });
        //短信备份
        click2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //0.显示进度条
                final ProgressDialog progressDialog = new ProgressDialog(AdvanceToolsActivity.this);
                progressDialog.setTitle("短信备份");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                //短信备份
                new Thread(){
                    @Override
                    public void run() {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"smsback.xml";
                        SmsBackUp.smsback(getApplicationContext(), path, new SmsBackUp.CallBack() {
                            @Override
                            public void setMax(int max) {
                            progressDialog.setMax(max);
                            }
                            @Override
                            public void setProgress(int index) {
                                progressDialog.setProgress(index);
                            }
                        });
                        progressDialog.dismiss();
                    }
                }.start();
            }
        });
    }
}
