package com.example.android.popularmovie;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.android.popularmovie.baseClass.AppCompatPreferenceActivity;

/**
 * Created by Administrator on 2017/1/6.
 */

public class SettingsActivity extends AppCompatPreferenceActivity
        implements Preference.OnPreferenceChangeListener{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // addPreferencesFromResource(R.xml.preferences);

        // Add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_key)));

        setupActionBar();
        System.out.println("SettingsActivity：onCreate执行了");

    }
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("SettingsActivity：onStart执行了");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("SettingsActivity：onResume执行了");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("SettingsActivity：onPause执行了");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("SettingsActivity：onStop执行了");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("SettingsActivity：onDestroy执行了");
    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        //第一次上来没有改变的情况下，设置Summary
        onPreferenceChange(preference,
                //拿到sharedPreferences对象，接着拿到值
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));//从首选项中获取值。
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);

            if (prefIndex >=0) {//如果未找到，则为-1。
                //entries是选项的名字，用来显示的，而 entryValues 是选项对应的 值
                // 用来在代码中进行计算或者其它的作用的
                //listPreference.getEntries()返回CharSequence[]字符序列数组
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
           // finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
