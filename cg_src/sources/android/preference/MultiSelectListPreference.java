package android.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/MultiSelectListPreference.class */
public class MultiSelectListPreference extends DialogPreference {
    public MultiSelectListPreference(Context context, AttributeSet attrs) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }

    public MultiSelectListPreference(Context context) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }

    public void setEntries(CharSequence[] entries) {
        throw new RuntimeException("Stub!");
    }

    public void setEntries(int entriesResId) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence[] getEntries() {
        throw new RuntimeException("Stub!");
    }

    public void setEntryValues(CharSequence[] entryValues) {
        throw new RuntimeException("Stub!");
    }

    public void setEntryValues(int entryValuesResId) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence[] getEntryValues() {
        throw new RuntimeException("Stub!");
    }

    public void setValues(Set<String> values) {
        throw new RuntimeException("Stub!");
    }

    public Set<String> getValues() {
        throw new RuntimeException("Stub!");
    }

    public int findIndexOfValue(String value) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.DialogPreference
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.DialogPreference
    protected void onDialogClosed(boolean positiveResult) {
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

    @Override // android.preference.DialogPreference, android.preference.Preference
    protected Parcelable onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }
}
