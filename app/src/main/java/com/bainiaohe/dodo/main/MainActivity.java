package com.bainiaohe.dodo.main;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.fragments.friends.FriendsFragment;
import com.bainiaohe.dodo.main.fragments.info.InfoFragment;
import com.bainiaohe.dodo.main.fragments.messages.MessageFragment;
import com.bainiaohe.dodo.main.fragments.personal_center.PersonalCenterFragment;
import com.bainiaohe.dodo.publish_info.PublishInfoActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";

    private LinearLayout titleBar = null;

    /**
     * Fragments
     */
    private List<Fragment> fragments = new ArrayList<Fragment>(4);

    private ArrayList<Integer> tabs = new ArrayList<Integer>(4);
    private ArrayList<Integer> tabTexts = new ArrayList<Integer>(4);

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setContentView(R.layout.activity_main);

        init();
    }

    /**
     * onCreate时初始化
     */
    private void init() {

        //设置header bar
        titleBar = (LinearLayout) findViewById(R.id.title_bar);
//        ((ImageView) (titleBar.findViewById(R.id.back))).setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
        ((TextView) titleBar.findViewById(R.id.title)).setText(getText(R.string.app_name));
        ImageView publishInfoButton = new ImageView(this, null, R.style.extreme_small_picture);
        publishInfoButton.setImageDrawable(getResources().getDrawable(R.drawable.add_photo));
        publishInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PublishInfoActivity.class));//启动 发表info 页面
            }
        });
        ((LinearLayout) titleBar.findViewById(R.id.action_container)).addView(publishInfoButton);


        this.fragments.add(new InfoFragment());
        this.fragments.add(new MessageFragment());
        this.fragments.add(new FriendsFragment());
        this.fragments.add(new PersonalCenterFragment());

        this.tabs.add(R.id.tab_info);
        this.tabs.add(R.id.tab_message);
        this.tabs.add(R.id.tab_friends);
        this.tabs.add(R.id.tab_personal_center);

        this.tabTexts.add(R.id.info_textView);
        this.tabTexts.add(R.id.message_textView);
        this.tabTexts.add(R.id.friends_textView);
        this.tabTexts.add(R.id.personal_center_textView);

        onTabSelected(0);
    }


    /**
     * 点击tab时调用
     *
     * @param v
     */
    public void onTabClick(View v) {
        onTabSelected(tabs.indexOf(v.getId()));
    }

    /**
     * 当页面被选中时调用
     * 在xml中绑定点击事件
     *
     * @param tabIndex
     */
    private void onTabSelected(int tabIndex) {
        for (int i = 0; i < tabs.size(); i++) {
            if (i != tabIndex) {
                findViewById(tabs.get(i)).setSelected(false);
                ((TextView) findViewById(tabTexts.get(i))).setTextColor(getResources().getColor(R.color.default_text_color));
            }
        }
        if (tabIndex >= 0 && tabIndex < tabs.size()) {
            findViewById(tabs.get(tabIndex)).setSelected(true);
            ((TextView) findViewById(tabTexts.get(tabIndex))).setTextColor(getResources().getColor(R.color.blue));
        }

        //set fragment
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, this.fragments.get(tabIndex));
//        transaction.addToBackStack(null);
//        transaction.commit();

        for (int i = 0; i < this.fragments.size(); i++) {
            if (i != tabIndex) {
                if (this.fragments.get(i).isAdded())
                    transaction.hide(this.fragments.get(i));
            }
        }

        if (this.fragments.get(tabIndex).isAdded())
            transaction.show(this.fragments.get(tabIndex));
        else
            transaction.add(R.id.fragment_container, this.fragments.get(tabIndex));

        transaction.commit();

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}

