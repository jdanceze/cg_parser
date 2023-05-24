package android.content;

import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/SharedPreferences.class */
public interface SharedPreferences {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/SharedPreferences$Editor.class */
    public interface Editor {
        Editor putString(String str, String str2);

        Editor putStringSet(String str, Set<String> set);

        Editor putInt(String str, int i);

        Editor putLong(String str, long j);

        Editor putFloat(String str, float f);

        Editor putBoolean(String str, boolean z);

        Editor remove(String str);

        Editor clear();

        boolean commit();

        void apply();
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/SharedPreferences$OnSharedPreferenceChangeListener.class */
    public interface OnSharedPreferenceChangeListener {
        void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str);
    }

    Map<String, ?> getAll();

    String getString(String str, String str2);

    Set<String> getStringSet(String str, Set<String> set);

    int getInt(String str, int i);

    long getLong(String str, long j);

    float getFloat(String str, float f);

    boolean getBoolean(String str, boolean z);

    boolean contains(String str);

    Editor edit();

    void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener);

    void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener);
}
