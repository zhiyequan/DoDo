package com.bainiaohe.dodo.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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

    public static String userLogin(String userphone, String password) {
        String result = null;
        String ret = "登陆成功";
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Url.loginUrl);
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
                if (status == 0)
                    ret = "success";
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
     *
     * @param otherplatformId
     * @return
     */
    public static boolean isRegisted(int otherplatformType, String otherplatformId) {

        boolean ret = false;
        String result;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Url.loginUrl);
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
                else
                    ret = true;


            } else {
                ret = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 本平台的用户和其他平台的用户
     * 本平台传两个参数：phone pw
     * 其他平台的传三个参数 phone plat_type plat_id
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
        HttpPost httpPost = new HttpPost(Url.registerUrl);
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



