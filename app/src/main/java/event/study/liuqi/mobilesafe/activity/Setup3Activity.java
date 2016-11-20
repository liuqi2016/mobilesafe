package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.SpUtils;
import event.study.liuqi.mobilesafe.utils.ToastUtils;

/**
 * Created by liuqi on 2016/10/29.
 */
public class Setup3Activity extends BaseSetupActivity{

    private EditText et_contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initUi();
    }

    @Override
    public void showNextPage() {
        //判断是否有号码输入
        String safeNumber = et_contacts.getText().toString();
        if(!safeNumber.isEmpty()) {
            //保存号码到sp
            SpUtils.putString(getApplicationContext(), ConstansValue.SAFENUMBER,safeNumber);
            Intent intent = new Intent(this, Setup4Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else{
            ToastUtils.show(getApplicationContext(),"请输入安全号码");
        }
    }

    @Override
    public void showPrePage() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    /**
     * 初始化Ui
     */
    private void initUi() {
        Button bt_contacts = (Button) findViewById(R.id.bt_contacts);
        et_contacts = (EditText) findViewById(R.id.et_contacts);
        //回显安全号码
        et_contacts.setText(SpUtils.getString(getApplicationContext(),ConstansValue.SAFENUMBER,""));
        bt_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), SetupContactsActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0){
            String number = data.getStringExtra("number");
            et_contacts.setText(number);
        }
    }
}
