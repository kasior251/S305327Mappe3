package com.example.kasia.s305327mappe3;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Kasia on 18.10.2017.
 */

public class ShowPreferences extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment {

        CheckBoxPreference checkBoxPreference;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            checkBoxPreference = (CheckBoxPreference) findPreference("checkbox_preference");

            checkBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {

                    if (o.toString().equals("true")) {
                        MainActivity.setPush(true);
                        Toast.makeText(getContext(), "The value is " + MainActivity.getPush(), Toast.LENGTH_SHORT).show();

                    }
                    else {
                        MainActivity.setPush(false);
                        Toast.makeText(getContext(), "The value is " + MainActivity.getPush(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
        }
    }

}