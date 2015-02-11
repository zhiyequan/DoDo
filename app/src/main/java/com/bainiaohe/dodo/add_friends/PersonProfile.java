package com.bainiaohe.dodo.add_friends;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.model.User;
import com.bainiaohe.dodo.utils.AddFriendsService;
import com.squareup.picasso.Picasso;

/**
 * 在找好友的时候展现个人信息
 * Created by liuxiaoran on 2015/2/5.
 */
public class PersonProfile extends Activity implements View.OnClickListener{

    private ImageView avator;
    private TextView  nickname;
    private TextView  realname;
    private TextView  phone;
    private TextView  sex;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);
        avator= (ImageView) findViewById(R.id.person_avatar);
        nickname=(TextView) findViewById(R.id.person_nickname);
        realname=(TextView) findViewById(R.id.person_real_name);
        phone=(TextView) findViewById(R.id.person_account);
        sex=(TextView) findViewById(R.id.person_sex);
        User user=AddFriendsService.user;
        Picasso.with(this).load(user.getAvatar()).into(avator);
        nickname.setText(user.getNickName());
        realname.setText(user.getName());
        phone.setText(user.getPhone());
        sex.setText(user.getSex());

    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.add_friend_button){
            //添加他为好友
           AddFriendsService.addFriend(AddFriendsService.user.getUserId());
        }
    }
}
