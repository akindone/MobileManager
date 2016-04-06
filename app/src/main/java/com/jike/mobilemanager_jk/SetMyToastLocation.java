package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.application.MyApplication;
import com.jike.utils.WindowUtils;

public class SetMyToastLocation extends Activity {

    private static final String TAG = "SetMyToastLocation";
    private LinearLayout ll_mytoast_bg;
    private long t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_my_toast_location);
        ll_mytoast_bg = (LinearLayout) findViewById(R.id.ll_mytoast_bg);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_mytoast_bg.getLayoutParams();
        layoutParams.leftMargin= MyApplication.getConfigValue("mytoast_location_x",200);
        layoutParams.topMargin = MyApplication.getConfigValue("mytoast_location_y",300);

        ll_mytoast_bg.setOnTouchListener(new View.OnTouchListener() {
            private float startX;
            private float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "ACTION_DOWN");
                        //触摸点相对屏幕的位置
                        startX = event.getRawX();
                        startY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "ACTION_MOVE");
                        float movingX = event.getRawX();
                        float movingY = event.getRawY();
                        float dx = movingX - startX;
                        float dy = movingY - startY;

                        float x = ll_mytoast_bg.getX();//控件相对屏幕的左上角的左侧像素
                        float y = ll_mytoast_bg.getY();

//                        int left = ll_mytoast_bg.getLeft();//相对于父控件

                        int width = ll_mytoast_bg.getWidth();
                        int height = ll_mytoast_bg.getHeight();

                        int l=    Math.round(x + dx);
                        int t=    Math.round(y + dy);
                        int r=Math.round(x + dx + width);
                        int b=Math.round(y + dy + height);

                        Log.e(TAG,l+"---"+(x+dx));

                        int widthPixels = WindowUtils.getWidthPixels(SetMyToastLocation.this);
                        int heightPixels = WindowUtils.getHeightPixels(SetMyToastLocation.this);

                        if (l<0||t<0||r>widthPixels ||b>heightPixels) {break;}
                        Log.e(TAG,"BREAK!!!");
                        ll_mytoast_bg.layout(l, t, r,b);

                        //重置
                        startX=movingX;
                        startY=movingY;
                        break;
                    case MotionEvent.ACTION_UP:

                        MyApplication.setConfigValue("mytoast_location_x",(int)ll_mytoast_bg.getX());
                        MyApplication.setConfigValue("mytoast_location_y", (int)ll_mytoast_bg.getY());
                        Log.e(TAG, "SP:" + (int) ll_mytoast_bg.getX() + "---" + (int) ll_mytoast_bg.getY());
                        Log.e(TAG, "ACTION_UP");
                        break;
                }
                return false;
            }
        });

        //实现双击，归位效果
        ll_mytoast_bg.setOnClickListener(new View.OnClickListener() {
            boolean flag=true;
            @Override
            public void onClick(View v) {

                if (flag){
                    t1 = System.currentTimeMillis();
                    flag=false;
                }else{
                    long t2 = System.currentTimeMillis();
                    if (t2-t1<500){
                        Toast.makeText(SetMyToastLocation.this, "double click", Toast.LENGTH_SHORT).show();
                        ll_mytoast_bg.layout(200,300,200+ll_mytoast_bg.getWidth(),+300+ll_mytoast_bg.getHeight());
                        MyApplication.setConfigValue("mytoast_location_x", 200);
                        MyApplication.setConfigValue("mytoast_location_y", 300);
                    }
                    flag=true;
                }

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }




    private View inflate;
    private WindowManager wm;
    private LinearLayout ll_mytoast_bg2;

    private void showMyToast(String phoneLocation) {
        if (phoneLocation.isEmpty()){
            Toast.makeText(this, "输入号码不符合要求", Toast.LENGTH_SHORT).show();
            return;
        }
        inflate = View.inflate(this, R.layout.mytoast, null);
        ll_mytoast_bg2 = (LinearLayout) inflate.findViewById(R.id.ll_mytoast_bg);

        //动态给toast设置背景图片
        int resId_bg = MyApplication.getConfigValue("ll_mytoast_bg", R.drawable.call_locate_blue);
        ll_mytoast_bg2.setBackgroundResource(resId_bg);

        TextView tv_mytoast_msg = (TextView) inflate.findViewById(R.id.tv_mytoast_msg);
        tv_mytoast_msg.setText(phoneLocation);

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
         WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;


        //给自动以window动态设置位置，一定要先指定gravity！！！
        params.gravity= Gravity.LEFT|Gravity.TOP;
        params.x=MyApplication.getConfigValue("mytoast_location_x", 200);//相对于屏幕
        params.y=MyApplication.getConfigValue("mytoast_location_y", 300);

//        params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        wm.addView(inflate, params);

    }



    private void hideMyToast() {
        if (wm!=null){
            wm.removeView(inflate);
        }
    }

    public void showMytoust(View view) {
        showMyToast("这是一个测试");
    }

    public void hideMytoust(View view) {
        hideMyToast();
    }
}
