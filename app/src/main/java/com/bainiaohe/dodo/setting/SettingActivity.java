package com.bainiaohe.dodo.setting;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.login.LoginActivity;
import com.bainiaohe.dodo.main.MainActivity;

public class SettingActivity extends ActionBarActivity {
    private Button zhuxiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
        initListen();
    }

    public void initView() {
        zhuxiao = (Button) findViewById(R.id.zhuxiao);

    }

    public void initListen() {
        zhuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.getSharedPreferences("user", MODE_PRIVATE).edit().clear();
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
