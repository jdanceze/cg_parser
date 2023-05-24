package android.net.wifi.p2p;

import android.net.wifi.WpsInfo;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/p2p/WifiP2pConfig.class */
public class WifiP2pConfig implements Parcelable {
    public String deviceAddress;
    public WpsInfo wps;
    public int groupOwnerIntent;
    public static final Parcelable.Creator<WifiP2pConfig> CREATOR = null;

    public WifiP2pConfig() {
        throw new RuntimeException("Stub!");
    }

    public WifiP2pConfig(WifiP2pConfig source) {
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
