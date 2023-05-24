package android.preference;

import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.ListView;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/PreferenceActivity.class */
public abstract class PreferenceActivity extends ListActivity implements PreferenceFragment.OnPreferenceStartFragmentCallback {
    public static final String EXTRA_SHOW_FRAGMENT = ":android:show_fragment";
    public static final String EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":android:show_fragment_args";
    public static final String EXTRA_SHOW_FRAGMENT_TITLE = ":android:show_fragment_title";
    public static final String EXTRA_SHOW_FRAGMENT_SHORT_TITLE = ":android:show_fragment_short_title";
    public static final String EXTRA_NO_HEADERS = ":android:no_headers";
    public static final long HEADER_ID_UNDEFINED = -1;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/PreferenceActivity$Header.class */
    public static final class Header implements Parcelable {
        public long id;
        public int titleRes;
        public CharSequence title;
        public int summaryRes;
        public CharSequence summary;
        public int breadCrumbTitleRes;
        public CharSequence breadCrumbTitle;
        public int breadCrumbShortTitleRes;
        public CharSequence breadCrumbShortTitle;
        public int iconRes;
        public String fragment;
        public Bundle fragmentArguments;
        public Intent intent;
        public Bundle extras;
        public static final Parcelable.Creator<Header> CREATOR = null;

        public Header() {
            throw new RuntimeException("Stub!");
        }

        public CharSequence getTitle(Resources res) {
            throw new RuntimeException("Stub!");
        }

        public CharSequence getSummary(Resources res) {
            throw new RuntimeException("Stub!");
        }

        public CharSequence getBreadCrumbTitle(Resources res) {
            throw new RuntimeException("Stub!");
        }

        public CharSequence getBreadCrumbShortTitle(Resources res) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }

        public void readFromParcel(Parcel in) {
            throw new RuntimeException("Stub!");
        }
    }

    public PreferenceActivity() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    public boolean hasHeaders() {
        throw new RuntimeException("Stub!");
    }

    public boolean isMultiPane() {
        throw new RuntimeException("Stub!");
    }

    public boolean onIsMultiPane() {
        throw new RuntimeException("Stub!");
    }

    public boolean onIsHidingHeaders() {
        throw new RuntimeException("Stub!");
    }

    public Header onGetInitialHeader() {
        throw new RuntimeException("Stub!");
    }

    public Header onGetNewHeader() {
        throw new RuntimeException("Stub!");
    }

    public void onBuildHeaders(List<Header> target) {
        throw new RuntimeException("Stub!");
    }

    public void invalidateHeaders() {
        throw new RuntimeException("Stub!");
    }

    public void loadHeadersFromResource(int resid, List<Header> target) {
        throw new RuntimeException("Stub!");
    }

    public void setListFooter(View view) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onStop() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.ListActivity, android.app.Activity
    protected void onDestroy() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.ListActivity, android.app.Activity
    protected void onRestoreInstanceState(Bundle state) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.ListActivity, android.app.Activity, android.view.Window.Callback
    public void onContentChanged() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.ListActivity
    protected void onListItemClick(ListView l, View v, int position, long id) {
        throw new RuntimeException("Stub!");
    }

    public void onHeaderClick(Header header, int position) {
        throw new RuntimeException("Stub!");
    }

    public Intent onBuildStartFragmentIntent(String fragmentName, Bundle args, int titleRes, int shortTitleRes) {
        throw new RuntimeException("Stub!");
    }

    public void startWithFragment(String fragmentName, Bundle args, Fragment resultTo, int resultRequestCode) {
        throw new RuntimeException("Stub!");
    }

    public void startWithFragment(String fragmentName, Bundle args, Fragment resultTo, int resultRequestCode, int titleRes, int shortTitleRes) {
        throw new RuntimeException("Stub!");
    }

    public void showBreadCrumbs(CharSequence title, CharSequence shortTitle) {
        throw new RuntimeException("Stub!");
    }

    public void setParentTitle(CharSequence title, CharSequence shortTitle, View.OnClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void switchToHeader(String fragmentName, Bundle args) {
        throw new RuntimeException("Stub!");
    }

    public void switchToHeader(Header header) {
        throw new RuntimeException("Stub!");
    }

    public void startPreferenceFragment(Fragment fragment, boolean push) {
        throw new RuntimeException("Stub!");
    }

    public void startPreferencePanel(String fragmentClass, Bundle args, int titleRes, CharSequence titleText, Fragment resultTo, int resultRequestCode) {
        throw new RuntimeException("Stub!");
    }

    public void finishPreferencePanel(Fragment caller, int resultCode, Intent resultData) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.PreferenceFragment.OnPreferenceStartFragmentCallback
    public boolean onPreferenceStartFragment(PreferenceFragment caller, Preference pref) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public PreferenceManager getPreferenceManager() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public PreferenceScreen getPreferenceScreen() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void addPreferencesFromIntent(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void addPreferencesFromResource(int preferencesResId) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public Preference findPreference(CharSequence key) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        throw new RuntimeException("Stub!");
    }
}
