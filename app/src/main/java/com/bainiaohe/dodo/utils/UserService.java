package com.bainiaohe.dodo.utils;

import android.content.SharedPreferences;
import android.util.Log;

import com.bainiaohe.dodo.login.LoginActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaoran on 2015/1/22.
 */
public class UserService {
    public static final String TAG = "UserService";
    public static String userId;
    public static boolean isNew;
    public static String registerErrorMessage;
    public static void writeMapIntoSp(HashMap<String,String> map){
        SharedPreferences.Editor editor= LoginActivity.sharedPreferences.edit();
        Iterator iterator = map.keySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry entry= (Map.Entry) iterator.next();
            editor.putString(entry.getKey().toString(),entry.getValue().toString());
        }

        editor.commit();

    }
    public static String userLogin(String userphone, String password) {
        String result = null;
        String ret = "登陆成功";
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URLConstants.LOGIN);
        List params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("phone", userphone));
        params.add(new BasicNameValuePair("password", password));
        try {
            //构造post的表单实体
            UrlEncodedFormEntity form = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            httpPost.setEntity(form);
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
                JSONObject object = new JSONObject(result);
                int status = object.getInt("status");
                Log.v("login","---status: "+status);
                if (status == 0) {
                    ret = "success";
                    userId=object.getString("id");
                    String token =object.getString("token");
                    HashMap map=new HashMap();
                    map.put("id",userId);
                    map.put("token",token);
                    map.put("name",object.getString("name"));
                    map.put("sex",object.getString("sex"));
                    map.put("phone",object.getString("phone"));
                    map.put("email",object.getString("email"));
                    writeMapIntoSp(map);
                }
                else
                    ret = "error";


            } else {
                ret = "error";
            }

        } catch (Exception e) {
            Log.v("login",e.toString());
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 判断其他平台的用户是否注册过
     * 相当第三方平台的用户的登录
     * @param otherplatformId
     * @return
     */
    public static boolean isRegisted(int otherplatformType, String otherplatformId) {

        boolean ret = false;
        String result;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URLConstants.LOGIN);
        List params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("plat_type", Integer.toString(otherplatformType)));
        params.add(new BasicNameValuePair("plat_id", otherplatformId));
        try {
            //构造post的表单实体
            UrlEncodedFormEntity form = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            httpPost.setEntity(form);
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
                JSONObject object = new JSONObject(result);
                int status = object.getInt("status");
                if (status == 1001)
                    ret = false;
                else {
                    ret = true;
                    userId=object.getString("id");
                    String token =object.getString("token");
                    HashMap map=new HashMap();
                    map.put("id",userId);
                    map.put("token",token);
                    map.put("name",object.getString("name"));
                    map.put("sex",object.getString("sex"));
                    map.put("phone",object.getString("phone"));
                    map.put("email",object.getString("email"));
                    writeMapIntoSp(map);
                }


            } else {
                ret = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void registerByAsynchronous(String phone, String plat_type,String plat_id){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("plat_type", plat_type);
        params.put("plat_id", plat_id);
        client.post(URLConstants.REGISTER, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //第三方用户注册成功
                Log.v("UserService","---"+new String(responseBody));
                try {
                    userId=new JSONObject(new String(responseBody)).getString("id");
                    HashMap map=new HashMap();
                    map.put("id",userId);
                    writeMapIntoSp(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v("UserService","failed");
            }
        });
    }

    /**
     * 本平台的用户和其他平台的用户
     * 本平台传两个参数：phone pw
     * 其他平台的传三个参数 phone plat_type plat_id ,目前第三方平台的注册使用的是registerByAsynchronous
     * @param phone
     * @param p
     * @return
     */
    public static boolean userRegister(String phone,  String... p) {
        boolean ret = false;
        String pw="";
        String plat_id="";
        String plat_type="0";
        if (p.length==1){
           //本平台的用户的注册
            pw=p[0];
        }else{
            //其他平台的用户
            plat_type=p[0];
            plat_id=p[1];
        }
        Log.v("UserService", "phone  " + phone);
        Log.v("UserService", "pw  " + pw);
        HttpPost httpPost = new HttpPost(URLConstants.REGISTER);
        List params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("plat_type", plat_type));
        params.add(new BasicNameValuePair("plat_id", plat_id));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("password", pw));
        HttpClient client = new DefaultHttpClient();
        //连接超时
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        //请求超时
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
        try {
            //构造post的表单实体
            UrlEncodedFormEntity form = new UrlEncodedFormEntity(params);
            httpPost.setEntity(form);
            HttpResponse response = client.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                Log.v(TAG, result);
                JSONObject object = new JSONObject(result);
                String message = object.getString("message");
                if (message.equals("success")) {
                    userId = object.getString("id");
                    HashMap map=new HashMap();
                    map.put("id",userId);
                    writeMapIntoSp(map);
                    ret = true;
                } else {
                    registerErrorMessage = object.getString("message");
                    ret = false;
                }
            } else {
                ret = false;
                Log.v("TAG", "wrong");
                registerErrorMessage = "网络不好";
            }

        } catch (Exception e) {
            Log.v("UserService", e.toString());

            e.printStackTrace();
        }
        return ret;
    }

    public static String userInputError;

    public static boolean phonePatternMatch(String str) {
        Pattern pattern = Pattern.compile("[0-9]{11}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches()) {
            userInputError = "电话格式错误 ";
        } else {
            userInputError = "";
        }
        return matcher.matches();
    }

    public static boolean pwPatternMatch(String str) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{6,16}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches()) {
            userInputError += " 密码格式错误";
        }
        return matcher.matches();

    }

}



