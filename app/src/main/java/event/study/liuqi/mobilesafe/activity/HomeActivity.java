package event.study.liuqi.mobilesafe.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.SpUtils;
import event.study.liuqi.mobilesafe.utils.ToastUtils;

/**
 * Created by liuqi on 2016/10/23.
 */
public class HomeActivity extends Activity {

    private GridView gv_app;
    private int[]  mImages ;
    private String[] mAppnames;

    private String tag = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUi();
        //初始化数据方法
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mAppnames = new String[]{
                "手机防盗",
                "通信卫士",
                "软件管理",
                "进程管理",
                "流量统计",
                "手机杀毒",
                "缓存清理",
                "高级工具",
                "设置中心"
        };
        mImages = new int[]{
                R.drawable.home_safe,
                R.drawable.home_callmsgsafe,
                R.drawable.home_apps,
                R.drawable.home_taskmanager,
                R.drawable.home_netmanager,
                R.drawable.home_trojan,
                R.drawable.home_sysoptimize,
                R.drawable.home_tools,
                R.drawable.home_settings,

        };

        gv_app.setAdapter(new Myadapter());
        gv_app.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        //手机防盗
                        showDialog();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }

            }
        });
    }

    /**
     * 手机防盗
     */
    private void showDialog() {
        //判断本地是否保存密码
        String pwd = SpUtils.getString(this, ConstansValue.MOBILESAFEPWD, "");
        if(pwd.isEmpty()){
            //1.显示初次密码框
            showSetPasswordDialog();
        }else{
            //2.保存密码后的框
            showSetConfirmPsdDialog();
        }
    }
    /**
     * 设置密码对话框
     */
    private void showSetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.passworddialog, null);
        dialog.setView(view);
        dialog.show();
        final EditText et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        final EditText et_confirm_pwd = (EditText) view.findViewById(R.id.et_confirm_pwd);
        Button bt_confirm_pwd = (Button) view.findViewById(R.id.bt_confirm_pwd);
        Button bt_reset_pwd = (Button) view.findViewById(R.id.bt_reset_pwd);
        //点击确认
        bt_confirm_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = et_pwd.getText().toString();
                String confirm_pwd = et_confirm_pwd.getText().toString();
                if(!pwd.isEmpty() && !confirm_pwd.isEmpty()){
                if(pwd.equals(confirm_pwd)){
                    //保存到sp
                    SpUtils.putString(getApplicationContext(),ConstansValue.MOBILESAFEPWD,pwd);
                    //进入设置界面
                    Intent intent = new Intent(getApplicationContext(), SafeActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                }else{
                    ToastUtils.show(getApplicationContext(),"你输入的两次密码不相同");
                }
            }else{
                    ToastUtils.show(getApplicationContext(),"请输入密码");
                }
            }
        });
        //点击取消
        bt_reset_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            dialog.dismiss();
            }
        });
    }
    /**
     * 确认密码后的对话框
     */
    private void showSetConfirmPsdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.passworddialog2, null);
        dialog.setView(view);
        dialog.show();
        //验证密码
        final EditText et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        Button bt_confirm_pwd = (Button) view.findViewById(R.id.bt_confirm_pwd);
        Button bt_reset_pwd = (Button) view.findViewById(R.id.bt_reset_pwd);
        bt_confirm_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = SpUtils.getString(getApplicationContext(), ConstansValue.MOBILESAFEPWD, "");
                if(pwd.equals(et_pwd.getText().toString())){
                    //进入设置界面
                    Intent intent = new Intent(getApplicationContext(), SafeActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                }else{
                    ToastUtils.show(getApplicationContext(),"密码错误");
                }
            }
        });
        bt_reset_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
    /**
     * 初始化Ui
     */
    private void initUi() {
        gv_app = (GridView) findViewById(R.id.gv_app);
    }
    class Myadapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mAppnames.length;
        }

        @Override
        public Object getItem(int i) {
            return mAppnames[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = View.inflate(getApplicationContext(), R.layout.gridiew_item, null);
            TextView tv_appname = (TextView) view1.findViewById(R.id.tv_appname);
            ImageView iv_icon = (ImageView) view1.findViewById(R.id.iv_icon);
            tv_appname.setText(mAppnames[i]);
            iv_icon.setBackgroundResource(mImages[i]);
            return view1;
        }
    }
}
