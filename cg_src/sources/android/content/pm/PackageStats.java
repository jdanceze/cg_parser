package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/PackageStats.class */
public class PackageStats implements Parcelable {
    public String packageName;
    public long codeSize;
    public long dataSize;
    public long cacheSize;
    public long externalCodeSize;
    public long externalDataSize;
    public long externalCacheSize;
    public long externalMediaSize;
    public long externalObbSize;
    public static final Parcelable.Creator<PackageStats> CREATOR = null;

    public PackageStats(String pkgName) {
        throw new RuntimeException("Stub!");
    }

    public PackageStats(Parcel source) {
        throw new RuntimeException("Stub!");
    }

    public PackageStats(PackageStats pStats) {
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
}
