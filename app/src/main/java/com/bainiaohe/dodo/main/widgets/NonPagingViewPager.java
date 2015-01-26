package com.bainiaohe.dodo.main.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class NonPagingViewPager extends ViewPager {

    private boolean pagingEnabled = false;
    private int childId;

    public NonPagingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonPagingViewPager(Context context) {
        super(context);
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean result = false;
        View scroll = getChildAt(childId);
        if (scroll != null) {
            Rect rect = new Rect();

            scroll.getHitRect(rect);
            if (rect.contains((int) event.getX(), (int) event.getY())) {

                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return pagingEnabled && super.onTouchEvent(ev);
    }
}
