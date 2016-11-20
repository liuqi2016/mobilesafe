package event.study.liuqi.mobilesafe.activity;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.SpUtils;
/**
 * 归属地位置
 * Created by liuqi on 2016/11/5.
 */
public class AddressPlaceActivity extends Activity {

    private float startX;
    private float startY;
    private String tag = "AddressPlaceActivity";
    private int widthPixels;
    private int heightPixels;
    private RelativeLayout.LayoutParams layoutParams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressplace);
        //获取屏幕宽高
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
        intiUI();
    }

    /**
     * 初始化ui
     */
    private void intiUI() {
        final ImageView iv_click_two = (ImageView) findViewById(R.id.iv_click_two);
        final Button bt_top = (Button) findViewById(R.id.bt_top);
        final Button bt_bottom = (Button) findViewById(R.id.bt_bottom);
        //初始化显示位置
        int LocationX = SpUtils.getInt(getApplicationContext(), ConstansValue.LOCATION_X, 0);
        int LocationY = SpUtils.getInt(getApplicationContext(), ConstansValue.LOCATION_Y, 0);
        //左上角坐标作用在iv_drag上
        //iv_drag在相对布局中,所以其所在位置的规则需要由相对布局提供
        //指定宽高都为WRAP_CONTENT
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //将左上角的坐标作用在iv_drag对应规则参数上
        layoutParams.leftMargin=LocationX;
        layoutParams.topMargin=LocationY;
        //将以上规则作用在iv_drag上
        iv_click_two.setLayoutParams(layoutParams);
        if (LocationY > heightPixels / 2) {
            bt_top.setVisibility(View.VISIBLE);
            bt_bottom.setVisibility(View.INVISIBLE);
        } else {
            bt_top.setVisibility(View.INVISIBLE);
            bt_bottom.setVisibility(View.VISIBLE);
        }
        iv_click_two.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getRawX();
                        startY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float stopX = event.getRawX();
                        float stopY = event.getRawY();
                        //偏移量
                        int offsetX = (int) (stopX - startX);
                        int offsetY = (int) (stopY - startY);
                        //当前控件在屏幕的位置
                        int left = iv_click_two.getLeft() + offsetX;
                        int top = iv_click_two.getTop() + offsetY;
                        int right = iv_click_two.getRight() + offsetX;
                        int bottom = iv_click_two.getBottom() + offsetY;
                        if (left < 0 || top < 0 || right > widthPixels || bottom > heightPixels - 22) {
                            return true;
                        }
                        if (top > heightPixels / 2) {
                            bt_top.setVisibility(View.VISIBLE);
                            bt_bottom.setVisibility(View.INVISIBLE);
                        } else {
                            bt_top.setVisibility(View.INVISIBLE);
                            bt_bottom.setVisibility(View.VISIBLE);
                        }
                        iv_click_two.layout(left, top, right, bottom);
                        startX = stopX;
                        startY = stopY;
                        break;
                    case MotionEvent.ACTION_UP:
                        SpUtils.putInt(getApplicationContext(), ConstansValue.LOCATION_X, iv_click_two.getLeft());
                        SpUtils.putInt(getApplicationContext(), ConstansValue.LOCATION_Y, iv_click_two.getTop());
                        break;
                }
                return false;
            }
        });
        iv_click_two.setOnClickListener(new View.OnClickListener() {
            long[] mHins = new long[2];
            @Override
            public void onClick(View v) {
                //双击事件
                //将数组所有元素左移一个位子
                //1.src:源数组;2.srcPos:源数组要复制的起始位置;3.dest:目的数组;4.destPos:目的数组放置的起始位置5.length:复制的长度
                System.arraycopy(mHins, 1, mHins, 0, mHins.length - 1);
                //获取当前系统的启动时间
                mHins[mHins.length - 1] = SystemClock.uptimeMillis();
                Log.i(tag, "点击了。。。。。");
                if (SystemClock.uptimeMillis() - mHins[0] < 500) {
                    int left = widthPixels / 2 - iv_click_two.getWidth() / 2;
                    int top = heightPixels / 2 - iv_click_two.getHeight() / 2;
                    int rigth = widthPixels / 2 + iv_click_two.getWidth() / 2;
                    int bottom = heightPixels / 2 + iv_click_two.getHeight() / 2;
                    iv_click_two.layout(left,top,rigth,bottom);
                    SpUtils.putInt(getApplicationContext(), ConstansValue.LOCATION_X,left);
                    SpUtils.putInt(getApplicationContext(), ConstansValue.LOCATION_Y,top);
                }

            }
        });
    }
}
