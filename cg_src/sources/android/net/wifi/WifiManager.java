package android.net.wifi;

import android.net.DhcpInfo;
import android.os.WorkSource;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/WifiManager.class */
public class WifiManager {
    public static final int ERROR_AUTHENTICATING = 1;
    public static final String WIFI_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    public static final String EXTRA_WIFI_STATE = "wifi_state";
    public static final String EXTRA_PREVIOUS_WIFI_STATE = "previous_wifi_state";
    public static final int WIFI_STATE_DISABLING = 0;
    public static final int WIFI_STATE_DISABLED = 1;
    public static final int WIFI_STATE_ENABLING = 2;
    public static final int WIFI_STATE_ENABLED = 3;
    public static final int WIFI_STATE_UNKNOWN = 4;
    public static final String SUPPLICANT_CONNECTION_CHANGE_ACTION = "android.net.wifi.supplicant.CONNECTION_CHANGE";
    public static final String EXTRA_SUPPLICANT_CONNECTED = "connected";
    public static final String NETWORK_STATE_CHANGED_ACTION = "android.net.wifi.STATE_CHANGE";
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String EXTRA_BSSID = "bssid";
    public static final String EXTRA_WIFI_INFO = "wifiInfo";
    public static final String SUPPLICANT_STATE_CHANGED_ACTION = "android.net.wifi.supplicant.STATE_CHANGE";
    public static final String EXTRA_NEW_STATE = "newState";
    public static final String EXTRA_SUPPLICANT_ERROR = "supplicantError";
    public static final String SCAN_RESULTS_AVAILABLE_ACTION = "android.net.wifi.SCAN_RESULTS";
    public static final String RSSI_CHANGED_ACTION = "android.net.wifi.RSSI_CHANGED";
    public static final String EXTRA_NEW_RSSI = "newRssi";
    public static final String NETWORK_IDS_CHANGED_ACTION = "android.net.wifi.NETWORK_IDS_CHANGED";
    public static final String ACTION_PICK_WIFI_NETWORK = "android.net.wifi.PICK_WIFI_NETWORK";
    public static final int WIFI_MODE_FULL = 1;
    public static final int WIFI_MODE_SCAN_ONLY = 2;
    public static final int WIFI_MODE_FULL_HIGH_PERF = 3;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/WifiManager$WifiLock.class */
    public class WifiLock {
        WifiLock() {
            throw new RuntimeException("Stub!");
        }

        public void acquire() {
            throw new RuntimeException("Stub!");
        }

        public void release() {
            throw new RuntimeException("Stub!");
        }

        public void setReferenceCounted(boolean refCounted) {
            throw new RuntimeException("Stub!");
        }

        public boolean isHeld() {
            throw new RuntimeException("Stub!");
        }

        public void setWorkSource(WorkSource ws) {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }

        protected void finalize() throws Throwable {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/WifiManager$MulticastLock.class */
    public class MulticastLock {
        MulticastLock() {
            throw new RuntimeException("Stub!");
        }

        public void acquire() {
            throw new RuntimeException("Stub!");
        }

        public void release() {
            throw new RuntimeException("Stub!");
        }

        public void setReferenceCounted(boolean refCounted) {
            throw new RuntimeException("Stub!");
        }

        public boolean isHeld() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }

        protected void finalize() throws Throwable {
            throw new RuntimeException("Stub!");
        }
    }

    WifiManager() {
        throw new RuntimeException("Stub!");
    }

    public List<WifiConfiguration> getConfiguredNetworks() {
        throw new RuntimeException("Stub!");
    }

    public int addNetwork(WifiConfiguration config) {
        throw new RuntimeException("Stub!");
    }

    public int updateNetwork(WifiConfiguration config) {
        throw new RuntimeException("Stub!");
    }

    public boolean removeNetwork(int netId) {
        throw new RuntimeException("Stub!");
    }

    public boolean enableNetwork(int netId, boolean disableOthers) {
        throw new RuntimeException("Stub!");
    }

    public boolean disableNetwork(int netId) {
        throw new RuntimeException("Stub!");
    }

    public boolean disconnect() {
        throw new RuntimeException("Stub!");
    }

    public boolean reconnect() {
        throw new RuntimeException("Stub!");
    }

    public boolean reassociate() {
        throw new RuntimeException("Stub!");
    }

    public boolean pingSupplicant() {
        throw new RuntimeException("Stub!");
    }

    public boolean startScan() {
        throw new RuntimeException("Stub!");
    }

    public WifiInfo getConnectionInfo() {
        throw new RuntimeException("Stub!");
    }

    public List<ScanResult> getScanResults() {
        throw new RuntimeException("Stub!");
    }

    public boolean saveConfiguration() {
        throw new RuntimeException("Stub!");
    }

    public DhcpInfo getDhcpInfo() {
        throw new RuntimeException("Stub!");
    }

    public boolean setWifiEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public int getWifiState() {
        throw new RuntimeException("Stub!");
    }

    public boolean isWifiEnabled() {
        throw new RuntimeException("Stub!");
    }

    public static int calculateSignalLevel(int rssi, int numLevels) {
        throw new RuntimeException("Stub!");
    }

    public static int compareSignalLevel(int rssiA, int rssiB) {
        throw new RuntimeException("Stub!");
    }

    public WifiLock createWifiLock(int lockType, String tag) {
        throw new RuntimeException("Stub!");
    }

    public WifiLock createWifiLock(String tag) {
        throw new RuntimeException("Stub!");
    }

    public MulticastLock createMulticastLock(String tag) {
        throw new RuntimeException("Stub!");
    }
}
