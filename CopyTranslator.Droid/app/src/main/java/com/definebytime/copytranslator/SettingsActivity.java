package com.definebytime.copytranslator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import java.util.List;

public class SettingsActivity extends MyPreferenceActivity {
    private Toolbar toolbar;
    public Toolbar getToolBar(){
        return toolbar;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.setting,target);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.activity_setting, new LinearLayout(this), false);


        toolbar = (Toolbar)contentView.findViewById (R.id.action_bar_setting);
        ViewGroup contentWrapper =(ViewGroup) contentView.findViewById (R.id.content_wrapper);
        LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);

        getWindow().setContentView(contentView);
        toolbar.setTitle(R.string.setting_title);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       // getListView().setBackgroundColor(Color.WHITE);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {

        return true;
    }
}
