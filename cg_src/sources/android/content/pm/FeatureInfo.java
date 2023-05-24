package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/FeatureInfo.class */
public class FeatureInfo implements Parcelable {
    public String name;
    public static final int GL_ES_VERSION_UNDEFINED = 0;
    public int reqGlEsVersion;
    public static final int FLAG_REQUIRED = 1;
    public int flags;
    public static final Parcelable.Creator<FeatureInfo> CREATOR = null;

    public FeatureInfo() {
        throw new RuntimeException("Stub!");
    }

    public FeatureInfo(FeatureInfo orig) {
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
