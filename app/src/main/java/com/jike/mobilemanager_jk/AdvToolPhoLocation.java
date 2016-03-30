package com.jike.mobilemanager_jk;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.dao.NumberLocationDao;

public class AdvToolPhoLocation extends ActionBarActivity {

    private static final String TAG = "AdvToolPhoLocation";
    private EditText et_phoneNum;
    private TextView tv_phoneLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_tool_pho_location);
        et_phoneNum = (EditText) findViewById(R.id.et_phoneNum);
        tv_phoneLocation = (TextView) findViewById(R.id.tv_phoneLocation);
        et_phoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e(TAG,"beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG,"beforeTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG,"beforeTextChanged");
                String num = et_phoneNum.getText().toString();
                String phoneLocation = NumberLocationDao.getPhoneLocation(null, num);
                tv_phoneLocation.setText(phoneLocation);
            }
        });
    }

    public void queryLocation(View v){
        String num = et_phoneNum.getText().toString();
        if (NumberLocationDao.isValid(num)){
            String phoneLocation = NumberLocationDao.getPhoneLocation(this, num);
            Toast.makeText(this, phoneLocation, Toast.LENGTH_SHORT).show();
        }
        else
        Toast.makeText(this, "输入号码不符合要求", Toast.LENGTH_SHORT).show();
    }


}
