package android.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/EditTextPreference.class */
public class EditTextPreference extends DialogPreference {
    public EditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }

    public EditTextPreference(Context context, AttributeSet attrs) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }

    public EditTextPreference(Context context) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }

    public void setText(String text) {
        throw new RuntimeException("Stub!");
    }

    public String getText() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.DialogPreference
    protected void onBindDialogView(View view) {
        throw new RuntimeException("Stub!");
    }

    protected void onAddEditTextToDialogView(View dialogView, EditText editText) {
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

    @Override // android.preference.Preference
    public boolean shouldDisableDependents() {
        throw new RuntimeException("Stub!");
    }

    public EditText getEditText() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.DialogPreference, android.preference.Preference
    protected Parcelable onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.DialogPreference, android.preference.Preference
    protected void onRestoreInstanceState(Parcelable state) {
        throw new RuntimeException("Stub!");
    }
}
