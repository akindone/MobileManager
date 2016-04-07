package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class MyBaseActivity extends Activity {
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_base);
        gestureDetector = new GestureDetector(this, new MyGestureListener());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    abstract void next(View v);
    abstract void previous(View v);

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float startx = e1.getX();

            float endx = e2.getX();

            if (startx-endx>200) next(null);
            else if (endx-startx>200) previous(null);
            return super.onFling(e1, e2, velocityX, velocityY);
        }



        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.e("MyGestureListener", "onDoubleTap");
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.e("MyGestureListener","onLongPress");
            super.onLongPress(e);
        }

    }
}
