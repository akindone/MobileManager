package com.jike.mobilemanager_jk;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by wancc on 2016/3/25.
 */
class MyBaseAdapter extends BaseAdapter {
    static final int COUNT=9;
    int[] cions={R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
            R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
            R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};
    static final String[] funsName={"手机防盗","通信卫士","软件管理",
            "进程管理","流量统计","手机杀毒",
            "缓存清理","高级工具","设置中心"};
    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(Home.mHome, R.layout.item_home_funcs, null);
        ImageView iv_item_cion = (ImageView) v.findViewById(R.id.iv_item_cion);
        TextView tv_item_funcsName = (TextView) v.findViewById(R.id.tv_item_funcsName);
        iv_item_cion.setImageResource(cions[position]);
        tv_item_funcsName.setText(funsName[position]);

        return v;
    }
}
