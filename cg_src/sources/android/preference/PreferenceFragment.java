package android.preference;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/PreferenceFragment.class */
public abstract class PreferenceFragment extends Fragment {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/PreferenceFragment$OnPreferenceStartFragmentCallback.class */
    public interface OnPreferenceStartFragmentCallback {
        boolean onPreferenceStartFragment(PreferenceFragment preferenceFragment, Preference preference);
    }

    public PreferenceFragment() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onStart() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onStop() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        throw new RuntimeException("Stub!");
    }

    public PreferenceManager getPreferenceManager() {
        throw new RuntimeException("Stub!");
    }

    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        throw new RuntimeException("Stub!");
    }

    public PreferenceScreen getPreferenceScreen() {
        throw new RuntimeException("Stub!");
    }

    public void addPreferencesFromIntent(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    public void addPreferencesFromResource(int preferencesResId) {
        throw new RuntimeException("Stub!");
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        throw new RuntimeException("Stub!");
    }

    public Preference findPreference(CharSequence key) {
        throw new RuntimeException("Stub!");
    }
}
