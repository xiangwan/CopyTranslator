package com.definebytime.copytranslator;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.HashMap;
import java.util.Map;


public class SettingActivity extends ActionBarActivity {

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private HashMap<String, String> apps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        apps=Utils.getAllApps(getApplicationContext());
        adapter=new MyAdapter(getApplicationContext(),apps);
        recyclerView= (RecyclerView)findViewById(R.id.recylerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        swipeLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              apps=  Utils.getAllApps(getApplicationContext());
                adapter.notifyDataSetChanged();
            }
        });

    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        public  final CheckBox checkbox;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.checkbox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }
    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        private final Context context;
        HashMap<String,String> map;
        public MyAdapter(Context context, HashMap<String,String> map){
            this.map=map;
            this.context=context;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(
                    R.layout.activity_setting_item, viewGroup,false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
            if (map==null||map.size()==0||myViewHolder==null)return;

           myViewHolder.checkbox.setText(map.get(map.keySet().toArray()[i]));
        }

        @Override
        public int getItemCount() {
            return map.size();
        }
    }
}
