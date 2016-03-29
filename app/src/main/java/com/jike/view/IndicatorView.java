package com.jike.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jike.mobilemanager_jk.R;

/**
 * Created by wancc on 2016/3/28.
 */
public class IndicatorView extends LinearLayout {
    private static final String TAG = "IndicatorView";

    public IndicatorView(Context context) {
        super(context);
        init(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View inflate = View.inflate(context, R.layout.item_indicator, null);
        ImageView indicator01 = (ImageView) inflate.findViewById(R.id.iv_item_indicator01);
        ImageView indicator02 = (ImageView) inflate.findViewById(R.id.iv_item_indicator02);
        ImageView indicator03 = (ImageView) inflate.findViewById(R.id.iv_item_indicator03);
        ImageView indicator04 = (ImageView) inflate.findViewById(R.id.iv_item_indicator04);

        ImageView[] views=new ImageView[4];
        views[0]=indicator01;
        views[1]=indicator02;
        views[2]=indicator03;
        views[3]=indicator04;

        if (attrs!=null){
            int currentId = attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "currentId", 0);
            Log.e(TAG, "currentId:" + currentId);
            for (int i=0;i<currentId;i++){
                views[i].setImageResource(android.R.drawable.presence_online);
            }
        }

        addView(inflate);//千万别忘了这一步
    }
}
