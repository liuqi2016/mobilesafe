package event.study.liuqi.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import event.study.liuqi.mobilesafe.R;
import event.study.liuqi.mobilesafe.utils.ConstansValue;
import event.study.liuqi.mobilesafe.utils.SpUtils;
import event.study.liuqi.mobilesafe.view.SettingItemView;
/**
 * Created by liuqi on 2016/10/25.
 */
public class SettingActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initUpdate();
    }
    /**
     * 版本更新开关
     */
    private void initUpdate() {
        final SettingItemView update_set = (SettingItemView) findViewById(R.id.update_set);
        //获取sp中open_update的状态
         Boolean open_update = SpUtils.getboolean(this, ConstansValue.OPEN_UPDATE, false);
        //设置内容
        update_set.setCheck(open_update);
        update_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果之前是选中的，点击后变成未选中
                //如果之前是未选中，点击后变成选中
                //获取之前选中的状态
                boolean ischeck = update_set.isCheck();
                //取反
                update_set.setCheck(!ischeck);
                //保存数据到sp中,取反后保存
                SpUtils.putboolean(SettingActivity.this,ConstansValue.OPEN_UPDATE,!ischeck);
            }
        });
    }
}
