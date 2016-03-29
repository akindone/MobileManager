package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jike.application.MyApplication;

public class PreThiefSetup3 extends Activity {

    private EditText et_setup3_safenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_thief_setup3);
        et_setup3_safenum = (EditText) findViewById(R.id.et_setup3_safenum);
    }

    //获取手机联系人
    /*// way1 调用系统api
    public void gotoContactList(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, 100);
    }
*/

    // way2 自己实现应该listview
    public void gotoContactList(View v){
        Intent intent = new Intent(this, MyContactView.class);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK){
            Uri uri = data.getData();
            String[] projection={ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToNext();
            int columnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String telnum = cursor.getString(columnIndex);
            et_setup3_safenum.setText(telnum);
        }else if (requestCode==200&&resultCode==2000){
            String telnum = data.getStringExtra("tele");
            et_setup3_safenum.setText(telnum);
        }
    }

    public void next(View v){
        String safenum = et_setup3_safenum.getText().toString();
        if (!safenum.isEmpty()){
            MyApplication.setConfigValue("safenum", safenum);
            startActivity(new Intent(this, PreThiefSetup4.class));
        }else Toast.makeText(PreThiefSetup3.this, "安全手机号码不能为空", Toast.LENGTH_SHORT).show();
    }
    public void previous(View v){
        startActivity(new Intent(this, PreThiefSetup2.class));
    }
}
