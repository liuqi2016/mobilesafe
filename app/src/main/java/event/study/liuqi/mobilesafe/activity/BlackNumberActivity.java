package event.study.liuqi.mobilesafe.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.List;
import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.db.dao.BlackNumberDao;
import event.study.liuqi.mobilesafe.db.domain.BlackNumberInfo;
/**
 * Created by liuqi on 2016/11/10.
 */
//listView 优化 1.复用contertView 2.使用ViewHolder 3.优化查询
public class BlackNumberActivity extends Activity{
    private BlackNumberDao db;
    private List<BlackNumberInfo> blackNumberInfos;
    private int checked_id = 1;
    private ListView ll_blacknumber;
    private Myadapter myadapter;
    Handler mhanlder =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(myadapter==null){
                myadapter = new Myadapter();
                ll_blacknumber.setAdapter(myadapter);
            }else{
                myadapter.notifyDataSetChanged();
            }
        }
    };
    private boolean mIsLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.black_number);
        initUI();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        new Thread() {
            @Override
            public void run() {
                db = BlackNumberDao.getInstance(getApplicationContext());
                blackNumberInfos = db.find(0);//默认查询前20条
                mhanlder.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        Button bt_add = (Button) findViewById(R.id.bt_add_backnumber);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        //展示列表
        ll_blacknumber = (ListView) findViewById(R.id.ll_blacknumber);
        //滚动监听
        ll_blacknumber.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (blackNumberInfos != null) {
                    //当屏幕停止滚动监听
                    switch (scrollState) {
                        case SCROLL_STATE_IDLE:
                            if (ll_blacknumber.getLastVisiblePosition() >= blackNumberInfos.size() - 1 && !mIsLoad) {
                        	/*mIsLoad防止重复加载的变量
						如果当前正在加载mIsLoad就会为true,本次加载完毕后,再将mIsLoad改为false
						如果下一次加载需要去做执行的时候,会判断上诉mIsLoad变量,是否为false,如果为true,就需要等待上一次加载完成,将其值
						改为false后再去加载*/
                                //如果条目的总数大于集合的总数就去更新
                                int count = db.count();
                                if (count > blackNumberInfos.size()) {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            List<BlackNumberInfo> newInfos = db.find(blackNumberInfos.size());
                                            blackNumberInfos.addAll(newInfos);
                                            mhanlder.sendEmptyMessage(0);
                                        }
                                    }.start();
                                }
                            }
                            break;
                        case SCROLL_STATE_TOUCH_SCROLL:
                            break;
                        case SCROLL_STATE_FLING:
                            break;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
    class Myadapter extends BaseAdapter{
        @Override
        public int getCount() {
            return blackNumberInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return blackNumberInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                 convertView=View.inflate(getApplicationContext(),R.layout.item_blacknumber,null);
                 holder = new ViewHolder();
                 holder.tv_blacknumber = (TextView) convertView.findViewById(R.id.tv_blacknumber);
                 holder.tv_blacknumbertype = (TextView) convertView.findViewById(R.id.tv_blacknumbertype);
                 holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除数据库中对应数据
                    db.delect(blackNumberInfos.get(position).number);
                    //删除集合中的数据并更新UI
                    blackNumberInfos.remove(position);
                    myadapter.notifyDataSetChanged();
                }
            });
            holder.tv_blacknumber.setText(blackNumberInfos.get(position).number);
            String blacktype = null;
            switch (blackNumberInfos.get(position).type){
                case 1:
                    blacktype ="拦截短信";
                    break;
                case 2 :
                    blacktype ="拦截电话";
                    break;
                case 3 :
                    blacktype = "拦截所有";
                    break;
            }
            holder.tv_blacknumbertype.setText(blacktype);
            return convertView;
        }
    }
    //复用viewHolder第二步
    static class ViewHolder{
        TextView tv_blacknumber ;
        TextView tv_blacknumbertype ;
        ImageView iv_delete ;
    }
   //显示弹窗
    private void showDialog() {
        //alert
        AlertDialog.Builder alert = new AlertDialog.Builder(BlackNumberActivity.this);
        final AlertDialog dialog = alert.create();
        View inflate = View.inflate(getApplicationContext(), R.layout.dialogblacknumber, null);
        dialog.setView(inflate);
        dialog.show();
        //添加数据，并显示到条目上
        final EditText et_blacknumber = (EditText) inflate.findViewById(R.id.et_blacknumber);
        Button bt_confirm = (Button) inflate.findViewById(R.id.bt_confirm);
        Button bt_reset = (Button) inflate.findViewById(R.id.bt_reset);
        final RadioGroup rg_type = (RadioGroup) inflate.findViewById(R.id.rg_type);
        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_sms:
                        checked_id=1;
                        break;
                    case  R.id.rb_phone:
                        checked_id=2;
                        break;
                    case R.id.rb_all:
                        checked_id=3;
                        break;
                }
            }
        });
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.添加数据到数据库
                String blacknumber = et_blacknumber.getText().toString();
                if(blacknumber != null) {
                    db.insert(blacknumber,checked_id);
                //2.更新列表
                BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                blackNumberInfo.number=blacknumber;
                blackNumberInfo.type=checked_id;
                blackNumberInfos.add(0,blackNumberInfo);
                //通知适配器刷新
                if(ll_blacknumber != null){
                    myadapter.notifyDataSetChanged();
                }
                }
                dialog.dismiss();
            }
        });
        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
