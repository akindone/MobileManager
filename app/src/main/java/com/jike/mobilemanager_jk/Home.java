package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.application.MyApplication;
import com.jike.utils.MD5Utils;

public class Home extends Activity {
    private static final String TAG = "Home";
    public static Home mHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mHome=this;

        //隐藏actiongbar
        /*ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();*/

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
                        //设置一个密码，输入密码正确之后才可以进入设置
                        //第一次进入的时候让他设置密码，保存到sp里。之后第二次以后才验证密码
                        String code = MyApplication.getConfigValue("preventThiefCode", "");
                        if (code.isEmpty()) {
                            alterSetPwdDialog();
                        } else {
                            alterReEnterDailog();
                        }
                        //Toast.makeText(Home.this, MyBaseAdapter.funsName[position], Toast.LENGTH_SHORT).show();
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
                        startActivity(new Intent(Home.this, SettingActivity.class));
                        break;
                }
            }
        });
        gv_home_funcs.setAdapter(new MyBaseAdapter());
    }

    private void alterReEnterDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = View.inflate(this, R.layout.dialog_psw_enter_again, null);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText et_enterAgain_psw = (EditText) v.findViewById(R.id.et_enterAgain_psw);
        Button bt_dialog_enterAgain_ok = (Button) v.findViewById(R.id.bt_dialog_enterAgain_ok);
        final Button bt_dialog_enterAgain_cancel = (Button) v.findViewById(R.id.bt_dialog_enterAgain_cancel);



        bt_dialog_enterAgain_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psw_enterAgain = et_enterAgain_psw.getText().toString();
                String md5_psw = MD5Utils.getMd5(psw_enterAgain);

                String preventThiefCode = MyApplication.getConfigValue("preventThiefCode", "");
                if (md5_psw.equals(preventThiefCode)) {
                    Toast.makeText(Home.this, "密码密码正确，将跳转至设置页面", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    enterSafeSetting();
                } else {
                    Toast.makeText(Home.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_dialog_enterAgain_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void enterSafeSetting() {
        startActivity(new Intent(Home.this,PreThiefStart.class));
    }

    private void alterSetPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v=View.inflate(this, R.layout.dialog_psw_in, null);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();//被局部内部类调用的变量要声明为final
        alertDialog.show();

        final TextView et_psw = (TextView) v.findViewById(R.id.et_psw);
        final TextView et_psw_confirm = (TextView) v.findViewById(R.id.et_psw_confirm);
        Button bt_dialog_setpwd_ok = (Button) v.findViewById(R.id.bt_dialog_setpwd_ok);
        Button bt_dialog_setpwd_cancel = (Button) v.findViewById(R.id.bt_dialog_setpwd_cancel);



        bt_dialog_setpwd_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String psw = et_psw.getText().toString();
                String psw_confirm = et_psw_confirm.getText().toString();

                if (psw.isEmpty()||psw_confirm.isEmpty()){
                    Toast.makeText(Home.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }else if (!psw.equals(psw_confirm)){
                    Toast.makeText(Home.this, "两次密码不同，请重新输入", Toast.LENGTH_SHORT).show();
                }else {
                    //把密码加密
                    String md5_psw = MD5Utils.getMd5(psw);
                    MyApplication.setConfigValue("preventThiefCode", md5_psw);
                    alertDialog.dismiss();
                    Toast.makeText(Home.this, "密码设置成功", Toast.LENGTH_SHORT).show();
                    enterSafeSetting();//进入手机防盗设置页面
                }
            }
        });

        bt_dialog_setpwd_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

}
