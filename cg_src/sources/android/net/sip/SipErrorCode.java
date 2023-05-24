package android.net.sip;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/sip/SipErrorCode.class */
public class SipErrorCode {
    public static final int NO_ERROR = 0;
    public static final int SOCKET_ERROR = -1;
    public static final int SERVER_ERROR = -2;
    public static final int TRANSACTION_TERMINTED = -3;
    public static final int CLIENT_ERROR = -4;
    public static final int TIME_OUT = -5;
    public static final int INVALID_REMOTE_URI = -6;
    public static final int PEER_NOT_REACHABLE = -7;
    public static final int INVALID_CREDENTIALS = -8;
    public static final int IN_PROGRESS = -9;
    public static final int DATA_CONNECTION_LOST = -10;
    public static final int CROSS_DOMAIN_AUTHENTICATION = -11;
    public static final int SERVER_UNREACHABLE = -12;

    SipErrorCode() {
        throw new RuntimeException("Stub!");
    }

    public static String toString(int errorCode) {
        throw new RuntimeException("Stub!");
    }
}
