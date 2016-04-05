package com.jike.mobilemanager_jk;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.beans.ProcessInfo;
import com.jike.utils.ProcessUtils;

import java.util.ArrayList;

public class ProcessManagerActivity extends Activity {

    private static final String TAG = "ProcessManagerActivity";
    private TextView tv_process_processnum;
    private TextView tv_process_spaceRAM;
    private TextView tv_process_reminder;
    private ListView lv_process_content;

    private ArrayList<ProcessInfo> runningProcess;
    private ArrayList<ProcessInfo> runningProcess_sys;
    private ArrayList<ProcessInfo> runningProcess_user;

    private MyBaseAdapter myBaseAdapter;
    private boolean showUserProc;
    private MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);

        tv_process_processnum = (TextView) findViewById(R.id.tv_process_processnum);
        tv_process_spaceRAM = (TextView) findViewById(R.id.tv_process_spaceRAM);
        lv_process_content = (ListView) findViewById(R.id.lv_process_content);
        tv_process_reminder = (TextView) findViewById(R.id.tv_process_reminder);

        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();


    }

    public void selectAll(View view) {
        isSelectAll(true);
    }

    private void isSelectAll(boolean flag) {
        for (ProcessInfo info:runningProcess){
            info.setIsChecked(flag);
            if (myBaseAdapter!=null){
                myBaseAdapter.notifyDataSetChanged();
            }
        }
    }

    public void unselectAll(View view) {
        isSelectAll(false);
    }

    public void killProcess(View view) {
        ProcessUtils.killProcess(runningProcess,this);
        reFresh();
    }

    private void reFresh() {
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    public void showByGroup(View view) {
        //不需要重新分组怎么显示
        if (!showUserProc){
            Toast.makeText(ProcessManagerActivity.this, "只显示用户进程", Toast.LENGTH_SHORT).show();
            showUserProc = true;
        }else{
            Toast.makeText(ProcessManagerActivity.this, "显示用户和系统进程", Toast.LENGTH_SHORT).show();
            showUserProc = false;
        }
        reFresh();
    }


    class MyAsyncTask extends AsyncTask<Void,Void,Void>{

        public MyAsyncTask() {
            super();
        }

        @Override
        protected Void doInBackground(Void... params) {



             //初始化进程数据集
            runningProcess     =ProcessUtils.getAllProcess(ProcessManagerActivity.this);
            runningProcess_sys =new ArrayList<>();
            runningProcess_user=new ArrayList<>();

            //根据isSystem来分组
            for (ProcessInfo procInfo:runningProcess){
                if (procInfo.isSystem()){
                    runningProcess_sys.add(procInfo);
                }else runningProcess_user.add(procInfo);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String processNum="当前手机进程数\r\n"+ ProcessUtils.getAllProcessNum(ProcessManagerActivity.this);
            String spaceRAM="可用RAM/总RAM\r\n"+ ProcessUtils.getAvailableRAM(ProcessManagerActivity.this)+
                    "/"+ProcessUtils.getTotalRAM(ProcessManagerActivity.this);

            tv_process_processnum.setText(processNum);
            tv_process_spaceRAM.setText(spaceRAM);

            //便于刷新,不需要重新new一个adapter，不至于每次都回到第一条
            if (myBaseAdapter==null){
                myBaseAdapter = new MyBaseAdapter();
                lv_process_content.setAdapter(myBaseAdapter);
            } else
                myBaseAdapter.notifyDataSetChanged();

            //设置滚动监听器
            lv_process_content.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    if (!showUserProc){
                        if (firstVisibleItem > runningProcess_user.size())
                            tv_process_reminder.setText("系统的应用：" + runningProcess_sys.size() + "个");
                        else
                            tv_process_reminder.setText("用户安装的应用：" + runningProcess_user.size() + "个");
                    }
                    else {
                        tv_process_reminder.setText("用户安装的应用：" + runningProcess_user.size() + "个");
                    }

                }
            });


            //设置item的点击监听器，对checkbox的状态进行改变
            lv_process_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ProcessInfo curr_click_processinfo = null;

                    //获取被点击的应用info
                    if (!showUserProc) {
                        if (0 < position && position < runningProcess_user.size() + 1) {
                            curr_click_processinfo = runningProcess_user.get(position - 1);
                        } else if (runningProcess_user.size() + 1 < position && position < runningProcess.size() + 2) {
                            curr_click_processinfo = runningProcess_sys.get(position - runningProcess_user.size() - 2);
                        }
                    } else {
                        curr_click_processinfo = runningProcess_user.get(position - 1);
                    }

                    //把info对象的ischeckbox 的状态改变
                    CheckBox cb_item_process = (CheckBox) view.findViewById(R.id.cb_item_process);
                    if (curr_click_processinfo.isChecked()) {
                        curr_click_processinfo.setIsChecked(false);
                        cb_item_process.setChecked(false);
                    } else {
                        curr_click_processinfo.setIsChecked(true);
                        cb_item_process.setChecked(true);
                    }

                }
            });
        }

    }


    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            //给定一个flag 根据flag选择不同语句执行
            if (!showUserProc)
                return runningProcess.size()+2;
            else
                return runningProcess_user.size()+1;
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

            Holder holder;
            View view;
            ProcessInfo processInfo=null;

            //显示全部进程
            if (!showUserProc){
                if (position==0){
                    TextView v=new TextView(ProcessManagerActivity.this);
                    v.setBackgroundColor(Color.GRAY);
                    v.setText("用户进程：" + runningProcess_user.size() + "个");
                    return v;
                } else if (position<runningProcess_user.size()+1){
                    processInfo  = runningProcess_user.get(position - 1);
                } else if (position==runningProcess_user.size()+1){
                    TextView v=new TextView(ProcessManagerActivity.this);
                    v.setText("系统进程："+runningProcess_sys.size()+"个");
                    v.setBackgroundColor(Color.GRAY);
                    return v;
                } else if (position<runningProcess.size()+2){
                    processInfo  = runningProcess_sys.get(position - runningProcess_user.size()-2);
                }
            }
            //只显示用户进程
            else {
                if (position==0){
                    TextView v=new TextView(ProcessManagerActivity.this);
                    v.setBackgroundColor(Color.GRAY);
                    v.setText("用户进程：" + runningProcess_user.size() + "个");
                    return v;
                } else if (position<runningProcess_user.size()+1){
                    processInfo  = runningProcess_user.get(position - 1);
                }
            }


            //因为在listView中有两种view的对象 所有要做判断
            if (convertView!=null&& convertView instanceof RelativeLayout){
                view=convertView;
                holder = (Holder) view.getTag();
            }else {

                holder=new Holder();
                view= View.inflate(ProcessManagerActivity.this, R.layout.listitem_appinfo_process, null);

                holder.iv_item_process_icon       = (ImageView) view.findViewById(R.id.iv_item_process_icon);
                holder.tv_item_process_label      = (TextView) view.findViewById(R.id.tv_item_process_label);
                holder.tv_item_process_ramSize = (TextView) view.findViewById(R.id.tv_item_process_ramSize);
                holder.cb_item_process       = (CheckBox)view.findViewById(R.id.cb_item_process);
                view.setTag(holder);
            }

            if (processInfo!=null){//TODO 等于null？？
                holder.iv_item_process_icon.setBackground(processInfo.getIcon());
                holder.tv_item_process_label      .setText(processInfo.getName());
                holder.tv_item_process_ramSize.setText(processInfo.getRamsize()+"KB");
                if (processInfo.isChecked()) holder.cb_item_process.setChecked(true);
                else holder.cb_item_process.setChecked(false);
            }

            return view;
        }
    }

    class Holder{
        private ImageView iv_item_process_icon;
        private TextView tv_item_process_label;
        private TextView tv_item_process_ramSize;
        private CheckBox cb_item_process;
    }


}
