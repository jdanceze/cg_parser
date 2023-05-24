package android.net.nsd;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/nsd/NsdManager.class */
public final class NsdManager {
    public static final String ACTION_NSD_STATE_CHANGED = "android.net.nsd.STATE_CHANGED";
    public static final String EXTRA_NSD_STATE = "nsd_state";
    public static final int NSD_STATE_DISABLED = 1;
    public static final int NSD_STATE_ENABLED = 2;
    public static final int PROTOCOL_DNS_SD = 1;
    public static final int FAILURE_INTERNAL_ERROR = 0;
    public static final int FAILURE_ALREADY_ACTIVE = 3;
    public static final int FAILURE_MAX_LIMIT = 4;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/nsd/NsdManager$DiscoveryListener.class */
    public interface DiscoveryListener {
        void onStartDiscoveryFailed(String str, int i);

        void onStopDiscoveryFailed(String str, int i);

        void onDiscoveryStarted(String str);

        void onDiscoveryStopped(String str);

        void onServiceFound(NsdServiceInfo nsdServiceInfo);

        void onServiceLost(NsdServiceInfo nsdServiceInfo);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/nsd/NsdManager$RegistrationListener.class */
    public interface RegistrationListener {
        void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int i);

        void onUnregistrationFailed(NsdServiceInfo nsdServiceInfo, int i);

        void onServiceRegistered(NsdServiceInfo nsdServiceInfo);

        void onServiceUnregistered(NsdServiceInfo nsdServiceInfo);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/nsd/NsdManager$ResolveListener.class */
    public interface ResolveListener {
        void onResolveFailed(NsdServiceInfo nsdServiceInfo, int i);

        void onServiceResolved(NsdServiceInfo nsdServiceInfo);
    }

    NsdManager() {
        throw new RuntimeException("Stub!");
    }

    public void registerService(NsdServiceInfo serviceInfo, int protocolType, RegistrationListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterService(RegistrationListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void discoverServices(String serviceType, int protocolType, DiscoveryListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void stopServiceDiscovery(DiscoveryListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void resolveService(NsdServiceInfo serviceInfo, ResolveListener listener) {
        throw new RuntimeException("Stub!");
    }
}
