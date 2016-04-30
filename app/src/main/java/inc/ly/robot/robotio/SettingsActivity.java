package inc.ly.robot.robotio;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by HarmanS on 30/04/2016.
 */
public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.pref_general);

//        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
//        // updated when the preference changes.
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_location_key)));

    }

    @Override
    protected void onResume() {
        super.onResume();

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_url_key)));
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
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        SharedPreferences prefs = getSharedPreferences("test", MODE_PRIVATE);

        prefs.edit().putString(getString(R.string.pref_url_key), stringValue).commit();
        Log.d("SettingsActivity", "Updated url value: " + stringValue);

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
//            preferenc
        }
        return true;
    }

//    /**
//     * The method will get the parent activity intent for the up button behaviour. (back button on top)
//     *
//     * @return
//     */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public Intent getParentActivityIntent() {
//        /**
//         * We get the parent activity, which should create a new intent to main activity.
//         * WE add the intent flag which indicates that the system will check if the main activity
//         * is already running in our task and to use that one instead of creating a new main activity instance
//         * when we press the back button on the top bar.
//         * BECAUSE this method did not exist prior to jelly bean, therefore, we had to add the targetapi tag.
//         */
//        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    }

}