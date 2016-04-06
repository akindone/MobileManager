package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jike.beans.BlackListItem;
import com.jike.dao.BlackListDao;

import java.util.ArrayList;

public class TeleManagerActivity extends Activity {

    private static final int LIMIT = 10;
    private static final String TAG = "TeleManagerActivity";
    private int offset = 0;

    private ListView lv_blacklist_content;
    private BlackListDao blackListDao;
    private ArrayList<BlackListItem> list;
    private MyBlackListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele_manager);
        lv_blacklist_content = (ListView) findViewById(R.id.lv_blacklist_content);

        blackListDao = new BlackListDao(TeleManagerActivity.this);

        list= new ArrayList<>();

        //一次查询全部，耗时
//        list = blackListDao.queryAll();

        //第一次进入页面只加载10条
        ArrayList<BlackListItem> queryPart = blackListDao.queryPart(LIMIT, offset);
        list.addAll(queryPart);
        offset = list.size();
        Log.e(TAG, offset + "");

        adapter = new MyBlackListAdapter();
        lv_blacklist_content.setAdapter(adapter);

        lv_blacklist_content.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState==SCROLL_STATE_IDLE){
                    int num = blackListDao.queryNum();
                    if (view.getLastVisiblePosition()==list.size()-1&&offset<num){
                        //页面到达最低端，且后面还有没有显示的
                        ArrayList<BlackListItem> queryPart = blackListDao.queryPart(LIMIT,offset);
                        list.addAll(queryPart);
                        offset = list.size();
                        Log.e(TAG, offset + "");
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        lv_blacklist_content.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {

                TextView tv_item_blacklist_num = (TextView) view.findViewById(R.id.tv_item_blacklist_num);
                TextView tv_item_blacklist_mode = (TextView) view.findViewById(R.id.tv_item_blacklist_mode);

                final String num = tv_item_blacklist_num.getText().toString();
                String mode = tv_item_blacklist_mode.getText().toString();
                int check = Integer.parseInt(mode)-1;
                //弹出模式修改框
                new AlertDialog.Builder(TeleManagerActivity.this)
                        .setTitle("修改拦截模式")
                        .setSingleChoiceItems(new String[]{"拦截电话","拦截短信","拦截全部"}, check, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("onItemLongClick", num + ":" + (which + 1));
                                blackListDao.update(num, which + 1);




                                //因为添加了OnScroll的监听器，所以不需要再手动去notify了
                                /*list = blackListDao.queryAll();
                                adapter.notifyDataSetChanged();*/

                                dialog.dismiss();
                            }
                        })
                        .show();

                return true;
            }
        });

        lv_blacklist_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView tv_item_blacklist_num = (TextView) view.findViewById(R.id.tv_item_blacklist_num);
                final String num = tv_item_blacklist_num.getText().toString();

                //弹出是否删除框
                new AlertDialog.Builder(TeleManagerActivity.this)
                        .setTitle("是否删除该黑名单？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                blackListDao.delete(num);

                                list.remove(position);
                                Log.e(TAG,"remove:"+position);
                                //list = blackListDao.queryAll();

                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });


    }

    public void addToBlackList(View view) {
        View dialogView = View.inflate(this,R.layout.dialog_blacklist,null);
        final EditText et_blacklist_num = (EditText) dialogView.findViewById(R.id.et_blacklist_num);
        final RadioGroup rg_blacklist_mode = (RadioGroup) dialogView.findViewById(R.id.rg_blacklist_mode);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("增加号码黑名单")
                .setView(dialogView)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String num = et_blacklist_num.getText().toString();
                        int checkId = rg_blacklist_mode.getCheckedRadioButtonId();

                        int mode=-1;
                        if (checkId==R.id.rb_blacklist_call) mode=1;
                        else if (checkId==R.id.rb_blacklist_sms) mode=2;
                        else if (checkId==R.id.rb_blacklist_all) mode=3;
                        Log.e("onClick", mode + ":" + num);

                        if (mode!=-1&&!num.isEmpty()){

                            long insert = blackListDao.insert(num, mode);
                            if (insert!=-1){
                                list = blackListDao.queryAll();
                                adapter.notifyDataSetChanged();
                            }
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }

    class MyBlackListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
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
            View view = View.inflate(TeleManagerActivity.this, R.layout.listitem_blacklist, null);
            TextView tv_item_blacklist_num = (TextView) view.findViewById(R.id.tv_item_blacklist_num);
            TextView tv_item_blacklist_mode = (TextView) view.findViewById(R.id.tv_item_blacklist_mode);

            BlackListItem blackListItem = list.get(position);
            tv_item_blacklist_num.setText(blackListItem.getNum());
            tv_item_blacklist_mode.setText(blackListItem.getMode()+"");

            return view;
        }
    }
}
