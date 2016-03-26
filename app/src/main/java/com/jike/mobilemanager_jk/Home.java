package com.jike.mobilemanager_jk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class Home extends ActionBarActivity {
    public static Home mHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mHome=this;

        //隐藏actiongbar
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();
        //设置跑马灯  ellispe=marquee ,setSelecte（true）
        /*TextView tv_home_welcome = (TextView) findViewById(R.id.tv_home_welcome);
        tv_home_welcome.setSelected(true);*/


        GridView gv_home_funcs = (GridView) findViewById(R.id.gv_home_funcs);
        gv_home_funcs.setNumColumns(3);
        gv_home_funcs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(Home.this, MyBaseAdapter.funsName[position], Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(Home.this, MyBaseAdapter.funsName[position], Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(Home.this, MyBaseAdapter.funsName[position], Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(Home.this, MyBaseAdapter.funsName[position], Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(Home.this, MyBaseAdapter.funsName[position], Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(Home.this, MyBaseAdapter.funsName[position], Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(Home.this, MyBaseAdapter.funsName[position], Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(Home.this, MyBaseAdapter.funsName[position], Toast.LENGTH_SHORT).show();
                        break;
                    case 8:
                        Toast.makeText(Home.this, MyBaseAdapter.funsName[position], Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Home.this,SettingActivity.class));
                        break;
                }
            }
        });
        gv_home_funcs.setAdapter(new MyBaseAdapter());
    }

}
