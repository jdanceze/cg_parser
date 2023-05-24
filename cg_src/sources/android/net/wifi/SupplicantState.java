package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/SupplicantState.class */
public enum SupplicantState implements Parcelable {
    ASSOCIATED,
    ASSOCIATING,
    AUTHENTICATING,
    COMPLETED,
    DISCONNECTED,
    DORMANT,
    FOUR_WAY_HANDSHAKE,
    GROUP_HANDSHAKE,
    INACTIVE,
    INTERFACE_DISABLED,
    INVALID,
    SCANNING,
    UNINITIALIZED;

    public static boolean isValidState(SupplicantState state) {
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
