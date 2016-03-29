package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdvanceTool extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_tool);

    }

    public void getPhoneLocation(View v){
        startActivity(new Intent(this,AdvToolPhoLocation.class));
        overridePendingTransition(R.anim.slideinright,R.anim.slideoutleft);
    }

    public void smsCopy(View v){}

    public void smsRestore(View v){}


}
