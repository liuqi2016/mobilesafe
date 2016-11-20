package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import event.study.liuqi.mobilesafe.R;

/**
 * Created by liuqi on 2016/10/28.
 */
public class Setup1Activity extends BaseSetupActivity{
    private String tag="Setup1Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

    }
    @Override
    public void showNextPage() {
        Intent intent = new Intent(Setup1Activity.this, Setup2Activity.class);
        startActivity(intent);
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }
    @Override
    public void showPrePage() {
        //无事件
    }
}
