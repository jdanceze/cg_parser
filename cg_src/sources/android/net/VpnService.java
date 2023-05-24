package android.net;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/VpnService.class */
public class VpnService extends Service {
    public static final String SERVICE_INTERFACE = "android.net.VpnService";

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/VpnService$Builder.class */
    public class Builder {
        public Builder() {
            throw new RuntimeException("Stub!");
        }

        public Builder setSession(String session) {
            throw new RuntimeException("Stub!");
        }

        public Builder setConfigureIntent(PendingIntent intent) {
            throw new RuntimeException("Stub!");
        }

        public Builder setMtu(int mtu) {
            throw new RuntimeException("Stub!");
        }

        public Builder addAddress(InetAddress address, int prefixLength) {
            throw new RuntimeException("Stub!");
        }

        public Builder addAddress(String address, int prefixLength) {
            throw new RuntimeException("Stub!");
        }

        public Builder addRoute(InetAddress address, int prefixLength) {
            throw new RuntimeException("Stub!");
        }

        public Builder addRoute(String address, int prefixLength) {
            throw new RuntimeException("Stub!");
        }

        public Builder addDnsServer(InetAddress address) {
            throw new RuntimeException("Stub!");
        }

        public Builder addDnsServer(String address) {
            throw new RuntimeException("Stub!");
        }

        public Builder addSearchDomain(String domain) {
            throw new RuntimeException("Stub!");
        }

        public ParcelFileDescriptor establish() {
            throw new RuntimeException("Stub!");
        }
    }

    public VpnService() {
        throw new RuntimeException("Stub!");
    }

    public static Intent prepare(Context context) {
        throw new RuntimeException("Stub!");
    }

    public boolean protect(int socket) {
        throw new RuntimeException("Stub!");
    }

    public boolean protect(Socket socket) {
        throw new RuntimeException("Stub!");
    }

    public boolean protect(DatagramSocket socket) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    public void onRevoke() {
        throw new RuntimeException("Stub!");
    }
}
