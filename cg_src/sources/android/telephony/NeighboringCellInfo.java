package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/telephony/NeighboringCellInfo.class */
public class NeighboringCellInfo implements Parcelable {
    public static final int UNKNOWN_RSSI = 99;
    public static final int UNKNOWN_CID = -1;
    public static final Parcelable.Creator<NeighboringCellInfo> CREATOR = null;

    @Deprecated
    public NeighboringCellInfo() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public NeighboringCellInfo(int rssi, int cid) {
        throw new RuntimeException("Stub!");
    }

    public NeighboringCellInfo(int rssi, String location, int radioType) {
        throw new RuntimeException("Stub!");
    }

    public NeighboringCellInfo(Parcel in) {
        throw new RuntimeException("Stub!");
    }

    public int getRssi() {
        throw new RuntimeException("Stub!");
    }

    public int getLac() {
        throw new RuntimeException("Stub!");
    }

    public int getCid() {
        throw new RuntimeException("Stub!");
    }

    public int getPsc() {
        throw new RuntimeException("Stub!");
    }

    public int getNetworkType() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setCid(int cid) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setRssi(int rssi) {
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
