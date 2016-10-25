package event.study.liuqi.mobilesafe.activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.utils.StreamUtil;
import event.study.liuqi.mobilesafe.utils.ToastUtils;

public class SplashActivity extends AppCompatActivity {

    private TextView tv;
    private PackageManager pm;
    private String tag="SplashActivity";
    //版本code
    private int mversionCode;
    private Message msg;
    //更新状态码
    private static final int UPDATE_VERSION = 100;
    //进入应用状态码
    private  static final int INTOACTIVITY=101;
    private static final int JSONERROR=102;
    private static final int IOERROR=103;
    private Handler mHandler = new Handler(){
        //接收消息
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_VERSION:
                    //发送更新
                    showUpdateDialog();
                    break;
                case INTOACTIVITY:
                    enterHome();
                    break;
                case JSONERROR:
                    ToastUtils.show(getApplicationContext(),"JSON異常");
                    enterHome();
                    break;
                case IOERROR:
                    ToastUtils.show(getApplicationContext(),"网络异常");
                    enterHome();
                    break;
            }
        }
        /**
         * 弹出对话框，提示用户更新
         */
        private void showUpdateDialog() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("版本更新");
            builder.setMessage(versionDes);
            builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.i(tag,"选择更新。。。。。");
                    downloadFile();
                }
            });
            builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    enterHome();
                }
            });
            //用户点击取消按钮
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
               enterHome();
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
        /**
         * 下载文件
         */
        private void downloadFile() {

            //1.判断sd卡是否可用，是否挂在上
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
               //2.获取sd卡的路径  File.separator == \
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() +File.separator + "mobilesafe.apk";
                //3.发送请求，传递参数 ①下载路径②保存路径③断点续传④自动重命名⑤请求状态
                HttpUtils httpUtils = new HttpUtils();
                 httpUtils.download(downloadUrl,path,new RequestCallBack<File>(){
                    //开始下载
                    @Override
                    public void onStart() {
                        Log.i(tag,"开始下载");
                        super.onStart();
                    }
                    //下载中
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        Log.i(tag,"下载中...");
                        Log.i(tag,"total:"+total);
                        Log.i(tag,"current:"+current);
                        super.onLoading(total, current, isUploading);
                    }
                    //下载成功
                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                    Log.i(tag,"下载成功");
                       File file = responseInfo.result;
                        //提示用户安装文件
                        installApk(file);
                    }
                    //下载失败
                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.e(tag,"下载失败");
                    }
                });
            };
        }
    };
    private RelativeLayout rl_root;


    /**
     * 安装apk
     */
    private void installApk(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        startActivityForResult(intent,0);
    }
    //开启一个activity 返回结果调用的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String versionDes;
    private String downloadUrl;

    /**
     * 进入应用主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //初始化UI
        initUi();
        //初始化数据
        initData();
        //初始化动画
        initAnimation();
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        rl_root.startAnimation(alphaAnimation);

    }

    private void initData() {
        //1.获取包管理者
        pm = getPackageManager();
        //2.获取本地应用基本信息 0
        getVersionCode();
        //3.检测版本号
        checkVersion();

    }

    /**
     * 检测版本号
     */
    private void checkVersion() {
        new Thread(){
            @Override
            public void run() {
                //发送请求，获取json连接地址
                long startTime =  System.currentTimeMillis();
                try {
                    //1.封装URL地址
                    URL url = new URL("http://10.0.2.2/mobilesafe/update.json");
                    //2.开启连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3.设置常见请求参数
                    //请求超时
                    connection.setConnectTimeout(2000);
                    //连接超时
                    connection.setReadTimeout(2000);
                    //默认为get请求
                    connection.setRequestMethod("GET");
                    //4.请求成功的响应码
                    msg = Message.obtain();
                    if(connection.getResponseCode() == 200){
                        //5以流的形式，将数据取下来
                        InputStream is = connection.getInputStream();
                        //6.将流转换成字符串(工具类封装)
                        String json = StreamUtil.streamToString(is);
                        Log.i(tag,json);
                        //7.json的解析
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String versionName = jsonObject.getString("versionName");
                            versionDes = jsonObject.getString("versionDes");
                            String versionCode = jsonObject.getString("versionCode");
                            downloadUrl = jsonObject.getString("downloadUrl");
                            //debug
                            Log.i(tag,versionName);
                            Log.i(tag, versionDes);
                            Log.i(tag,versionCode);
                            Log.i(tag, downloadUrl);
                            //8.对比版本code
                            if(mversionCode < Integer.parseInt(versionCode)){
                                //发送更新消息
                                msg.what = UPDATE_VERSION;
                            }else{
                                msg.what = INTOACTIVITY;
                                //进入应用主界面
                            }
                        } catch (JSONException e) {
                            msg.what = JSONERROR;
                            e.printStackTrace();
                        }
                    }
                } catch (java.io.IOException e) {
                    msg.what = IOERROR;
                    e.printStackTrace();
                }finally {
                    //判断网络请求事件，指定睡眠事件
                    long endtime = System.currentTimeMillis();
                    if(endtime-startTime<4000){
                        try {
                            Thread.sleep(4000-(endtime-startTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
                super.run();
            }
        }.start();
    }

    private void getVersionCode() {
        //1.获取本地信息
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            mversionCode = info.versionCode;
            tv.setText(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *初始化UI
     */
    private void initUi() {
        tv = (TextView) findViewById(R.id.tv_version_name);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);

    }
}
