package com.bainiaohe.dodo.main.sections;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.sections.listeners.MaterialAccountSectionOnClickListener;
import com.squareup.picasso.Picasso;


/**
 * Created by zhugongpu on 15/2/13.
 */
public class MaterialAccountSection extends MaterialSection {

    private String name;
    private String email;

    private String avatarURL = null;//当avatar url为null时，必有avatar resouce id
    private int avatarResourceId = 0;//当avatar resouce id为0时，必有avatar url

    //Views
    private ImageView background = null;
    private ImageView avatar = null;
    private TextView userName = null;
    private TextView userEmail = null;

    private MaterialAccountSectionOnClickListener onClickListener = null;

    private Intent targetIntent = null;
    private View contentView = null;
    private Context context = null;

    public MaterialAccountSection(Context context, String name, String email, String avatarURL,
                                  Intent targetIntent,
                                  MaterialAccountSectionOnClickListener onClickListener) {
        super(context);
        this.context = context;
        this.name = name;
        this.email = email;
        this.avatarURL = avatarURL;
        this.avatarResourceId = 0;

        this.onClickListener = onClickListener;
        this.targetIntent = targetIntent;
    }

    public MaterialAccountSection(Context context, String name, String email, int avatarResourceId,
                                  Intent targetIntent,
                                  MaterialAccountSectionOnClickListener onClickListener) {
        super(context);
        this.context = context;
        this.name = name;
        this.email = email;
        this.avatarResourceId = avatarResourceId;
        this.avatarURL = null;

        this.onClickListener = onClickListener;
        this.targetIntent = targetIntent;
    }

    public View getContentView() {
        //inflate view
        if (contentView == null) {
            contentView = View.inflate(context, R.layout.layout_material_section_account, null);
            background = (ImageView) contentView.findViewById(R.id.account_section_background);
            avatar = (ImageView) contentView.findViewById(R.id.account_section_avatar);
            userName = (TextView) contentView.findViewById(R.id.account_section_user_name);
            userEmail = (TextView) contentView.findViewById(R.id.account_section_user_email);

            //加载头像
            if (avatarURL != null)
                Picasso.with(context).load(avatarURL).noFade().into(avatar);
            else
                Picasso.with(context).load(avatarResourceId).noFade().into(avatar);
            userName.setText(name);
            userEmail.setText(email);

            //todo layout中添加ripple layout
//            rippleLayout = (com.balysv.materialripple.MaterialRippleLayout) contentView.findViewById(R.id.section_ripple);
//            rippleLayout.setRippleBackground(colorUnpressed);
//            rippleLayout.setRippleColor(colorPressed);


            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(MaterialAccountSection.this);
                }
            });
        }
        return contentView;
    }

    public Intent getTargetIntent() {
        return targetIntent;
    }

    public int getAvatarResourceId() {
        return avatarResourceId;
    }

    public void setAvatarResourceId(int avatarResourceId) {
        this.avatarResourceId = avatarResourceId;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
