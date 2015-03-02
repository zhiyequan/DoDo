package com.bainiaohe.dodo;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;

import com.bainiaohe.dodo.model.User;
import com.sea_monster.core.utils.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.UserInfo;

/**
 * Created by Lewis on 2015/1/30.
 */
public class DoDoContext {

    //查询手机联系人的返回值
    @SuppressLint("InlineApi")
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                            ContactsContract.Contacts.DISPLAY_NAME
            };
    private static final int CONTACT_ID_INDEX = 0;
    private static final int CONTACT_NAME_INDEX = 1;
    private static DoDoContext instance;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private List<UserInfo> mUserInfos;

    public DoDoContext() {
    }

    /**
     * 获得一个DoDoContext 实例
     *
     * @return
     */
    public static DoDoContext getInstance() {

        if (instance == null) {
            instance = new DoDoContext();
        }

        return instance;
    }

    public SharedPreferences getmSharedPreferences() {
        return mSharedPreferences;
    }

    public void setmSharedPreferences(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public void init(Context context) {
        mContext = context;
        //初始化 sharedPreferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }


    /**
     * 当获取数据之后
     *
     * @param userInfos
     */
    public void setFriends(ArrayList<UserInfo> userInfos) {

        this.mUserInfos = userInfos;

        /**
         * 使用融云的imkit接口，所以必须设置好友关系提供者
         * RongIM将调用此Provider 获取好友列表信息
         */
        RongIM.setGetFriendsProvider(new RongIM.GetFriendsProvider() {

            @Override
            public List<UserInfo> getFriends() {
                return mUserInfos;
            }
        });


        /**
         * 设置用户信息提供者.如果在聊天的过程中融云将调用此方法获取用户信息
         */
        RongIM.setGetUserInfoProvider(new RongIM.GetUserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {

                return getUserInfoById(s);
            }
        }, false);
    }


    /**
     * 通过userId 查找在好友列表中特定的人
     */
    public UserInfo getUserInfoById(String userId) {
        UserInfo userForReturn = null;

        if (!userId.equals("") && userId != null && mUserInfos != null) {
            for (UserInfo userInfo : mUserInfos) {
                if (userInfo.getUserId().equals(userId)) {
                    userForReturn = userInfo;
                    break;
                }
            }
        }

        return userForReturn;

    }


    /**
     * 存储当前用户信息
     * 将user 存储到sharedpreference 之后如果需要的话直接通过getCurrentUser获得
     */
    public void saveCurrentUser(User user) {

        if (mSharedPreferences == null)
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(user);

            String userBase64 = new String(Base64.encode(baos.toByteArray()));

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("currentUser", userBase64);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获得当前用户,之前的信息都存储在sharedpreferences里面在
     * 返回的是一个user实例
     */
    public User getCurrentUser() {

        User user = null;
        String userBase64 = mSharedPreferences.getString("currentUser", null);

        try {
            byte[] base64 = Base64.decode(userBase64);

            ByteArrayInputStream bais = new ByteArrayInputStream(base64);
            ObjectInputStream bis = new ObjectInputStream(bais);

            user = (User) bis.readObject();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 发送短信，这里采用的是如果超过字数限制则发送多条短信
     */
    public void sendMessage(String message, String phoneNumber) {

        SmsManager sms = SmsManager.getDefault();

        message = message + " 欢迎使用职圈科技/www.baidu.com";
        ArrayList<String> messages = sms.divideMessage(message);

        sms.sendMultipartTextMessage(phoneNumber, null, messages, null, null);

    }

    /**
     * 获得手机所有联系人 信息包括名字和电话号码
     */
    public ArrayList<HashMap<String, String>> getPhoneContacts() {

        ArrayList<HashMap<String, String>> phoneContacts = new ArrayList<>();

        ContentResolver resolver = mContext.getContentResolver();

        //从数据库中得到手机联系人 查询条件:含有电话号码
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(CONTACT_ID_INDEX);
                String name = cursor.getString(CONTACT_NAME_INDEX);

                //查询每一个的手机号码
                Cursor pCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id}, null);

                while (pCursor.moveToNext()) {
                    HashMap<String, String> contact = new HashMap<>();
                    String phonNumber = getFormatePhoneNumber(pCursor.getString(0));
                    if (phonNumber != null) {
                        contact.put("contactName", name);
                        contact.put("number", phonNumber);

                        phoneContacts.add(contact);
                    }

                }
                pCursor.close();
            }
        }


        return phoneContacts;
    }

    /**
     * 格式化手机号码 暂时只考虑中国的
     *
     * @param number
     * @return
     */
    private String getFormatePhoneNumber(String number) {
        String res = number.replace('-', ' ').replaceAll("\\s", "");
        if (res.length() >= 11) {

            res = res.substring(res.length() - 11);
            return res;

        }
        return null;
    }

}
