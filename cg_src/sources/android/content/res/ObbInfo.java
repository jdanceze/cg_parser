package android.content.res;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/res/ObbInfo.class */
public class ObbInfo implements Parcelable {
    public static final int OBB_OVERLAY = 1;
    public String filename;
    public String packageName;
    public int version;
    public int flags;
    public static final Parcelable.Creator<ObbInfo> CREATOR = null;

    ObbInfo() {
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
