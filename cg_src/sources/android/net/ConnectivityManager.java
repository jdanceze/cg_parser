package android.net;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/ConnectivityManager.class */
public class ConnectivityManager {
    public static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    @Deprecated
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String EXTRA_IS_FAILOVER = "isFailover";
    public static final String EXTRA_OTHER_NETWORK_INFO = "otherNetwork";
    public static final String EXTRA_NO_CONNECTIVITY = "noConnectivity";
    public static final String EXTRA_REASON = "reason";
    public static final String EXTRA_EXTRA_INFO = "extraInfo";
    @Deprecated
    public static final String ACTION_BACKGROUND_DATA_SETTING_CHANGED = "android.net.conn.BACKGROUND_DATA_SETTING_CHANGED";
    public static final int TYPE_MOBILE = 0;
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE_MMS = 2;
    public static final int TYPE_MOBILE_SUPL = 3;
    public static final int TYPE_MOBILE_DUN = 4;
    public static final int TYPE_MOBILE_HIPRI = 5;
    public static final int TYPE_WIMAX = 6;
    public static final int TYPE_BLUETOOTH = 7;
    public static final int TYPE_DUMMY = 8;
    public static final int TYPE_ETHERNET = 9;
    public static final int DEFAULT_NETWORK_PREFERENCE = 1;

    ConnectivityManager() {
        throw new RuntimeException("Stub!");
    }

    public static boolean isNetworkTypeValid(int networkType) {
        throw new RuntimeException("Stub!");
    }

    public void setNetworkPreference(int preference) {
        throw new RuntimeException("Stub!");
    }

    public int getNetworkPreference() {
        throw new RuntimeException("Stub!");
    }

    public NetworkInfo getActiveNetworkInfo() {
        throw new RuntimeException("Stub!");
    }

    public NetworkInfo getNetworkInfo(int networkType) {
        throw new RuntimeException("Stub!");
    }

    public NetworkInfo[] getAllNetworkInfo() {
        throw new RuntimeException("Stub!");
    }

    public int startUsingNetworkFeature(int networkType, String feature) {
        throw new RuntimeException("Stub!");
    }

    public int stopUsingNetworkFeature(int networkType, String feature) {
        throw new RuntimeException("Stub!");
    }

    public boolean requestRouteToHost(int networkType, int hostAddress) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean getBackgroundDataSetting() {
        throw new RuntimeException("Stub!");
    }

    public boolean isActiveNetworkMetered() {
        throw new RuntimeException("Stub!");
    }
}
