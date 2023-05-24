package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/ConfigurationInfo.class */
public class ConfigurationInfo implements Parcelable {
    public int reqTouchScreen;
    public int reqKeyboardType;
    public int reqNavigation;
    public static final int INPUT_FEATURE_HARD_KEYBOARD = 1;
    public static final int INPUT_FEATURE_FIVE_WAY_NAV = 2;
    public int reqInputFeatures;
    public static final int GL_ES_VERSION_UNDEFINED = 0;
    public int reqGlEsVersion;
    public static final Parcelable.Creator<ConfigurationInfo> CREATOR = null;

    public ConfigurationInfo() {
        throw new RuntimeException("Stub!");
    }

    public ConfigurationInfo(ConfigurationInfo orig) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        throw new RuntimeException("Stub!");
    }

    public String getGlEsVersion() {
        throw new RuntimeException("Stub!");
    }
}
