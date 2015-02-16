package com.bainiaohe.dodo.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.fragments.info.InfoFragment;
import com.bainiaohe.dodo.main.fragments.menu.MenuFragment;
import com.bainiaohe.dodo.publish_info.PublishInfoActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG = "MainActivity";
    /**
     * 当前显示的content fragment
     */
    private Fragment contentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//TODO 判断bundle是否为空
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.publish_info) {
            Intent intent = new Intent(this, PublishInfoActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

