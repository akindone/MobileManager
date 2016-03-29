package com.jike.mobilemanager_jk;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jike.dao.NumberLocationDao;

public class AdvToolPhoLocation extends ActionBarActivity {

    private EditText et_phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_tool_pho_location);
        et_phoneNum = (EditText) findViewById(R.id.et_phoneNum);
    }

    public void queryLocation(View v){
        String num = et_phoneNum.getText().toString();
        String phoneLocation = NumberLocationDao.getPhoneLocation(this, num);
        Toast.makeText(this, phoneLocation, Toast.LENGTH_SHORT).show();
    }
}
