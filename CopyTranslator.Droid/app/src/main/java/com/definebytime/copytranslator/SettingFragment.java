package com.definebytime.copytranslator;

import android.os.Bundle;
import android.preference.PreferenceFragment;


public class SettingFragment extends PreferenceFragment {




    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsActivity activity = (SettingsActivity) getActivity();
        String settings = getArguments().getString("SettingKey");
        if ("about".equals(settings))
        {
            activity.getToolBar().setTitle(R.string.about);
            addPreferencesFromResource(R.xml.setting_about);
            String versionName;
            try
            {
                versionName = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
            }
            catch (Exception e)
            {
                versionName = getString(R.string.about_version_number_unknown);
            }

            findPreference("version").setSummary(versionName);
        }
        else if ("normal".equals(settings))
        {
            activity.getToolBar().setTitle(R.string.setting_normal);
           addPreferencesFromResource(R.xml.setting_normal);
        }

        //读取首选项值
        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //String syncConnPref = sharedPref.getString(SettingsActivity.KEY_PREF_SYNC_CONN, "");

        //首选项变化事件 ：
/*                            @Override
                protected void onResume() {
                    super.onResume();
                    getPreferenceScreen().getSharedPreferences()
                            .registerOnSharedPreferenceChangeListener(this);
                }

                @Override
                protected void onPause() {
                    super.onPause();
                    getPreferenceScreen().getSharedPreferences()
                            .unregisterOnSharedPreferenceChangeListener(this);
                }*/
    }
}
