package android.net.wifi.p2p;

import android.os.Parcel;
import android.os.Parcelable;
import java.net.InetAddress;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/p2p/WifiP2pInfo.class */
public class WifiP2pInfo implements Parcelable {
    public boolean groupFormed;
    public boolean isGroupOwner;
    public InetAddress groupOwnerAddress;
    public static final Parcelable.Creator<WifiP2pInfo> CREATOR = null;

    public WifiP2pInfo() {
        throw new RuntimeException("Stub!");
    }

    public WifiP2pInfo(WifiP2pInfo source) {
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
