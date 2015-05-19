package edu.washington.mikhail3.quizdroid;
import android.os.Bundle;
import android.preference.*;
import android.util.Log;
import android.widget.Toast;

public class prefActivity extends PreferenceActivity {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupPreferencesScreen();
        //Log
    }
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String StrVal = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference lp = (ListPreference) preference;
                int index = lp.findIndexOfValue(StrVal);
                preference.setSummary(index >= 0 ? lp.getEntries()[index] : null);
            } else {
                preference.setSummary(StrVal);
            }
            return true;
        }
    };
    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }
    private void setupPreferencesScreen() {
        addPreferencesFromResource(R.xml.container);
        PreferenceCategory head = new PreferenceCategory(this);
        head.setTitle("Downloads and Data");
        getPreferenceScreen().addPreference(head);
        addPreferencesFromResource(R.xml.pref_downloads);
        bindPreferenceSummaryToValue(findPreference("down_freq"));
        bindPreferenceSummaryToValue(findPreference("down_url"));
    }
}