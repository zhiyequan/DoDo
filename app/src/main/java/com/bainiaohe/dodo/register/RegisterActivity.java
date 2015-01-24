package com.bainiaohe.dodo.register;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.MainActivity;
import com.bainiaohe.dodo.utils.UserService;

/**
 * Created by xiaoran on 2015/1/19.
 */
public class RegisterActivity extends Activity implements View.OnClickListener{
    private int otherplatformType=0;
    private String otherplatformId="";
    private TextView register_tv;
    private EditText phone_et,pw_et;
    private Button register_btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Intent intent=getIntent();
        int who=intent.getIntExtra("whosend",0);
        if (who==1) {
            //第三方登陆未成功，之后注册的用户
            otherplatformType = intent.getIntExtra("otherplatformType", 0);
            otherplatformId = intent.getStringExtra("otherplatformId");
        }
        else {
            //从欢迎界面进入的注册
            otherplatformType=0;
            otherplatformId="";
        }
        phone_et= (EditText) findViewById(R.id.register_phone_et);
        pw_et= (EditText) findViewById(R.id.register_pw_et);
        register_tv = (TextView) findViewById(R.id.register_protocol);
        register_tv.setText(
                Html.fromHtml(
                        "点击注册表示同意" +
                                "<a href=\"http://www.google.com\">《度度软件许可及服务协议》</a> "));
        register_tv.setMovementMethod(LinkMovementMethod.getInstance());
        register_btn= (Button) findViewById(R.id.register_btn);
        register_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String phone =phone_et.getText().toString();
        String pw=pw_et.getText().toString();
        Log.v("UserService",phone +"  "+pw);
        if (phone.equals("") || pw.equals("")){
            Toast.makeText(this,"电话或者密码不能为空",Toast.LENGTH_LONG).show();
        }else if(UserService.pwPatternMatch(pw)){
            Toast.makeText(this,"密码格式不正确",Toast.LENGTH_LONG).show();
        }else if (UserService.pwPatternMatch(phone)){
            Toast.makeText(this,"手机号格式不正确",Toast.LENGTH_LONG).show();
        }
        else{
            new RegisterTask().execute(phone,pw,otherplatformType,otherplatformId);

        }
    }
    class RegisterTask extends AsyncTask{
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setTitle("请等待");
            dialog.setMessage("Logging");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return  UserService.userRegister(objects[0].toString(),objects[1].toString(),Integer.parseInt(objects[2].toString()),objects[3].toString());
        }
        protected void onPostExecute(Object result) {
            dialog.cancel();
            boolean ret =Boolean.parseBoolean(result.toString());
            if (ret){
                //注册成功
                Intent  intent =new Intent (RegisterActivity.this, MainActivity.class);
                startActivity(intent);

            }else{
                Toast.makeText(RegisterActivity.this,UserService.registerErrorMessage,Toast.LENGTH_LONG).show();

            }

        }
    }
}
