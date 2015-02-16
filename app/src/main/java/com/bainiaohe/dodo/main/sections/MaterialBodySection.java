package com.bainiaohe.dodo.main.sections;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.sections.listeners.MaterialBodySectionOnClickListener;
import com.squareup.picasso.Picasso;

/**
 * Created by zhugongpu on 15/2/15.
 */
public class MaterialBodySection extends MaterialSection {
    private static final String TAG = "MaterialBodySection";

    protected int layoutId = R.layout.layout_material_section_with_icon_ripple;
    private int iconResourceId = 0;//图标drawable id
    private String title = "";//section中显示的文字
    private String notification = "";//通知内容（可以为数字）
    private MaterialBodySectionOnClickListener onClickListener = null;

    private int position;
    private Fragment targetFragment = null;
    private View contentView = null;
    private Context context = null;
    private boolean isSelected = false;


    public MaterialBodySection(Context context, int iconResourceId, String title, String notification, int position,
                               Fragment targetFragment, MaterialBodySectionOnClickListener onClickListener) {
        super(context);
        this.context = context;
        this.iconResourceId = iconResourceId;
        this.title = title;
        this.notification = notification;
        this.position = position;
        this.targetFragment = targetFragment;
        this.onClickListener = onClickListener;

        this.layoutId = R.layout.layout_material_section_with_icon_ripple;
    }

    public MaterialBodySection(Context context, String title, String notification, int position,
                               Fragment targetFragment, MaterialBodySectionOnClickListener onClickListener) {
        super(context);
        this.context = context;
        this.title = title;
        this.notification = notification;
        this.position = position;
        this.targetFragment = targetFragment;
        this.onClickListener = onClickListener;

        this.layoutId = R.layout.layout_material_section_no_icon_ripple;
        this.iconResourceId = 0;
    }

    public View getContentView() {
        if (contentView == null) {
            contentView = View.inflate(context, layoutId, null);

            //根据是否设置icon选择性加载
            if (this.iconResourceId != 0)
                Picasso.with(context).load(iconResourceId).into((android.widget.ImageView) contentView.findViewById(R.id.section_icon));

            ((TextView) contentView.findViewById(R.id.section_text)).setText(title);
            ((TextView) contentView.findViewById(R.id.section_notification)).setText(notification);

            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(MaterialBodySection.this);
                }
            });
        }
        return contentView;
    }

    public int getPosition() {
        return position;
    }

    public Fragment getTargetFragment() {
        return targetFragment;
    }

    public boolean isSelected() {
        return isSelected;
    }

    /**
     * 需要被复写
     * 用于处理取消选中事件
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;

        if (contentView != null)
            contentView.setSelected(isSelected());

        //Log.e(TAG, "set selected : " + isSelected + "  " + getPosition() + "   " + (rippleLayout == null));
    }
}
