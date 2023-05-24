package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/WpsInfo.class */
public class WpsInfo implements Parcelable {
    public static final int PBC = 0;
    public static final int DISPLAY = 1;
    public static final int KEYPAD = 2;
    public static final int LABEL = 3;
    public static final int INVALID = 4;
    public int setup;
    public String pin;
    public static final Parcelable.Creator<WpsInfo> CREATOR = null;

    public WpsInfo() {
        throw new RuntimeException("Stub!");
    }

    public WpsInfo(WpsInfo source) {
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
