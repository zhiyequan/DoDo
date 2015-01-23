package com.bainiaohe.dodo.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.utils.UserService;

/**
 * Created by xiaoran on 2015/1/19.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
    private int otherplatformType=0;
    private String otherplatformId;
    private TextView register_tv;
    private EditText phone_et,pw_et;
    //    private Button validate;
    private Button register_btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Intent intent=getIntent();
        otherplatformType=intent.getIntExtra("otherplatformType",0);
        otherplatformId=intent.getStringExtra("otherplatformId");
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
        if (phone==null || pw==null ){
            Toast.makeText(this, "电话或者密码不能为空", Toast.LENGTH_LONG).show();
        }else if(pw.length()<6 ||pw.length()>16){
            Toast.makeText(this,"密码长度不合适，注意是6-16位之间",Toast.LENGTH_LONG).show();
        }else{
            boolean ret= UserService.userRegister(phone, pw, otherplatformType, otherplatformId);
            if (ret){
                //注册成功
                //TODO 进入系统

            }else{
                Toast.makeText(this,UserService.registerErrorMessage,Toast.LENGTH_LONG).show();

            }
        }
    }
}
