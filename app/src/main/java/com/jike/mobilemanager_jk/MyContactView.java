package com.jike.mobilemanager_jk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jike.beans.Contact;
import com.jike.utils.ContactUtils;

import java.util.ArrayList;

public class MyContactView extends ActionBarActivity {

    private static final String TAG = "MyContactView";
    private ListView lv_concact_content;
    private ArrayList<Contact> contacts=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact_view);

        lv_concact_content = (ListView) findViewById(R.id.lv_concact_content);
        //初始化contacts的数据
        contacts = ContactUtils.getAllContact(this);
        lv_concact_content.setAdapter(new MyBaseAdapter());
        lv_concact_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG,"onItemClick"+parent.getClass().getName()+"----"+view.getClass().getName()
                        +"----"+position+"----"+id);
                String tele = contacts.get(position).getTele();
                Log.e(TAG,"tele"+tele);
                Intent intent = new Intent();
                intent.putExtra("tele",tele);
                setResult(2000,intent);
                finish();
            }
        });
    }



    class MyBaseAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            if (contacts.size()!=0) return contacts.size();
            else return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate;
            Holder holder;
            if (convertView==null){
                 inflate = View.inflate(MyContactView.this, R.layout.listitem_contact, null);
                holder = new Holder();
                holder.tv_contact_name = (TextView) inflate.findViewById(R.id.tv_contact_name);
                holder.tv_contact_tele = (TextView) inflate.findViewById(R.id.tv_contact_tele);
                inflate.setTag(holder);
            }else{
                inflate=convertView;
                holder = (Holder)inflate.getTag();
            }
            holder.tv_contact_name.setText(contacts.get(position).getName());
            holder.tv_contact_tele.setText(contacts.get(position).getTele());
            return inflate;
        }

        class Holder{
            private TextView tv_contact_name;
            private TextView tv_contact_tele;
        }
    }
}
