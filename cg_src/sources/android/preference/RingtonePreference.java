package android.preference;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/RingtonePreference.class */
public class RingtonePreference extends Preference implements PreferenceManager.OnActivityResultListener {
    public RingtonePreference(Context context, AttributeSet attrs, int defStyle) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public RingtonePreference(Context context, AttributeSet attrs) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public RingtonePreference(Context context) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public int getRingtoneType() {
        throw new RuntimeException("Stub!");
    }

    public void setRingtoneType(int type) {
        throw new RuntimeException("Stub!");
    }

    public boolean getShowDefault() {
        throw new RuntimeException("Stub!");
    }

    public void setShowDefault(boolean showDefault) {
        throw new RuntimeException("Stub!");
    }

    public boolean getShowSilent() {
        throw new RuntimeException("Stub!");
    }

    public void setShowSilent(boolean showSilent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected void onClick() {
        throw new RuntimeException("Stub!");
    }

    protected void onPrepareRingtonePickerIntent(Intent ringtonePickerIntent) {
        throw new RuntimeException("Stub!");
    }

    protected void onSaveRingtone(Uri ringtoneUri) {
        throw new RuntimeException("Stub!");
    }

    protected Uri onRestoreRingtone() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected Object onGetDefaultValue(TypedArray a, int index) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValueObj) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.PreferenceManager.OnActivityResultListener
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        throw new RuntimeException("Stub!");
    }
}
