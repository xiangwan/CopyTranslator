package com.definebytime.copytranslator;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
private   String SERVICE_NAME="com.definebytime.copytranslator.ClipChangedListenerService";
    private View btnStart;
    private View btnStop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setIcon(R.drawable.ic_home);


        btnStart =  this.findViewById(R.id.btnStartService);
        btnStop =  this.findViewById(R.id.btnStopService);
        if (Utils.isServiceRun(MainActivity.this,SERVICE_NAME)){
            btnStart.setEnabled(false);
        }else{
            btnStop.setEnabled(false);
        }
    }


    public void startClipService(View view) {
        if (Utils.isServiceRun(MainActivity.this,SERVICE_NAME)){
            Toast.makeText(getApplicationContext(), "服务已经在运行！", Toast.LENGTH_SHORT).show();
            return;
        }
         Intent intent=new Intent(MainActivity.this,ClipChangedListenerService.class);
        startService(intent);
        Toast.makeText(getApplicationContext(), "成功开启服务！", Toast.LENGTH_SHORT).show();
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
    }

    public void stopClipService(View view) {
        if (!Utils.isServiceRun(MainActivity.this,SERVICE_NAME)){
            Toast.makeText(getApplicationContext(), "服务没有在运行，不用停止！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent(MainActivity.this,ClipChangedListenerService.class);
        stopService(intent);
        btnStop.setEnabled(false);
        btnStart.setEnabled(true);
        Toast.makeText(getApplicationContext(), "成功停止服务！", Toast.LENGTH_SHORT).show();
    }

    public void openSetting(View view) {
        startActivity(new Intent(MainActivity.this,SettingsActivity.class));
    }
}
