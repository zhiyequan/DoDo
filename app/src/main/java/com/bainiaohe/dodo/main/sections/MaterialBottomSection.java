package com.bainiaohe.dodo.main.sections;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.sections.listeners.MaterialBottomSectionOnClickListener;
import com.squareup.picasso.Picasso;

/**
 * Created by zhugongpu on 15/2/15.
 */
public class MaterialBottomSection extends MaterialSection {

    private static final String TAG = "MaterialBottomSection";
    protected int layoutId = R.layout.layout_material_section_with_icon_large_ripple;
    private int iconResourceId = 0;//图标drawable id
    private String title = "";//section中显示的文字
    private String notification = "";//通知内容（可以为数字）
    private MaterialBottomSectionOnClickListener onClickListener = null;
    private Intent targetIntent = null;
    private View contentView = null;
    private Context context = null;


    public MaterialBottomSection(Context context, int iconResourceId, String title, String notification,
                                 Intent targetIntent,
                                 MaterialBottomSectionOnClickListener onClickListener) {
        super(context);
        this.context = context;
        this.iconResourceId = iconResourceId;
        this.title = title;
        this.notification = notification;
        this.targetIntent = targetIntent;
        this.onClickListener = onClickListener;

    }

    public Intent getTargetIntent() {
        return targetIntent;
    }

    public View getContentView() {
        if (contentView == null) {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            // inflate the line
            View divider = new View(context);
            divider.setBackgroundColor(Color.parseColor("#e0e0e0"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            layout.addView(divider, params);


            View section = View.inflate(context, layoutId, null);
            Picasso.with(context).load(iconResourceId).into((android.widget.ImageView) section.findViewById(R.id.section_icon));
            ((TextView) section.findViewById(R.id.section_text)).setText(title);
            ((TextView) section.findViewById(R.id.section_notification)).setText(notification);

            section.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(MaterialBottomSection.this);
                }
            });

            layout.addView(section);

            contentView = layout;
        }
        return contentView;
    }

}
