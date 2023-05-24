package android.content;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/SyncAdapterType.class */
public class SyncAdapterType implements Parcelable {
    public final String authority;
    public final String accountType;
    public final boolean isKey;
    public static final Parcelable.Creator<SyncAdapterType> CREATOR = null;

    public SyncAdapterType(String authority, String accountType, boolean userVisible, boolean supportsUploading) {
        throw new RuntimeException("Stub!");
    }

    public SyncAdapterType(Parcel source) {
        throw new RuntimeException("Stub!");
    }

    public boolean supportsUploading() {
        throw new RuntimeException("Stub!");
    }

    public boolean isUserVisible() {
        throw new RuntimeException("Stub!");
    }

    public boolean allowParallelSyncs() {
        throw new RuntimeException("Stub!");
    }

    public boolean isAlwaysSyncable() {
        throw new RuntimeException("Stub!");
    }

    public String getSettingsActivity() {
        throw new RuntimeException("Stub!");
    }

    public static SyncAdapterType newKey(String authority, String accountType) {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object o) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
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
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }
}
