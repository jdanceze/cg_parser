package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.BitSet;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/WifiConfiguration.class */
public class WifiConfiguration implements Parcelable {
    public int networkId;
    public int status;
    public String SSID;
    public String BSSID;
    public String preSharedKey;
    public String[] wepKeys = null;
    public int wepTxKeyIndex;
    public int priority;
    public boolean hiddenSSID;
    public BitSet allowedKeyManagement;
    public BitSet allowedProtocols;
    public BitSet allowedAuthAlgorithms;
    public BitSet allowedPairwiseCiphers;
    public BitSet allowedGroupCiphers;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/WifiConfiguration$KeyMgmt.class */
    public static class KeyMgmt {
        public static final int NONE = 0;
        public static final int WPA_PSK = 1;
        public static final int WPA_EAP = 2;
        public static final int IEEE8021X = 3;
        public static final String varName = "key_mgmt";
        public static final String[] strings = null;

        KeyMgmt() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/WifiConfiguration$Protocol.class */
    public static class Protocol {
        public static final int WPA = 0;
        public static final int RSN = 1;
        public static final String varName = "proto";
        public static final String[] strings = null;

        Protocol() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/WifiConfiguration$AuthAlgorithm.class */
    public static class AuthAlgorithm {
        public static final int OPEN = 0;
        public static final int SHARED = 1;
        public static final int LEAP = 2;
        public static final String varName = "auth_alg";
        public static final String[] strings = null;

        AuthAlgorithm() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/WifiConfiguration$PairwiseCipher.class */
    public static class PairwiseCipher {
        public static final int NONE = 0;
        public static final int TKIP = 1;
        public static final int CCMP = 2;
        public static final String varName = "pairwise";
        public static final String[] strings = null;

        PairwiseCipher() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/WifiConfiguration$GroupCipher.class */
    public static class GroupCipher {
        public static final int WEP40 = 0;
        public static final int WEP104 = 1;
        public static final int TKIP = 2;
        public static final int CCMP = 3;
        public static final String varName = "group";
        public static final String[] strings = null;

        GroupCipher() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/WifiConfiguration$Status.class */
    public static class Status {
        public static final int CURRENT = 0;
        public static final int DISABLED = 1;
        public static final int ENABLED = 2;
        public static final String[] strings = null;

        Status() {
            throw new RuntimeException("Stub!");
        }
    }

    public WifiConfiguration() {
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
