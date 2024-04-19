package com.taskdemo.practical.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;

import com.taskdemo.practical.R;

;

public class SettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences defaultSharedPreferences;
    SharedPreferences.Editor editor;

    public void onPostCreate(Bundle bundle) {
        CardView appBarLayout;
        int i;
        super.onPostCreate(bundle);
        //        if (Build.VERSION.SDK_INT >= 15) {
        @SuppressLint("ResourceType") LinearLayout linearLayout = (LinearLayout) findViewById(16908298).getParent().getParent().getParent();
        appBarLayout = (CardView) LayoutInflater.from(this).inflate(R.layout.toolbar_setting, (ViewGroup) linearLayout, false);
//            MainInterface_v2.Show_native(this, appBarLayout.findViewById(R.id.Admob_Native_Frame_two), com.facebook.shimmer.config.R.layout.custom_native_admob_free_size, com.facebook.shimmer.config.R.layout.custom_native_admob_free_size_simmer, MainInterface_v2.Banner_size_banner.MEDIUM_RECTANGLE_250);
        linearLayout.addView(appBarLayout, 0);

        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
        editor = defaultSharedPreferences.edit();

        setupSimplePreferencesScreen();
        preferenceClickEvent();
    }

    private void preferenceClickEvent() {

    }

    private void setupSimplePreferencesScreen() {

        addPreferencesFromResource(R.xml.pref_setting);
        PreferenceManager.setDefaultValues(this, R.xml.pref_setting, false);
        initSummary(getPreferenceScreen());
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        View onCreateView = super.onCreateView(str, context, attributeSet);

        if (onCreateView != null) {
            return onCreateView;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return null;
        }
        if (str.equals("EditText")) {
            return new AppCompatEditText(this, attributeSet);
        }
        if (str.equals("Spinner")) {
            return new AppCompatSpinner(this, attributeSet);
        }
        if (str.equals("CheckBox")) {
            return new AppCompatCheckBox(this, attributeSet);
        }
        if (str.equals("RadioButton")) {
            return new AppCompatRadioButton(this, attributeSet);
        }
        if (str.equals("CheckedTextView")) {
            return new AppCompatCheckedTextView(this, attributeSet);
        }
//        if (str.equals("FrameLayout")) {
//            return new FrameLayout(this, attributeSet);
//        }
        return null;
    }

    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    public void onBackPressed() {

        finish();

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        updatePrefSummary(findPreference(str));
    }

    private void initSummary(Preference preference) {
        if (preference instanceof PreferenceGroup) {
            PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
            for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
                initSummary(preferenceGroup.getPreference(i));
            }
            return;
        }
        updatePrefSummary(preference);
    }

    private void updatePrefSummary(Preference preference) {
        if (preference instanceof ListPreference) {
            preference.setSummary(((ListPreference) preference).getEntry());
        }
        if (preference instanceof EditTextPreference) {
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            if (preference.getTitle().toString().toLowerCase().contains("password")) {
                preference.setSummary("******");
            } else {
                preference.setSummary(editTextPreference.getText());
            }
        }
        if (preference instanceof MultiSelectListPreference) {
            preference.setSummary(((EditTextPreference) preference).getText());
        }
    }

}
