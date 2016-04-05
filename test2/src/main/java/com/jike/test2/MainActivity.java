package com.jike.test2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ProgressBar;

public class MainActivity extends ActionBarActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);

        new AsyncTask<Void,Integer,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                for (int i=0;i<=100;i++){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishProgress(i);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progressBar.setSecondaryProgress(values[0]+5);
                progressBar.setProgress(values[0]);
                super.onProgressUpdate(values);
            }
        }.execute();

    }
}
