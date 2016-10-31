package event.study.liuqi.mobilesafe.activity;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import event.study.liuqi.mobilesafe.R;
/**
 * Created by liuqi on 2016/10/30.
 */
public class SetupContactsActivity extends Activity {

    private ListView ll_contacts;
    private String tag = "SetupContactsActivity";
    List<HashMap<String, String>> contacts = new ArrayList<HashMap<String, String>>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            myadapter = new Myadapter();
            ll_contacts.setAdapter(myadapter);
        }
    };
    private Myadapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_contacts);
        initUi();
        initdata();
    }

    /**
     * 初始化数据
     */
    private void initdata() {
        //耗时操作，放在子线程
        new Thread() {
            @Override
            public void run() {
                //1.获取联系人数据
                Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                //2.遍历循环联系人
                contacts.clear();
                while (cursor.moveToNext()) {
                    HashMap<String, String> hashmap = new HashMap<>();
                    //获取联系人id
                    String contatctId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    //获取联系人名字
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //获取联系人电话
                    hashmap.put("name", name);
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contatctId, null, null);
                    //便利查找联系人号码
                    String phoneNumber ="";
                    while (phones.moveToNext()) {
                         phoneNumber += phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "")+" ";
                    }
                    hashmap.put("number", phoneNumber);
                    //拼装数组
                    phones.close();
                    contacts.add(hashmap);
                }
                cursor.close();
                mHandler.sendEmptyMessage(0);
            }

        }.start();
    }

    /**
     * 初始化ui
     */
    private void initUi() {
        ll_contacts = (ListView) findViewById(R.id.ll_contacts);
        ll_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //1.获取点击条目的索引指向集合中的对象
                if(myadapter!=null){
                    HashMap<String, String> item = myadapter.getItem(i);
                    //2.获取指定条目上的电话号码
                    String number = item.get("number");
                    //3.返回上一个界面，并将数据返回
                    Intent intent = new Intent();
                    intent.putExtra("number",number);
                    setResult(0,intent);
                    finish();
                }
            }
        });
    }

    class Myadapter extends BaseAdapter {
        @Override
        public int getCount() {
            return contacts.size();
        }

        @Override
        public HashMap<String, String> getItem(int i) {
            return contacts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = View.inflate(getApplicationContext(), R.layout.contacts_item, null);
            TextView contatct_name = (TextView) view1.findViewById(R.id.contact_name);
            TextView contatct_number = (TextView) view1.findViewById(R.id.contact_number);
            contatct_name.setText(getItem(i).get("name"));
            contatct_number.setText(getItem(i).get("number"));
            return view1;
        }
    }
}
