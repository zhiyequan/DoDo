package com.bainiaohe.dodo.add_friends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bainiaohe.dodo.R;

/**
 * Created by liuxiaoran on 2015/2/2.
 */
public class AddFriends extends Activity {

    private Button search_friends_btn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriends);
        search_friends_btn= (Button) findViewById(R.id.search_friends_button);
        search_friends_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddFriends.this,SearchFriends.class);
                startActivity(intent);
            }
        });
    }

}
