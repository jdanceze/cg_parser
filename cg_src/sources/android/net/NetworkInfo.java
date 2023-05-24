package android.net;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/NetworkInfo.class */
public class NetworkInfo implements Parcelable {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/NetworkInfo$DetailedState.class */
    public enum DetailedState {
        AUTHENTICATING,
        BLOCKED,
        CONNECTED,
        CONNECTING,
        DISCONNECTED,
        DISCONNECTING,
        FAILED,
        IDLE,
        OBTAINING_IPADDR,
        SCANNING,
        SUSPENDED,
        VERIFYING_POOR_LINK
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/NetworkInfo$State.class */
    public enum State {
        CONNECTED,
        CONNECTING,
        DISCONNECTED,
        DISCONNECTING,
        SUSPENDED,
        UNKNOWN
    }

    NetworkInfo() {
        throw new RuntimeException("Stub!");
    }

    public int getType() {
        throw new RuntimeException("Stub!");
    }

    public int getSubtype() {
        throw new RuntimeException("Stub!");
    }

    public String getTypeName() {
        throw new RuntimeException("Stub!");
    }

    public String getSubtypeName() {
        throw new RuntimeException("Stub!");
    }

    public boolean isConnectedOrConnecting() {
        throw new RuntimeException("Stub!");
    }

    public boolean isConnected() {
        throw new RuntimeException("Stub!");
    }

    public boolean isAvailable() {
        throw new RuntimeException("Stub!");
    }

    public boolean isFailover() {
        throw new RuntimeException("Stub!");
    }

    public boolean isRoaming() {
        throw new RuntimeException("Stub!");
    }

    public State getState() {
        throw new RuntimeException("Stub!");
    }

    public DetailedState getDetailedState() {
        throw new RuntimeException("Stub!");
    }

    public String getReason() {
        throw new RuntimeException("Stub!");
    }

    public String getExtraInfo() {
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
