package android.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/TwoStatePreference.class */
public abstract class TwoStatePreference extends Preference {
    public TwoStatePreference(Context context, AttributeSet attrs, int defStyle) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public TwoStatePreference(Context context, AttributeSet attrs) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public TwoStatePreference(Context context) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected void onClick() {
        throw new RuntimeException("Stub!");
    }

    public void setChecked(boolean checked) {
        throw new RuntimeException("Stub!");
    }

    public boolean isChecked() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    public boolean shouldDisableDependents() {
        throw new RuntimeException("Stub!");
    }

    public void setSummaryOn(CharSequence summary) {
        throw new RuntimeException("Stub!");
    }

    public void setSummaryOn(int summaryResId) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getSummaryOn() {
        throw new RuntimeException("Stub!");
    }

    public void setSummaryOff(CharSequence summary) {
        throw new RuntimeException("Stub!");
    }

    public void setSummaryOff(int summaryResId) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getSummaryOff() {
        throw new RuntimeException("Stub!");
    }

    public boolean getDisableDependentsState() {
        throw new RuntimeException("Stub!");
    }

    public void setDisableDependentsState(boolean disableDependentsState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected Object onGetDefaultValue(TypedArray a, int index) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected Parcelable onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected void onRestoreInstanceState(Parcelable state) {
        throw new RuntimeException("Stub!");
    }
}
