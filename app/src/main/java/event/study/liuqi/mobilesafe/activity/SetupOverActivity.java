package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.SpUtils;

/**
 * Created by liuqi on 2016/10/28.
 */
public class SetupOverActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean pwdtrue = SpUtils.getboolean(this, ConstansValue.SETUPOVER, false);
        //密码输入成功，并且四个导航界面设置完成--->停留在设置完成界面
        if(pwdtrue){
            setContentView(R.layout.acticity_setup_over);
        }else{
            //密码输入成功,四个导航没有设置完成 --->停留在导航界面
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
