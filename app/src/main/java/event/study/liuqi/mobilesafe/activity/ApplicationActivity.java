package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.util.ArrayList;
import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.db.domain.AppInfo;
import event.study.liuqi.mobilesafe.engine.AppInfoProvider;
import event.study.liuqi.mobilesafe.utils.ToastUtils;

/**
 * Created by liuqi on 2016/11/16.
 */
public class ApplicationActivity extends Activity{

    private Myadapter madapter;
    Handler mhandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(madapter == null){
                //展示应用程序信息
                madapter = new Myadapter();
                listview.setAdapter(madapter);
            }else {
                listview.setAdapter(madapter);
            }
        }
    };
    private ListView listview;
    //所有应用
    private ArrayList<AppInfo> appInfos;
    private ArrayList<AppInfo> userapps;
    private ArrayList<AppInfo> systemapps;
    private TextView tvtitle;
    private AppInfo currentAppInfo;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        initUI();
    }
    /**
     * 初始化ui
     */
    private void initUI() {
        initTitle();
        listview = (ListView) findViewById(R.id.lv_application);
        tvtitle = (TextView) findViewById(R.id.tv_apptitle);
        loadapplists();
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userapps != null && systemapps != null) {
                    if (firstVisibleItem >= userapps.size() + 1) {
                        tvtitle.setText("系统应用(" + systemapps.size() + ")");
                    } else {
                        tvtitle.setText("用户应用(" + userapps.size() + ")");
                    }
                }
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0 || position==userapps.size()+1){
                    currentAppInfo = null;
                    return;
                }else if(position<userapps.size()+1){
                    //显示弹窗
                 showPopupwindow(view);
                    //设置指针
                    currentAppInfo = userapps.get(position-1);
                }else{
                    currentAppInfo=systemapps.get(position-userapps.size()-2);
                    showPopupwindow(view);
                }
            }
        });

    }

    private void loadapplists() {
        //获取所有安装应用信息
        new Thread(){
            @Override
            public void run() {
                appInfos = AppInfoProvider.getAppInfos(getApplicationContext());
                userapps=new ArrayList<AppInfo>();
                systemapps=new ArrayList<AppInfo>();
                for (AppInfo app : appInfos){
                    if(app.isUserApp){
                        //用户应用
                        userapps.add(app);
                    }else{
                        //系统应用
                        systemapps.add(app);
                    }
                }
                mhandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initTitle() {
        TextView memoryAvailSpace = (TextView) findViewById(R.id.tv_memoryAvailSpace);
        TextView sdAvailSpace = (TextView) findViewById(R.id.tv_sdAvailSpace);
        //获取磁盘大小
        String path = Environment.getDataDirectory().getAbsolutePath();
        //获取sd卡大小
        String sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //获取上面两个路径文件夹可用大小
        String size = Formatter.formatFileSize(this, getAvailSpace(path));
        String sdsize = Formatter.formatFileSize(this, getAvailSpace(sdpath));
        memoryAvailSpace.setText("磁盘可用:"+size);
        sdAvailSpace.setText("sd卡可用:"+sdsize);
    }

    /**
     * 返回可用区域的byte类型值
     * @param path
     * @return byte
     */
    private long getAvailSpace(String path) {
        //获取可用磁盘大小类
        StatFs statFs = new StatFs(path);
        //获取可用区域的个数
        long count = statFs.getAvailableBlocks();
        //获取可用区块的大小
        int size = statFs.getBlockSize();
        return count*size;
    }

    private class Myadapter extends BaseAdapter{
        /**
         * f返回当前item类型总数
         * @return
         */
        @Override
        public int getViewTypeCount() {
            return 2;
        }
        /**
         * 返回当前view 类型
         * @param position
         * @return
         */

        @Override
        public int getItemViewType(int position) {
           int type = 1;
          if(position==0 ||position==userapps.size()+1){
           type =0;
          }else{
              type =1;
          }
            return type;
        }

        @Override
        public int getCount() {
            return userapps.size()+systemapps.size();
        }

        @Override
        public AppInfo getItem(int position) {
            if(position==0 || position==userapps.size()+1){
                return null;
            }
            if(position<userapps.size()+1){
                return userapps.get(position-1);
            }else{
                return systemapps.get(position-userapps.size()-2);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int viewType = getItemViewType(position);
            if (viewType == 0) {
                ViewTitleHolder holder = null;
                if (convertView == null) {
                    holder = new ViewTitleHolder();
                    convertView = View.inflate(getApplicationContext(), R.layout.item_application_title, null);
                    holder.title = (TextView) convertView.findViewById(R.id.tv_apptypetitle);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewTitleHolder) convertView.getTag();
                }
                if(position==0){
                    holder.title.setText("用户应用("+userapps.size()+")");
                }else {
                    holder.title.setText("系统应用("+systemapps.size()+")");
                }
            } else {
                ViewHolder holder = null;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = View.inflate(getApplicationContext(), R.layout.item_application, null);
                    holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    holder.title = (TextView) convertView.findViewById(R.id.tv_title);
                    holder.type = (TextView) convertView.findViewById(R.id.tv_apptype);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.icon.setImageDrawable(getItem(position).icon);
                holder.title.setText(getItem(position).getPackgename());
                if (getItem(position).isUserApp) {
                    holder.type.setText("手机应用");
                } else {
                    holder.type.setText("系统应用");
                }

            }
            return convertView;
        }
    }

    /**
     * 显示弹窗
     */
    private void showPopupwindow(View view) {
//        Log.i("显示弹窗","位置:"+position);
        //0.设置布局
        final View inflate = View.inflate(this, R.layout.popup_appinfo, null);
        //1.初始化popupwindow对象
        popupWindow = new PopupWindow(inflate, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,true);
        //2.设置背景
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        //3.显示弹窗
        popupWindow.showAsDropDown(view,50,-view.getHeight()-15);
        //4.设置动画效果
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);

        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1,0,1, Animation.RELATIVE_TO_PARENT,0f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);

        //动画集合
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(alphaAnimation);
        set.addAnimation(scaleAnimation);
        //运行动画
        inflate.startAnimation(set);
        //5.设置点击事件
        TextView uninstall = (TextView) inflate.findViewById(R.id.tv_uninstall);
        TextView startapp = (TextView) inflate.findViewById(R.id.tv_startapp);
        TextView shareapp = (TextView) inflate.findViewById(R.id.tv_shareapp);
        /**
         * 删除app
         */
        uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentAppInfo==null){
                    return;
                }
                if(currentAppInfo.isUserApp){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DELETE);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:"+currentAppInfo.packgename));
                    startActivityForResult(intent,0);
                }else{
                    ToastUtils.show(getApplicationContext(),"无法卸载系统应用程序");
                }
            }
        });
        /**
         * 启动app
         */
        startapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm =  getPackageManager();
                Intent launchIntentForPackage = pm.getLaunchIntentForPackage(currentAppInfo.getPackgename());
                startActivityForResult(launchIntentForPackage,1);
            }
        });
        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "分享给你一个很好的应用哦! 下载地址: https://play.google.com/apps/details?id="
                                + currentAppInfo.packgename);
                startActivityForResult(intent,3);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //刷新app列表
        loadapplists();
        popupWindow.dismiss();
        super.onActivityResult(requestCode, resultCode, data);

    }

    static class ViewHolder{
        ImageView icon ;
        TextView title ;
        TextView  type ;
    }

    static class ViewTitleHolder{
        TextView title;
    }
}
