package com.bainiaohe.dodo.main.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bainiaohe.dodo.R;

/**
 * Created by zhugongpu on 15/2/7.
 */
public class BottomBarTab {

    private View view = null;

    /**
     * @param context
     * @param image   selector res id
     * @param text    text res id
     */
    public BottomBarTab(Context context, int image, int text) {
        this.view = LayoutInflater.from(context).inflate(R.layout.widget_bottom_bar_tab, null);
        ((ImageView) this.view.findViewById(R.id.tab_image)).setImageResource(image);//设置图标
        ((TextView) this.view.findViewById(R.id.tab_text)).setText(context.getResources().getText(text));//设置说明文字
    }

    /**
     * @param context
     * @param image   selector res id
     * @param text    text res id
     * @return
     */
    public static View genTab(Context context, int image, int text) {
        View tab = LayoutInflater.from(context).inflate(R.layout.widget_bottom_bar_tab, null);
        //设置weight = 1
        tab.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        ((ImageView) tab.findViewById(R.id.tab_image)).setImageResource(image);//设置图标
        ((TextView) tab.findViewById(R.id.tab_text)).setText(context.getResources().getText(text));//设置说明文字
        return tab;
    }

    /**
     * 根据是否选中调整图片和文字颜色
     *
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.view.findViewById(R.id.tab_image).setSelected(selected);
        this.view.findViewById(R.id.tab_text).setSelected(selected);
    }

}
