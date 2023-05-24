package android.net.wifi.p2p;

import android.content.Context;
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pServiceRequest;
import android.os.Looper;
import java.util.List;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/p2p/WifiP2pManager.class */
public class WifiP2pManager {
    public static final String WIFI_P2P_STATE_CHANGED_ACTION = "android.net.wifi.p2p.STATE_CHANGED";
    public static final String EXTRA_WIFI_STATE = "wifi_p2p_state";
    public static final int WIFI_P2P_STATE_DISABLED = 1;
    public static final int WIFI_P2P_STATE_ENABLED = 2;
    public static final String WIFI_P2P_CONNECTION_CHANGED_ACTION = "android.net.wifi.p2p.CONNECTION_STATE_CHANGE";
    public static final String EXTRA_WIFI_P2P_INFO = "wifiP2pInfo";
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String WIFI_P2P_PEERS_CHANGED_ACTION = "android.net.wifi.p2p.PEERS_CHANGED";
    public static final String WIFI_P2P_DISCOVERY_CHANGED_ACTION = "android.net.wifi.p2p.DISCOVERY_STATE_CHANGE";
    public static final String EXTRA_DISCOVERY_STATE = "discoveryState";
    public static final int WIFI_P2P_DISCOVERY_STOPPED = 1;
    public static final int WIFI_P2P_DISCOVERY_STARTED = 2;
    public static final String WIFI_P2P_THIS_DEVICE_CHANGED_ACTION = "android.net.wifi.p2p.THIS_DEVICE_CHANGED";
    public static final String EXTRA_WIFI_P2P_DEVICE = "wifiP2pDevice";
    public static final int ERROR = 0;
    public static final int P2P_UNSUPPORTED = 1;
    public static final int BUSY = 2;
    public static final int NO_SERVICE_REQUESTS = 3;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/p2p/WifiP2pManager$ActionListener.class */
    public interface ActionListener {
        void onSuccess();

        void onFailure(int i);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/p2p/WifiP2pManager$ChannelListener.class */
    public interface ChannelListener {
        void onChannelDisconnected();
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/p2p/WifiP2pManager$ConnectionInfoListener.class */
    public interface ConnectionInfoListener {
        void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/p2p/WifiP2pManager$DnsSdServiceResponseListener.class */
    public interface DnsSdServiceResponseListener {
        void onDnsSdServiceAvailable(String str, String str2, WifiP2pDevice wifiP2pDevice);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/p2p/WifiP2pManager$DnsSdTxtRecordListener.class */
    public interface DnsSdTxtRecordListener {
        void onDnsSdTxtRecordAvailable(String str, Map<String, String> map, WifiP2pDevice wifiP2pDevice);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/p2p/WifiP2pManager$GroupInfoListener.class */
    public interface GroupInfoListener {
        void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/p2p/WifiP2pManager$PeerListListener.class */
    public interface PeerListListener {
        void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/p2p/WifiP2pManager$ServiceResponseListener.class */
    public interface ServiceResponseListener {
        void onServiceAvailable(int i, byte[] bArr, WifiP2pDevice wifiP2pDevice);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/p2p/WifiP2pManager$UpnpServiceResponseListener.class */
    public interface UpnpServiceResponseListener {
        void onUpnpServiceAvailable(List<String> list, WifiP2pDevice wifiP2pDevice);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/wifi/p2p/WifiP2pManager$Channel.class */
    public static class Channel {
        Channel() {
            throw new RuntimeException("Stub!");
        }
    }

    WifiP2pManager() {
        throw new RuntimeException("Stub!");
    }

    public Channel initialize(Context srcContext, Looper srcLooper, ChannelListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void discoverPeers(Channel c, ActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void stopPeerDiscovery(Channel c, ActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void connect(Channel c, WifiP2pConfig config, ActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void cancelConnect(Channel c, ActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void createGroup(Channel c, ActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void removeGroup(Channel c, ActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void addLocalService(Channel c, WifiP2pServiceInfo servInfo, ActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void removeLocalService(Channel c, WifiP2pServiceInfo servInfo, ActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void clearLocalServices(Channel c, ActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setServiceResponseListener(Channel c, ServiceResponseListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setDnsSdResponseListeners(Channel c, DnsSdServiceResponseListener servListener, DnsSdTxtRecordListener txtListener) {
        throw new RuntimeException("Stub!");
    }

    public void setUpnpServiceResponseListener(Channel c, UpnpServiceResponseListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void discoverServices(Channel c, ActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void addServiceRequest(Channel c, WifiP2pServiceRequest req, ActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void removeServiceRequest(Channel c, WifiP2pServiceRequest req, ActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void clearServiceRequests(Channel c, ActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void requestPeers(Channel c, PeerListListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void requestConnectionInfo(Channel c, ConnectionInfoListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void requestGroupInfo(Channel c, GroupInfoListener listener) {
        throw new RuntimeException("Stub!");
    }
}
