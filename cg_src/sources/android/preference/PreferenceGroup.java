package android.preference;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/PreferenceGroup.class */
public abstract class PreferenceGroup extends Preference {
    public PreferenceGroup(Context context, AttributeSet attrs, int defStyle) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public PreferenceGroup(Context context, AttributeSet attrs) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public void setOrderingAsAdded(boolean orderingAsAdded) {
        throw new RuntimeException("Stub!");
    }

    public boolean isOrderingAsAdded() {
        throw new RuntimeException("Stub!");
    }

    public void addItemFromInflater(Preference preference) {
        throw new RuntimeException("Stub!");
    }

    public int getPreferenceCount() {
        throw new RuntimeException("Stub!");
    }

    public Preference getPreference(int index) {
        throw new RuntimeException("Stub!");
    }

    public boolean addPreference(Preference preference) {
        throw new RuntimeException("Stub!");
    }

    public boolean removePreference(Preference preference) {
        throw new RuntimeException("Stub!");
    }

    public void removeAll() {
        throw new RuntimeException("Stub!");
    }

    protected boolean onPrepareAddPreference(Preference preference) {
        throw new RuntimeException("Stub!");
    }

    public Preference findPreference(CharSequence key) {
        throw new RuntimeException("Stub!");
    }

    protected boolean isOnSameScreenAsChildren() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected void onAttachedToActivity() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected void onPrepareForRemoval() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    public void setEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    protected void dispatchSaveInstanceState(Bundle container) {
        throw new RuntimeException("Stub!");
    }

    protected void dispatchRestoreInstanceState(Bundle container) {
        throw new RuntimeException("Stub!");
    }
}
