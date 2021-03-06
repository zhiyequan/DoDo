package com.bainiaohe.dodo.add_friends;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.utils.AddFriendsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;

/**
 * Created by liuxiaoran on 2015/2/3.
 */
public class SearchFriends extends Activity {
    private ImageView ivDeleteText;
    private EditText etSearch;
    private ListView searchListView;
    private static final String TAG = "search_friends";
    private Button searchButton;

    //电话联系人的电话--姓名
    private HashMap phoneToName;

    //包含输入数字的联系人的电话
    private ArrayList<String> retPhone;

    //包含输入数字的联系人的姓名
    private ArrayList<String> retName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
        etSearch = (EditText) findViewById(R.id.etSearch);
        searchListView = (ListView) findViewById(R.id.search_friends_listview);
        phoneToName = new HashMap();
        retPhone = new ArrayList<String>();
        retName = new ArrayList<String>();

        searchButton= (Button) findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFriendsService.getUserInfo(etSearch.getText().toString().trim(),SearchFriends.this);

            }
        });
        //得到手机联系人的信息
        getPhoneContacts();
        Log.v(TAG, "" + phoneToName.size());


        ivDeleteText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                etSearch.setText("");
            }
        });


        //listview 绑定adapter
        final ListViewAdapter adapter = new ListViewAdapter(this);
        searchListView.setAdapter(adapter);

        //监听listview的每个Item的点击
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //点击之后看对应人的资料，可以添加好友
                ViewHolder holder = (ViewHolder) view.getTag();
                AddFriendsService.getUserInfo(holder.personAccount.getText().toString().trim(),SearchFriends.this);
                Intent intent = new Intent(SearchFriends.this, PersonProfile.class);
                startActivity(intent);
            }
        });

        //监听搜索框
        etSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivDeleteText.setVisibility(View.GONE);
                } else {
                    ivDeleteText.setVisibility(View.VISIBLE);
                    changeRetArraylist(s.toString());
                    //改变retPhone retName 之后通知adpater数据源改变
                    adapter.notifyDataSetChanged();

                }
            }
        });
    }

    class ListViewAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public ListViewAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return retName.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.friends_result_listview, null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.portrait = (ImageView) convertView.findViewById(R.id.portrait_iv);

                holder.nameText = (TextView) convertView.findViewById(R.id.name_tv);
                holder.personAccount = (TextView) convertView.findViewById(R.id.person_account_tv);

                convertView.setTag(holder); //绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag(); //取出ViewHolder对象
            }

            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.nameText.setText(retName.get(position));
            holder.personAccount.setText(retPhone.get(position));
            holder.portrait.setImageResource(R.drawable.default_contact);

            return convertView;
        }
    }

    /*存放控件 的ViewHolder*/
    public final class ViewHolder {
        public ImageView portrait;
        public TextView nameText;
        public TextView personAccount;
    }

    /**
     * 将包含EditText输入的字符的联系人的电话姓名写入响应的Arraylist
     *
     * @param inputNumber 输入的字符
     */
    private void changeRetArraylist(String inputNumber) {

        Iterator iterator = phoneToName.keySet().iterator();
        while (iterator.hasNext()) {

            String contactPhone = (String) iterator.next();
            String contactName = (String) phoneToName.get(contactPhone);


            if (contactPhone.contains(inputNumber)) {

                retPhone.add(contactPhone);
                retName.add(contactName);
            }
        }

    }

    /**
     * 得到手机通讯录联系人信息*
     */
    private void getPhoneContacts() {
        //得到contentresolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标，必须先moveToNext()
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String string="";
        String name;
        String contact_phone="";
        Log.v(TAG,cr.toString());
        while (cursor.moveToNext()) {
            //取得联系人的名字索引
            Log.v(TAG,"arrivein");
            int nameIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameIndex);
            string += (contact + ":" + "/n");
            name=contact;
            //取得联系人的ID索引值
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //查询该位联系人的电话号码，类似的可以查询email，photo
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);//第一个参数是确定查询电话号，第三个参数是查询具体某个人的过滤值
            //一个人可能有几个号码
            while (phone.moveToNext()) {
                String strPhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contact_phone=strPhoneNumber;
                string += (strPhoneNumber + "/n");
            }
            phoneToName.put(name,contact_phone);
            phone.close();
        }
        cursor.close();

    }
}


