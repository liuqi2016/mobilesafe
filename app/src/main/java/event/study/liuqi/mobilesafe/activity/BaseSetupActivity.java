package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * setUp页面的基类
 * Created by liuqi on 2016/10/31.
 */

public  abstract class BaseSetupActivity extends Activity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector=new GestureDetector(this,new MyGestureDetector());
    }
   //抽象方法方式，定义跳转到下一页的方法
    public abstract void showNextPage();
    //抽象方法方式，定义跳转到上一页的方法
    public abstract void showPrePage();
    public void nextPage(View view){
        showNextPage();
    }
    public void prePage(View view){
        showPrePage();
    }
   class MyGestureDetector extends GestureDetector.SimpleOnGestureListener{
       @Override
       public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
           //e1位按下左边，e2为按起坐标,x,y为速度
           float startX = e1.getX();
           float stopX = e2.getX();
           if(startX-stopX>100){
               //下一页，抽象方法，让子类实现
               showNextPage();
           }
           if(stopX-startX>100){
               //上一页，抽象方法，让子类实现
               showPrePage();
           }
           return super.onFling(e1, e2, velocityX, velocityY);
       }
   }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //注册事件
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
