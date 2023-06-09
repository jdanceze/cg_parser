package android.preference;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/DialogPreference.class */
public abstract class DialogPreference extends Preference implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, PreferenceManager.OnActivityDestroyListener {
    public DialogPreference(Context context, AttributeSet attrs, int defStyle) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public DialogPreference(Context context, AttributeSet attrs) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public void setDialogTitle(CharSequence dialogTitle) {
        throw new RuntimeException("Stub!");
    }

    public void setDialogTitle(int dialogTitleResId) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getDialogTitle() {
        throw new RuntimeException("Stub!");
    }

    public void setDialogMessage(CharSequence dialogMessage) {
        throw new RuntimeException("Stub!");
    }

    public void setDialogMessage(int dialogMessageResId) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getDialogMessage() {
        throw new RuntimeException("Stub!");
    }

    public void setDialogIcon(Drawable dialogIcon) {
        throw new RuntimeException("Stub!");
    }

    public void setDialogIcon(int dialogIconRes) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getDialogIcon() {
        throw new RuntimeException("Stub!");
    }

    public void setPositiveButtonText(CharSequence positiveButtonText) {
        throw new RuntimeException("Stub!");
    }

    public void setPositiveButtonText(int positiveButtonTextResId) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getPositiveButtonText() {
        throw new RuntimeException("Stub!");
    }

    public void setNegativeButtonText(CharSequence negativeButtonText) {
        throw new RuntimeException("Stub!");
    }

    public void setNegativeButtonText(int negativeButtonTextResId) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getNegativeButtonText() {
        throw new RuntimeException("Stub!");
    }

    public void setDialogLayoutResource(int dialogLayoutResId) {
        throw new RuntimeException("Stub!");
    }

    public int getDialogLayoutResource() {
        throw new RuntimeException("Stub!");
    }

    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected void onClick() {
        throw new RuntimeException("Stub!");
    }

    protected void showDialog(Bundle state) {
        throw new RuntimeException("Stub!");
    }

    protected View onCreateDialogView() {
        throw new RuntimeException("Stub!");
    }

    protected void onBindDialogView(View view) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        throw new RuntimeException("Stub!");
    }

    protected void onDialogClosed(boolean positiveResult) {
        throw new RuntimeException("Stub!");
    }

    public Dialog getDialog() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.PreferenceManager.OnActivityDestroyListener
    public void onActivityDestroy() {
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
