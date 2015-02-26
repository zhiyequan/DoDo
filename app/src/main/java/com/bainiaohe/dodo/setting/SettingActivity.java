package com.bainiaohe.dodo.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.login.LoginActivity;


public class SettingActivity extends Activity implements View.OnClickListener {
    private Button logout;
    private Button backset;
    private RelativeLayout newmesset, nodisturbset, chatset, privacyset, normalset, safetyset, aboutset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
        initListen();
    }

    public void initView() {
        logout = (Button) findViewById(R.id.zhuxiao);
        backset = (Button) findViewById(R.id.backset);
        newmesset = (RelativeLayout) findViewById(R.id.newmesset);
        nodisturbset = (RelativeLayout) findViewById(R.id.nodisturbset);
        chatset = (RelativeLayout) findViewById(R.id.chatset);
        privacyset = (RelativeLayout) findViewById(R.id.privacyset);
        normalset = (RelativeLayout) findViewById(R.id.normalset);
        safetyset = (RelativeLayout) findViewById(R.id.safetyset);
        aboutset = (RelativeLayout) findViewById(R.id.aboutset);
    }

    public void initListen() {
        logout.setOnClickListener(this);
        backset.setOnClickListener(this);
        newmesset.setOnClickListener(this);
        nodisturbset.setOnClickListener(this);
        chatset.setOnClickListener(this);
        privacyset.setOnClickListener(this);
        normalset.setOnClickListener(this);
        safetyset.setOnClickListener(this);
        aboutset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhuxiao:
                SettingActivity.this.getSharedPreferences("user", MODE_PRIVATE).edit().clear();
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.backset:
                SettingActivity.this.finish();
                break;
            case R.id.newmesset:
                startActivity(new Intent(SettingActivity.this, NewmesActivity.class));
                break;
            case R.id.nodisturbset:
                startActivity(new Intent(SettingActivity.this, NodisturbActivity.class));
                break;
            case R.id.chatset:
                startActivity(new Intent(SettingActivity.this, ChatActivity.class));
                break;
            case R.id.privacyset:
                startActivity(new Intent(SettingActivity.this, PrivacyActivity.class));
                break;
            case R.id.normalset:
                startActivity(new Intent(SettingActivity.this, NormalActivity.class));
                break;
            case R.id.safetyset:
                startActivity(new Intent(SettingActivity.this, SafetyActivity.class));
                break;
            case R.id.aboutset:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
        }
    }
}
