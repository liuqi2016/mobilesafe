package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import event.study.liuqi.mobilesafe.R;

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
