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

    /**
     * 根据image和text生成一个tab
     *
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

}
