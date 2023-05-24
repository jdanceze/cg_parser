package android.preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/PreferenceManager.class */
public class PreferenceManager {
    public static final String METADATA_KEY_PREFERENCES = "android.preference";
    public static final String KEY_HAS_SET_DEFAULT_VALUES = "_has_set_default_values";

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/PreferenceManager$OnActivityDestroyListener.class */
    public interface OnActivityDestroyListener {
        void onActivityDestroy();
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/PreferenceManager$OnActivityResultListener.class */
    public interface OnActivityResultListener {
        boolean onActivityResult(int i, int i2, Intent intent);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/PreferenceManager$OnActivityStopListener.class */
    public interface OnActivityStopListener {
        void onActivityStop();
    }

    PreferenceManager() {
        throw new RuntimeException("Stub!");
    }

    public PreferenceScreen createPreferenceScreen(Context context) {
        throw new RuntimeException("Stub!");
    }

    public String getSharedPreferencesName() {
        throw new RuntimeException("Stub!");
    }

    public void setSharedPreferencesName(String sharedPreferencesName) {
        throw new RuntimeException("Stub!");
    }

    public int getSharedPreferencesMode() {
        throw new RuntimeException("Stub!");
    }

    public void setSharedPreferencesMode(int sharedPreferencesMode) {
        throw new RuntimeException("Stub!");
    }

    public SharedPreferences getSharedPreferences() {
        throw new RuntimeException("Stub!");
    }

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        throw new RuntimeException("Stub!");
    }

    public Preference findPreference(CharSequence key) {
        throw new RuntimeException("Stub!");
    }

    public static void setDefaultValues(Context context, int resId, boolean readAgain) {
        throw new RuntimeException("Stub!");
    }

    public static void setDefaultValues(Context context, String sharedPreferencesName, int sharedPreferencesMode, int resId, boolean readAgain) {
        throw new RuntimeException("Stub!");
    }
}
