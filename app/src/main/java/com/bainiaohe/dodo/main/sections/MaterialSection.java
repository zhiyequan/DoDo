package com.bainiaohe.dodo.main.sections;

import android.content.Context;


/**
 * 仅用于从当前theme中获取attributes
 * Created by zhugongpu on 15/2/13.
 */
public abstract class MaterialSection {

    private static final String TAG = "MaterialSection";
    // COLORS
    protected int colorPressed;
    protected int colorUnpressed;
    protected int colorSelected;

    protected MaterialSection(Context context) {
        // resolve attributes from current theme
//        Resources.Theme theme = context.getTheme();
//        TypedValue typedValue = new TypedValue();
//        theme.resolveAttribute(R.attr.sectionStyle, typedValue, true);
//        TypedArray values = theme.obtainStyledAttributes(typedValue.resourceId, R.styleable.MaterialSection);
//        try {
//            colorPressed = values.getColor(R.styleable.MaterialSection_sectionBackgroundColorPressed, 0x16000000);
//            colorUnpressed = values.getColor(R.styleable.MaterialSection_sectionBackgroundColor, 0x00FFFFFF);
//            colorSelected = values.getColor(R.styleable.MaterialSection_sectionBackgroundColorSelected, 0x0A000000);
//        } finally {
//            values.recycle();
//        }

        colorPressed = 0x16000000;
        colorUnpressed = 0x00FFFFFF;
        colorSelected = 0x0A000000;
    }


}
