/*
 * Copyright (C) 2013 SlimRoms Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.terminus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.util.Log;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class VolumeKeys extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "VolumeKeys";


    private static final String KEY_WAKEUP_CATEGORY = "category_wakeup_options";
    private static final String KEY_VOL_MEDIA = "volume_keys_control_media_stream";
    private static final String KEY_VOLUME_WAKE = "pref_volume_wake";

    // volume adjust sound
    private static final String KEY_MEDIA_CATEGORY = "category_wakeup_options";
    private static final String VOLUME_KEY_ADJUST_SOUND = "volume_key_adjust_sound";

    // cursor control
    private static final String KEY_CURSOR_CATEGORY = "category_cursor_options";
    private static final String VOLUME_KEY_CURSOR_CONTROL = "volume_key_cursor_control";

    private PreferenceCategory mWakeUpOptions;
    private SwitchPreference mVolumeKeysControlMedia;
    private SwitchPreference mVolumeWake;
    private SwitchPreference mVolumeKeyAdjustSound;
    private ListPreference mVolumeKeyCursorControl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.volume_keys);

        // volume control media
        mVolumeKeysControlMedia = (SwitchPreference) findPreference(KEY_VOL_MEDIA);
        mVolumeKeysControlMedia.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.VOLUME_KEYS_CONTROL_MEDIA_STREAM, 0) != 0);
        mVolumeKeysControlMedia.setOnPreferenceChangeListener(this);

        // volume wake options
        mWakeUpOptions = (PreferenceCategory) getPreferenceScreen().findPreference(KEY_WAKEUP_CATEGORY);
        mVolumeWake = (SwitchPreference) findPreference(KEY_VOLUME_WAKE);
        mVolumeWake.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.VOLUME_WAKE_SCREEN, 0) == 1);
        mVolumeWake.setOnPreferenceChangeListener(this);

        // volume adjust sound
        mVolumeKeyAdjustSound = (SwitchPreference) findPreference(VOLUME_KEY_ADJUST_SOUND);
        mVolumeKeyAdjustSound.setOnPreferenceChangeListener(this);
        mVolumeKeyAdjustSound.setChecked(Settings.System.getInt(getContentResolver(),
                VOLUME_KEY_ADJUST_SOUND, 1) != 0);

        // cursor control
        mVolumeKeyCursorControl = (ListPreference) findPreference(VOLUME_KEY_CURSOR_CONTROL);
        if (mVolumeKeyCursorControl != null) {
            mVolumeKeyCursorControl.setOnPreferenceChangeListener(this);
            mVolumeKeyCursorControl.setValue(Integer.toString(Settings.System.getInt(getActivity()
                    .getContentResolver(), Settings.System.VOLUME_KEY_CURSOR_CONTROL, 0)));
            mVolumeKeyCursorControl.setSummary(mVolumeKeyCursorControl.getEntry());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        // If we didn't handle it, let preferences handle it.
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        if (KEY_VOL_MEDIA.equals(key)) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.VOLUME_KEYS_CONTROL_MEDIA_STREAM,
                    (Boolean) objValue ? 1 : 0);
            return true;
        }
        else if (KEY_VOLUME_WAKE.equals(key)) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.VOLUME_WAKE_SCREEN,
                    (Boolean) objValue ? 1 : 0);
            return true;
        }
        else if (preference == mVolumeKeyAdjustSound) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getContentResolver(), VOLUME_KEY_ADJUST_SOUND,
                    value ? 1: 0);
            return true;
        }
        else if (preference == mVolumeKeyCursorControl) {
            String volumeKeyCursorControl = (String) objValue;
            int volumeKeyCursorControlValue = Integer.parseInt(volumeKeyCursorControl);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.VOLUME_KEY_CURSOR_CONTROL, volumeKeyCursorControlValue);
            int volumeKeyCursorControlIndex = mVolumeKeyCursorControl.findIndexOfValue(volumeKeyCursorControl);
            mVolumeKeyCursorControl.setSummary(mVolumeKeyCursorControl.getEntries()[volumeKeyCursorControlIndex]);
            return true;
        }
        return true;
    }

}
