package event.study.liuqi.mobilesafe;

import android.test.AndroidTestCase;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import event.study.liuqi.mobilesafe.db.dao.BlackNumberDao;
import event.study.liuqi.mobilesafe.db.domain.AppInfo;
import event.study.liuqi.mobilesafe.db.domain.BlackNumberInfo;
import event.study.liuqi.mobilesafe.engine.AppInfoProvider;

/**
 * Created by liuqi on 2016/11/10.
 */

public class TestClass extends AndroidTestCase {
    public void testinsert(){
        BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
        for(int i=0;i<100;i++){
            if(i<=10){
                dao.insert("1800000000"+i, (int) (Math.random()*3+1));
            }else{
                dao.insert("180000000"+i, (int) (Math.random()*3+1));
            }
        }
        dao.insert("100",1);
    };
    public void testdelect(){
        BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
        dao.delect("100");
    };
    public void  testquery(){
        BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
        List<BlackNumberInfo> all = dao.findAll();
        Log.i("测试", String.valueOf(all));
    };
    public void testLog(){
        Log.i("Log","显示log");
    }
    public void testApplicationlist(){
        ArrayList<AppInfo> appInfos = AppInfoProvider.getAppInfos(getContext());
        Log.i("所有安装程序", String.valueOf(appInfos));
    }
}
