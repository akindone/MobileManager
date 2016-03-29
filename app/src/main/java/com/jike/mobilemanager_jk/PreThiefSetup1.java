package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PreThiefSetup1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prethief_setup1);
    }
    public void next(View v){
        startActivity(new Intent(this,PreThiefSetup2.class));
    }
}
