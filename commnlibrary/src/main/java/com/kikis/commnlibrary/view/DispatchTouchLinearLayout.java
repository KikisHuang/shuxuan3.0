package com.kikis.commnlibrary.view;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Kikis on 2019/3/22.
 */

public class DispatchTouchLinearLayout extends LinearLayout {

    public DispatchTouchLinearLayout(Context context) {
        super(context);
    }

    public void setTouchListener() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

        }
        return super.dispatchTouchEvent(ev);
    }
}
