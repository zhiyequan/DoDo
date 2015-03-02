package com.bainiaohe.dodo.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.fragments.info.InfoFragment;
import com.bainiaohe.dodo.main.fragments.menu.MenuFragment;
import com.bainiaohe.dodo.publish_info.PublishInfoActivity;
import com.bainiaohe.dodo.searchable.SearchableActivity;
import com.bainiaohe.dodo.widgets.MaterialMenuDrawable;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG = "MainActivity";
    /**
     * 当前显示的content fragment
     */
    private Fragment contentFragment;

    private MaterialMenuDrawable materialMenuDrawable = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//TODO 判断bundle是否为空
        // 设置是否能够使用ActionBar来滑动
        setSlidingActionBarEnabled(true);
        // 初始化sliding menu
        initSlidingMenu(savedInstanceState);

        materialMenuDrawable = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        //设置Action Bar Icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(materialMenuDrawable);

        //set custom view
//        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示title
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setCustomView(R.layout.action_bar_custom_view);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

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

        //根据menu状态animate state
        menu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                materialMenuDrawable.animateIconState(MaterialMenuDrawable.IconState.BURGER, true);
            }
        });
        menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                materialMenuDrawable.animateIconState(MaterialMenuDrawable.IconState.ARROW, true);
            }
        });
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

    /**
     * 设置通知数量
     * 当数量为0时，自动隐藏通知气泡
     *
     * @param notificationCount
     */
    public void setNotificationCount(int notificationCount) {
        materialMenuDrawable.setNotificationCount(notificationCount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //set icon
        menu.findItem(R.id.publish_info).setIcon(
                new IconDrawable(this, Iconify.IconValue.fa_pencil_square_o)
                        .color(Color.WHITE)
                        .actionBarSize());
        menu.findItem(R.id.search).setIcon(
                new IconDrawable(this, Iconify.IconValue.fa_search)
                        .color(Color.WHITE)
                        .actionBarSize()
        ).collapseActionView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {//点击logo和home时，显示sliding menu
            showMenu();
        } else if (item.getItemId() == R.id.publish_info) {
            Intent intent = new Intent(this, PublishInfoActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.search) {
            startActivity(new Intent(this, SearchableActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }


}

