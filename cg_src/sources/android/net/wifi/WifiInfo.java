package android.net.wifi;

import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/WifiInfo.class */
public class WifiInfo implements Parcelable {
    public static final String LINK_SPEED_UNITS = "Mbps";

    WifiInfo() {
        throw new RuntimeException("Stub!");
    }

    public String getSSID() {
        throw new RuntimeException("Stub!");
    }

    public String getBSSID() {
        throw new RuntimeException("Stub!");
    }

    public int getRssi() {
        throw new RuntimeException("Stub!");
    }

    public int getLinkSpeed() {
        throw new RuntimeException("Stub!");
    }

    public String getMacAddress() {
        throw new RuntimeException("Stub!");
    }

    public int getNetworkId() {
        throw new RuntimeException("Stub!");
    }

    public SupplicantState getSupplicantState() {
        throw new RuntimeException("Stub!");
    }

    public int getIpAddress() {
        throw new RuntimeException("Stub!");
    }

    public boolean getHiddenSSID() {
        throw new RuntimeException("Stub!");
    }

    public static NetworkInfo.DetailedState getDetailedStateOf(SupplicantState suppState) {
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
