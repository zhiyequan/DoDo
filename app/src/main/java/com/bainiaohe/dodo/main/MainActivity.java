package com.bainiaohe.dodo.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.fragments.info.InfoFragment;
import com.bainiaohe.dodo.main.fragments.menu.MenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG = "MainActivity";


//    public void init(Bundle bundle) {
//        allowArrowAnimation();
//        //TODO 设置只有首次打开时显示
//        //disableLearningPattern();
//
//        User currentUser = DoDoContext.getInstance().getCurrentUser();
//
//        //TODO 设置头像和背景
//        //设置account
//        if (currentUser != null) {
//            MaterialAccount account = new MaterialAccount(this.getResources(), currentUser.getName(), currentUser.getEmail(), R.drawable.photo, R.drawable.bamboo);
//            addAccount(account);
//        }
//        //添加sections
//        addSection(newSection(getText(R.string.section_info).toString(), new InfoFragment()));
//        addSection(newSection(getText(R.string.section_internship).toString(), new InternshipFragment()));
//
//        addSubheader(getText(R.string.sub_header_friends).toString());
//        addSection(newSection(getText(R.string.section_friends).toString(), R.drawable.tab_friends, new FriendsFragment()));
//        addSection(newSection(getText(R.string.section_messages).toString(), R.drawable.tab_message, new MessageFragment()));
//        addSection(newSection(getText(R.string.section_discovery).toString(), new MessageFragment()));//TODO 添加图标
//        addDivisor();
//
//        //添加bottom section
//        addBottomSection(newSection(getText(R.string.section_settings).toString(), R.drawable.settings, new Intent(this, SettingActivity.class)));
//
//    }


    private Fragment contentFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG, "Unpressed#" + Integer.toHexString(0x00FFFFFF));
        Log.e(TAG, "Selected#" + Integer.toHexString(0x0A000000));

        // 设置是否能够使用ActionBar来滑动
        setSlidingActionBarEnabled(true);
        // 初始化sliding menu
        initSlidingMenu(savedInstanceState);

    }

    /**
     * 初始化Sliding Menu
     *
     * @param savedInstanceState
     */
    private void initSlidingMenu(Bundle savedInstanceState) {
        // 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
        if (savedInstanceState != null) {
            contentFragment = getSupportFragmentManager().getFragment(
                    savedInstanceState, "contentFragment");
        }
        if (contentFragment == null) {
            contentFragment = new InfoFragment();
        }
        // 设置主界面视图
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame, contentFragment, contentFragment.getClass().getSimpleName())
                .commit();

        // 设置滑动菜单的视图
        setBehindContentView(R.layout.container_menu);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.menu_frame, new MenuFragment(), MenuFragment.class.getSimpleName())
                .commit();

        // 实例化滑动菜单对象
        SlidingMenu menu = getSlidingMenu();
        // 设置滑动阴影的宽度
        menu.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动阴影的图像资源
        menu.setShadowDrawable(R.drawable.sliding_menu_shadow);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }

    /**
     * 切换content，并关闭menu
     *
     * @param targetFragment
     */
    public void switchContent(Fragment targetFragment) {
        if (contentFragment.getClass() != targetFragment.getClass()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (targetFragment.isAdded())
                transaction.show(
                        getSupportFragmentManager().findFragmentByTag(targetFragment.getClass().getSimpleName())
                );
            else
                transaction.add(R.id.content_frame, targetFragment, targetFragment.getClass().getSimpleName());

            transaction.hide(contentFragment);
            transaction.commit();

            contentFragment = targetFragment;
            showContent();
        }
    }

}

