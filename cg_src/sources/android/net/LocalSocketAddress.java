package android.net;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/LocalSocketAddress.class */
public class LocalSocketAddress {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/LocalSocketAddress$Namespace.class */
    public enum Namespace {
        ABSTRACT,
        FILESYSTEM,
        RESERVED
    }

    public LocalSocketAddress(String name, Namespace namespace) {
        throw new RuntimeException("Stub!");
    }

    public LocalSocketAddress(String name) {
        throw new RuntimeException("Stub!");
    }

    public String getName() {
        throw new RuntimeException("Stub!");
    }

    public Namespace getNamespace() {
        throw new RuntimeException("Stub!");
    }
}
