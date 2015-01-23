package com.bainiaohe.dodo.main;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.fragments.friends.FriendsFragment;
import com.bainiaohe.dodo.main.fragments.messages.MessageFragment;
import com.bainiaohe.dodo.main.fragments.personal_center.PersonalCenterFragment;
import com.bainiaohe.dodo.main.fragments.info.InfoFragment;
import io.rong.imkit.view.ActionBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";


    private ActionBar mAction = null;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        init();
    }

    /**
     * onCreate时初始化
     */
    private void init() {
        this.fragments.add(new InfoFragment());
        this.fragments.add(new MessageFragment());
        this.fragments.add(new FriendsFragment());
        this.fragments.add(new PersonalCenterFragment());

        this.tabs.add(R.id.tab_home);
        this.tabs.add(R.id.tab_monitor);
        this.tabs.add(R.id.tab_bill);
        this.tabs.add(R.id.tab_query);

        this.tabTexts.add(R.id.home_textView);
        this.tabTexts.add(R.id.monitor_textView);
        this.tabTexts.add(R.id.bill_textView);
        this.tabTexts.add(R.id.query_textView);

        onTabSelected(0);

        mAction = (ActionBar) findViewById(R.id.action_bar);
        mAction.getTitleTextView().setText("DoDo");
        ImageView backView = mAction.getBackView();
        backView.setVisibility(View.GONE);
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
                ((TextView) findViewById(tabTexts.get(i))).setTextColor(getResources().getColor(R.color.text_color_default));
            }
        }
        if (tabIndex >= 0 && tabIndex < tabs.size()) {
            findViewById(tabs.get(tabIndex)).setSelected(true);
            ((TextView) findViewById(tabTexts.get(tabIndex))).setTextColor(getResources().getColor(R.color.blue));
        }

        //set fragment
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, this.fragments.get(tabIndex));
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

