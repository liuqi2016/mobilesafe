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
public class Setup1Activity extends Activity{
    private String tag="Setup1Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }
   public void nextPage(View view){
       Intent intent = new Intent(Setup1Activity.this, Setup2Activity.class);
       startActivity(intent);
       finish();
   }
}
