package com.bainiaohe.dodo.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoran on 2015/1/22.
 */
public class UserService {

    public static String userId;

    public static String userLogin(String userphone, String password) {
        String result = null;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Url.loginUrl);
        List params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("loginPhone", userphone));
        params.add(new BasicNameValuePair("loginPassowrd", password));
        try {
            //构造post的表单实体
            UrlEncodedFormEntity form = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            httpPost.setEntity(form);
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
            } else {
                result = "error";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断其他平台的用户是否注册过
     *
     * @param otherplatformId
     * @return
     */
    public static boolean isRegisted(String otherplatformId) {
        HttpClient client = new DefaultHttpClient();
        String path = Url.checkOtherplatIsRegistered + "?otherplatformId=" + otherplatformId;
        HttpGet httpGet = new HttpGet(path);
        try {
            HttpResponse httpResponse = client.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            JSONObject object = new JSONObject(result);
            //是注册用户
            if (object.getString("Message").equals("registered")) {
                userId = object.getString("id");
                return true;
            } else {
                //用户未注册
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static String registerErrorMessage;

    /**
     * @param phone
     * @param pw
     * @param otherplatformId 如果不是从第三方，otherplatformId=0
     * @return
     */
    public static boolean userRegister(String phone, String pw, int otherplatformType,String otherplatformId) {
        boolean ret = false;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Url.registerUrl);
        List params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("password", pw));
        params.add(new BasicNameValuePair("otherplatformType",Integer.toString(otherplatformType)));
        params.add(new BasicNameValuePair("otherplatformId", otherplatformId));
        try {
            //构造post的表单实体
            UrlEncodedFormEntity form = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            httpPost.setEntity(form);
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                JSONObject object = new JSONObject(result);
                String message = object.getString("message");
                if (message.equals("success")) {
                    userId = object.getString("id");
                    ret = true;
                } else {
                    registerErrorMessage = object.getString("errorMessage");
                    ret = false;
                }
            } else {
                ret = false;
                registerErrorMessage = "网络状态不好请重试";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
