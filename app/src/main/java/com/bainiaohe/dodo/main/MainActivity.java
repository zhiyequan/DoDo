package com.bainiaohe.dodo.main;

import android.content.Intent;
import android.os.Bundle;
import com.bainiaohe.dodo.DoDoContext;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.fragments.friends.FriendsFragment;
import com.bainiaohe.dodo.main.fragments.info.InfoFragment;
import com.bainiaohe.dodo.main.fragments.internship.InternshipFragment;
import com.bainiaohe.dodo.main.fragments.messages.MessageFragment;
import com.bainiaohe.dodo.model.User;
import com.bainiaohe.dodo.setting.SettingActivity;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;

public class MainActivity extends MaterialNavigationDrawer {

    private static final String TAG = "MainActivity";


    @Override
    public void init(Bundle bundle) {
        allowArrowAnimation();
        //TODO 设置只有首次打开时显示
        //disableLearningPattern();

        User currentUser = DoDoContext.getInstance().getCurrentUser();

        //TODO 设置头像和背景
        //设置account
        MaterialAccount account = new MaterialAccount(this.getResources(), currentUser.getName(), currentUser.getEmail(), R.drawable.photo, R.drawable.bamboo);
        addAccount(account);

        //添加sections
        addSection(newSection(getText(R.string.section_info).toString(), new InfoFragment()));
        addSection(newSection(getText(R.string.section_internship).toString(), new InternshipFragment()));

        addSubheader(getText(R.string.sub_header_friends).toString());
        addSection(newSection(getText(R.string.section_friends).toString(), R.drawable.tab_friends, new FriendsFragment()));
        addSection(newSection(getText(R.string.section_messages).toString(), R.drawable.tab_message, new MessageFragment()));
        addSection(newSection(getText(R.string.section_discovery).toString(), new MessageFragment()));//TODO 添加图标
        addDivisor();

        //添加bottom section
        addBottomSection(newSection(getText(R.string.section_settings).toString(), R.drawable.settings, new Intent(this, SettingActivity.class)));

    }
}

