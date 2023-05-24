package android.preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.AbsSavedState;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/Preference.class */
public class Preference implements Comparable<Preference> {
    public static final int DEFAULT_ORDER = Integer.MAX_VALUE;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/Preference$OnPreferenceChangeListener.class */
    public interface OnPreferenceChangeListener {
        boolean onPreferenceChange(Preference preference, Object obj);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/Preference$OnPreferenceClickListener.class */
    public interface OnPreferenceClickListener {
        boolean onPreferenceClick(Preference preference);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/Preference$BaseSavedState.class */
    public static class BaseSavedState extends AbsSavedState {
        public static final Parcelable.Creator<BaseSavedState> CREATOR = null;

        public BaseSavedState(Parcel source) {
            super((Parcel) null);
            throw new RuntimeException("Stub!");
        }

        public BaseSavedState(Parcelable superState) {
            super((Parcel) null);
            throw new RuntimeException("Stub!");
        }
    }

    public Preference(Context context, AttributeSet attrs, int defStyle) {
        throw new RuntimeException("Stub!");
    }

    public Preference(Context context, AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    public Preference(Context context) {
        throw new RuntimeException("Stub!");
    }

    protected Object onGetDefaultValue(TypedArray a, int index) {
        throw new RuntimeException("Stub!");
    }

    public void setIntent(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    public Intent getIntent() {
        throw new RuntimeException("Stub!");
    }

    public void setFragment(String fragment) {
        throw new RuntimeException("Stub!");
    }

    public String getFragment() {
        throw new RuntimeException("Stub!");
    }

    public Bundle getExtras() {
        throw new RuntimeException("Stub!");
    }

    public Bundle peekExtras() {
        throw new RuntimeException("Stub!");
    }

    public void setLayoutResource(int layoutResId) {
        throw new RuntimeException("Stub!");
    }

    public int getLayoutResource() {
        throw new RuntimeException("Stub!");
    }

    public void setWidgetLayoutResource(int widgetLayoutResId) {
        throw new RuntimeException("Stub!");
    }

    public int getWidgetLayoutResource() {
        throw new RuntimeException("Stub!");
    }

    public View getView(View convertView, ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }

    protected View onCreateView(ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }

    protected void onBindView(View view) {
        throw new RuntimeException("Stub!");
    }

    public void setOrder(int order) {
        throw new RuntimeException("Stub!");
    }

    public int getOrder() {
        throw new RuntimeException("Stub!");
    }

    public void setTitle(CharSequence title) {
        throw new RuntimeException("Stub!");
    }

    public void setTitle(int titleResId) {
        throw new RuntimeException("Stub!");
    }

    public int getTitleRes() {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getTitle() {
        throw new RuntimeException("Stub!");
    }

    public void setIcon(Drawable icon) {
        throw new RuntimeException("Stub!");
    }

    public void setIcon(int iconResId) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getIcon() {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getSummary() {
        throw new RuntimeException("Stub!");
    }

    public void setSummary(CharSequence summary) {
        throw new RuntimeException("Stub!");
    }

    public void setSummary(int summaryResId) {
        throw new RuntimeException("Stub!");
    }

    public void setEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean isEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setSelectable(boolean selectable) {
        throw new RuntimeException("Stub!");
    }

    public boolean isSelectable() {
        throw new RuntimeException("Stub!");
    }

    public void setShouldDisableView(boolean shouldDisableView) {
        throw new RuntimeException("Stub!");
    }

    public boolean getShouldDisableView() {
        throw new RuntimeException("Stub!");
    }

    protected void onClick() {
        throw new RuntimeException("Stub!");
    }

    public void setKey(String key) {
        throw new RuntimeException("Stub!");
    }

    public String getKey() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasKey() {
        throw new RuntimeException("Stub!");
    }

    public boolean isPersistent() {
        throw new RuntimeException("Stub!");
    }

    protected boolean shouldPersist() {
        throw new RuntimeException("Stub!");
    }

    public void setPersistent(boolean persistent) {
        throw new RuntimeException("Stub!");
    }

    protected boolean callChangeListener(Object newValue) {
        throw new RuntimeException("Stub!");
    }

    public void setOnPreferenceChangeListener(OnPreferenceChangeListener onPreferenceChangeListener) {
        throw new RuntimeException("Stub!");
    }

    public OnPreferenceChangeListener getOnPreferenceChangeListener() {
        throw new RuntimeException("Stub!");
    }

    public void setOnPreferenceClickListener(OnPreferenceClickListener onPreferenceClickListener) {
        throw new RuntimeException("Stub!");
    }

    public OnPreferenceClickListener getOnPreferenceClickListener() {
        throw new RuntimeException("Stub!");
    }

    public Context getContext() {
        throw new RuntimeException("Stub!");
    }

    public SharedPreferences getSharedPreferences() {
        throw new RuntimeException("Stub!");
    }

    public SharedPreferences.Editor getEditor() {
        throw new RuntimeException("Stub!");
    }

    public boolean shouldCommit() {
        throw new RuntimeException("Stub!");
    }

    @Override // java.lang.Comparable
    public int compareTo(Preference another) {
        throw new RuntimeException("Stub!");
    }

    protected void notifyChanged() {
        throw new RuntimeException("Stub!");
    }

    protected void notifyHierarchyChanged() {
        throw new RuntimeException("Stub!");
    }

    public PreferenceManager getPreferenceManager() {
        throw new RuntimeException("Stub!");
    }

    protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        throw new RuntimeException("Stub!");
    }

    protected void onAttachedToActivity() {
        throw new RuntimeException("Stub!");
    }

    protected Preference findPreferenceInHierarchy(String key) {
        throw new RuntimeException("Stub!");
    }

    public void notifyDependencyChange(boolean disableDependents) {
        throw new RuntimeException("Stub!");
    }

    public void onDependencyChanged(Preference dependency, boolean disableDependent) {
        throw new RuntimeException("Stub!");
    }

    public boolean shouldDisableDependents() {
        throw new RuntimeException("Stub!");
    }

    public void setDependency(String dependencyKey) {
        throw new RuntimeException("Stub!");
    }

    public String getDependency() {
        throw new RuntimeException("Stub!");
    }

    protected void onPrepareForRemoval() {
        throw new RuntimeException("Stub!");
    }

    public void setDefaultValue(Object defaultValue) {
        throw new RuntimeException("Stub!");
    }

    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        throw new RuntimeException("Stub!");
    }

    protected boolean persistString(String value) {
        throw new RuntimeException("Stub!");
    }

    protected String getPersistedString(String defaultReturnValue) {
        throw new RuntimeException("Stub!");
    }

    protected boolean persistInt(int value) {
        throw new RuntimeException("Stub!");
    }

    protected int getPersistedInt(int defaultReturnValue) {
        throw new RuntimeException("Stub!");
    }

    protected boolean persistFloat(float value) {
        throw new RuntimeException("Stub!");
    }

    protected float getPersistedFloat(float defaultReturnValue) {
        throw new RuntimeException("Stub!");
    }

    protected boolean persistLong(long value) {
        throw new RuntimeException("Stub!");
    }

    protected long getPersistedLong(long defaultReturnValue) {
        throw new RuntimeException("Stub!");
    }

    protected boolean persistBoolean(boolean value) {
        throw new RuntimeException("Stub!");
    }

    protected boolean getPersistedBoolean(boolean defaultReturnValue) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public void saveHierarchyState(Bundle container) {
        throw new RuntimeException("Stub!");
    }

    protected Parcelable onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }

    public void restoreHierarchyState(Bundle container) {
        throw new RuntimeException("Stub!");
    }

    protected void onRestoreInstanceState(Parcelable state) {
        throw new RuntimeException("Stub!");
    }
}
