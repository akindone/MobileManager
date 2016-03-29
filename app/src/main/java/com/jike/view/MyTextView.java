package com.jike.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by wancc on 2016/3/25.
 */
public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
    }


    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //实现走马灯效果：使textview一直获得焦点，且ellezipe=marque
    @Override
    public boolean isFocused() {
        return true;//super.isFocused();
    }



}
