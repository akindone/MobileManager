package com.jike.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jike.application.MyApplication;
import com.jike.mobilemanager_jk.R;

/**
 * Created by wancc on 2016/3/25.
 */
public class SettingItem extends RelativeLayout implements View.OnClickListener{

    private static final String TAG = "SettingItem";
    private TextView tv_settingItem_title;
    private TextView tv_settingItem_state;
    private CheckBox cb_settingItem_choice;
    private String itemStatus_on;
    private String itemStatus_off;
    private String itemName;
    private String sp_key;


    public SettingItem(Context context) {
        super(context);
        init(null);
    }

    //系统调用
    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    private void init(AttributeSet attrs) {
        itemName = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "itemName");
        itemStatus_on = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "itemStatus_on");
        itemStatus_off = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "itemStatus_off");
        sp_key = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "sf_key");

        View v = View.inflate(getContext(), R.layout.item_setting, null);
        tv_settingItem_title = (TextView) v.findViewById(R.id.tv_settingItem_title);
        tv_settingItem_state = (TextView) v.findViewById(R.id.tv_settingItem_state);
        cb_settingItem_choice = (CheckBox) v.findViewById(R.id.cb_settingItem_choice);

        tv_settingItem_title.setText(itemName);

        boolean aBoolean = MyApplication.config.getBoolean(sp_key, true);
        Log.e(TAG,"init isChecked"+aBoolean);
        if (aBoolean){
            tv_settingItem_state.setText(itemStatus_on);
            cb_settingItem_choice.setChecked(true);

        }else {
            tv_settingItem_state.setText(itemStatus_off);
            cb_settingItem_choice.setChecked(false);
        }
        addView(v);//通过把组合控件添加到RelativeLayout,最终显示到调用RelativeLayout的布局中
        setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        boolean selected = cb_settingItem_choice.isChecked();
        Log.e(TAG,"onClick isChecked"+selected);
        SharedPreferences.Editor edit = MyApplication.config.edit();
        if (selected){
            cb_settingItem_choice.setChecked(false);
            tv_settingItem_state.setText(itemStatus_off);
            edit.putBoolean(sp_key, false);
            Log.e(TAG, "false" );
            edit.commit();
        } else {
            cb_settingItem_choice.setChecked(true);
            tv_settingItem_state.setText(itemStatus_on);
            edit.putBoolean(sp_key, true);
            Log.e(TAG, "true");
            edit.commit();
        }
    }
}
