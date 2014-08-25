package edu.virginia.cs2110.marbledungeon;

import android.app.Activity;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * A simple settings selection screen. Currently its only implementation 
 * is to select "Normal" or "Hardcore" difficulty levels. 
 * @author Kevin M. Goehring, Alishan Hassan, Nikhil Padmanabhan
 *
 */

public class SettingsScreen extends PreferenceActivity {
	public static final String KEY_PREF_HARDCORE = "pref_hardcore";
	public static String hardcoreCalled;
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference(KEY_PREF_HARDCORE);

        checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() 
        {            
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
            	if (newValue.equals(true))
            	{
            		Toast.makeText(SettingsScreen.this, "May the light guide you, my 1337 marble.", Toast.LENGTH_LONG).show();
            		hardcoreCalled = "yes";
            	}
            	else if (newValue.equals(false))
            	{
            		Toast.makeText(SettingsScreen.this, "Oh boo hoo hoo.", Toast.LENGTH_LONG).show();
            		hardcoreCalled = "no";
            	}
                return true;
            }
        }); 
	}
}

