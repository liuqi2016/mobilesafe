package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.db.domain.ProcessInfo;
import event.study.liuqi.mobilesafe.engine.ProccessInfoProvider;
import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.SpUtils;

/**
 * 进程管理
 * Created by liuqi on 2016/11/19.
 */
public class ProcessManager extends Activity implements View.OnClickListener {
    private ArrayList<ProcessInfo> userproccess, systemproccess;
    private ListView lv_proccess;
    private Button bt_all_select, bt_all_select_opp, bt_clean_all, bt_setting;
    private Myadapter madapter;
    private int count;
    private long availSpace;
    private long allSpace;
    private List<ProcessInfo> proccessInfos;
    private ProcessInfo mProcessInfo;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            madapter = new Myadapter();
            lv_proccess.setAdapter(madapter);
            //设置进程数
            if(SpUtils.getboolean(getApplicationContext(),ConstansValue.SHOWSYSTEMPROCCESS,false)){
                tv_proccess_count.setText("进程总数:" +(userproccess.size()+systemproccess.size()));
            }else{
                tv_proccess_count.setText("进程总数:" +userproccess.size());
            }

        }
    };
    private TextView tv_proccess_count;
    private TextView tv_proccess_space;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processmanger);
        initUI();
    }

    private void initUI() {
        initTitle();
        initListView();
        initButton();
        initListViewData();
    }

    private void initButton() {
        bt_all_select = (Button) findViewById(R.id.bt_all_select);
        bt_all_select.setOnClickListener(this);
        bt_all_select_opp = (Button) findViewById(R.id.bt_all_select_opp);
        bt_all_select_opp.setOnClickListener(this);
        bt_clean_all = (Button) findViewById(R.id.bt_clean_all);
        bt_clean_all.setOnClickListener(this);
        bt_setting = (Button) findViewById(R.id.bt_setting);
        bt_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_all_select: {
                //全选
                selectall();
                break;
            }
            case R.id.bt_all_select_opp: {
                //反选
                selectopp();
                break;
            }
            case R.id.bt_clean_all: {
                //一键清理
                cleanall();
                break;
            }
            case R.id.bt_setting: {
                //进程设置
                Intent intent = new Intent(this, ProccessSetting.class);
                startActivityForResult(intent,0);
                break;
            }
        }

    }

    /**
     * 回调activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //通知适配器刷新
        if (madapter != null) {
            madapter.notifyDataSetChanged();
        }
        if(SpUtils.getboolean(getApplicationContext(),ConstansValue.SHOWSYSTEMPROCCESS,false)){
            tv_proccess_count.setText("进程总数:" +(userproccess.size()+systemproccess.size()));
        }else{
            tv_proccess_count.setText("进程总数:" +userproccess.size());
        }

    }

    private void cleanall() {
        //1.获取被选中的进程
        ArrayList<ProcessInfo> lists = new ArrayList<>();
        for (ProcessInfo proccess : userproccess) {
            if (proccess.isCheck() == true) {
                lists.add(proccess);
            }
        }
        for (ProcessInfo proccess : systemproccess) {
            if (proccess.isCheck() == true) {
                lists.add(proccess);
            }
        }
        //记录进程大小
        int totalproccess = 0;
        //2.便利循环要被杀死的进程，从集中移除
        for (ProcessInfo processInfo:lists){
            //判断当前进程在哪个集合中
            if(userproccess.contains(processInfo)){
                userproccess.remove(processInfo);
            }
            if(systemproccess.contains(processInfo)){
                systemproccess.remove(processInfo);
            }
            totalproccess +=processInfo.memSize;
//3.杀死进程
            ProccessInfoProvider.killproccess(getApplicationContext(),processInfo);
    }
    //4.更新进程总数
        count-=lists.size();
    //5.更新剩余内存数
        availSpace+=totalproccess;
        tv_proccess_count.setText("进程总数:" + count);
        tv_proccess_space.setText("剩余/总共:" + Formatter.formatFileSize(getApplicationContext(),availSpace) + "/" + Formatter.formatFileSize(getApplicationContext(),allSpace));
    //更新adapyer
        Toast.makeText(getApplicationContext(),String.format("杀死了%d个进程释放了%s空间",lists.size(),Formatter.formatFileSize(getApplicationContext(),totalproccess)),Toast.LENGTH_SHORT).show();
    if(madapter!=null)
    {
        madapter.notifyDataSetChanged();
    }
    }

    /**
     * 反选
     */
    private void selectopp() {
        for (ProcessInfo proccess : userproccess) {
            if (proccess.getPackageName().equals(getPackageName())) {
                continue;
            }
            proccess.isCheck = !proccess.isCheck;
        }
        for (ProcessInfo proccess2 : systemproccess) {
            proccess2.isCheck = !proccess2.isCheck;
        }
        //通知适配器刷新
        if (madapter != null) {
            madapter.notifyDataSetChanged();
        }
    }

    /**
     * 全选
     */
    private void selectall() {
        for (ProcessInfo proccess : userproccess) {
            if (proccess.getPackageName().equals(getPackageName())) {
                continue;
            }
            proccess.isCheck = true;
        }
        for (ProcessInfo proccess2 : systemproccess) {
            proccess2.isCheck = true;
        }
        //通知适配器刷新
        if (madapter != null) {
            madapter.notifyDataSetChanged();
        }
    }

    private void initListView() {
        lv_proccess = (ListView) findViewById(R.id.lv_proccess);
        final TextView tv_des = (TextView) findViewById(R.id.tv_des);
        lv_proccess.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userproccess != null && systemproccess != null) {
                    if (userproccess.size() >= firstVisibleItem) {
                        tv_des.setText("用户进程(" + userproccess.size() + ")");
                    } else {
                        tv_des.setText("系统进程(" + systemproccess.size() + ")");
                    }
                }
            }
        });
        lv_proccess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == userproccess.size() + 1) {
                    return;
                } else {
                    if (position < userproccess.size() + 1) {
                        mProcessInfo = userproccess.get(position - 1);
                    } else {
                        //返回系统应用对应条目的对象
                        mProcessInfo = systemproccess.get(position - userproccess.size() - 2);
                    }
                    if (mProcessInfo != null) {
                        if (!mProcessInfo.packageName.equals(getPackageName())) {
                            //选中条目指向的对象和本应用的包名不一致,才需要去状态取反和设置单选框状态
                            //状态取反
                            mProcessInfo.isCheck = !mProcessInfo.isCheck;
                            //checkbox显示状态切换
                            //通过选中条目的view对象,findViewById找到此条目指向的cb_box,然后切换其状态
                            CheckBox cb_box = (CheckBox) view.findViewById(R.id.cb_box);
                            cb_box.setChecked(mProcessInfo.isCheck);
                        }
                    }
                }
            }
        });
    }

    private void initListViewData() {
        getData();
    }

    private void getData() {
        new Thread() {
            @Override
            public void run() {
                proccessInfos = ProccessInfoProvider.getProccessInfos(getApplicationContext());
                userproccess = new ArrayList<>();
                systemproccess = new ArrayList<>();
                for (ProcessInfo proccess : proccessInfos) {
                    if (proccess.isSystem) {
                        systemproccess.add(proccess);
                    } else {
                        userproccess.add(proccess);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initTitle() {
        //1.进程数
        tv_proccess_count = (TextView) findViewById(R.id.tv_proccess_count);
        count = ProccessInfoProvider.getProccessCount(this);
        tv_proccess_count.setText("进程总数:" + count);
        //2.剩余进程数
        availSpace = ProccessInfoProvider.getAvailSpace(this);
        String availsize = Formatter.formatFileSize(getApplicationContext(), availSpace);
        //3.总共进程数
        allSpace = ProccessInfoProvider.getAllSpace(this);
        String allsize = Formatter.formatFileSize(getApplicationContext(), allSpace);
        tv_proccess_space = (TextView) findViewById(R.id.tv_proccess_space);
        tv_proccess_space.setText("剩余/总共:" + availsize + "/" + allsize);
    }

    class Myadapter extends BaseAdapter {
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == userproccess.size() + 1) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getCount() {
            Boolean isshow = SpUtils.getboolean(getApplicationContext(), ConstansValue.SHOWSYSTEMPROCCESS, false);
            if(!isshow){
                count=userproccess.size();
                return userproccess.size()+1;
            }else {
                count=userproccess.size()+systemproccess.size();
                return userproccess.size() + systemproccess.size() + 2;
            }
        }

        @Override
        public ProcessInfo getItem(int position) {
            if (position == 0 || position == userproccess.size() + 1) {
                return null;
            } else {
                if (position < userproccess.size() + 1) {
                    return userproccess.get(position - 1);
                } else {
                    return systemproccess.get(position - userproccess.size() - 2);
                }
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int itemViewType = getItemViewType(position);
            if (itemViewType == 0) {
                ViewTitleHolder holder = null;
                if (convertView == null) {
                    holder = new ViewTitleHolder();
                    convertView = View.inflate(getApplicationContext(), R.layout.item_proccess_title, null);
                    holder.tv_title = (TextView) convertView.findViewById(R.id.tv_des);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewTitleHolder) convertView.getTag();
                }
                if (position == 0) {
                    holder.tv_title.setText("用户进程(" + userproccess.size() + ")");
                } else {
                    holder.tv_title.setText("系统进程(" + systemproccess.size() + ")");
                }
            } else {
                ViewHolder holder = null;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = View.inflate(getApplicationContext(), R.layout.item_proccess, null);
                    holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                    holder.tv_memory = (TextView) convertView.findViewById(R.id.tv_memory);
                    holder.cb_box = (CheckBox) convertView.findViewById(R.id.cb_box);
                    if (getItem(position).packageName.equals(getPackageName())) {
                        holder.cb_box.setVisibility(View.GONE);
                    } else {
                        holder.cb_box.setVisibility(View.VISIBLE);
                    }

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.cb_box.setChecked(getItem(position).isCheck);
                holder.iv_icon.setImageDrawable((getItem(position).icon));
                holder.tv_title.setText(getItem(position).name);
                holder.tv_memory.setText("内存占用:" + Formatter.formatFileSize(getApplicationContext(), getItem(position).memSize));
            }
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_memory;
        CheckBox cb_box;
    }

    private class ViewTitleHolder {
        TextView tv_title;
    }
}
