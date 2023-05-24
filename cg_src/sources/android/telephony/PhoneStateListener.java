package android.telephony;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/telephony/PhoneStateListener.class */
public class PhoneStateListener {
    public static final int LISTEN_NONE = 0;
    public static final int LISTEN_SERVICE_STATE = 1;
    @Deprecated
    public static final int LISTEN_SIGNAL_STRENGTH = 2;
    public static final int LISTEN_MESSAGE_WAITING_INDICATOR = 4;
    public static final int LISTEN_CALL_FORWARDING_INDICATOR = 8;
    public static final int LISTEN_CELL_LOCATION = 16;
    public static final int LISTEN_CALL_STATE = 32;
    public static final int LISTEN_DATA_CONNECTION_STATE = 64;
    public static final int LISTEN_DATA_ACTIVITY = 128;
    public static final int LISTEN_SIGNAL_STRENGTHS = 256;

    public PhoneStateListener() {
        throw new RuntimeException("Stub!");
    }

    public void onServiceStateChanged(ServiceState serviceState) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void onSignalStrengthChanged(int asu) {
        throw new RuntimeException("Stub!");
    }

    public void onMessageWaitingIndicatorChanged(boolean mwi) {
        throw new RuntimeException("Stub!");
    }

    public void onCallForwardingIndicatorChanged(boolean cfi) {
        throw new RuntimeException("Stub!");
    }

    public void onCellLocationChanged(CellLocation location) {
        throw new RuntimeException("Stub!");
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        throw new RuntimeException("Stub!");
    }

    public void onDataConnectionStateChanged(int state) {
        throw new RuntimeException("Stub!");
    }

    public void onDataConnectionStateChanged(int state, int networkType) {
        throw new RuntimeException("Stub!");
    }

    public void onDataActivity(int direction) {
        throw new RuntimeException("Stub!");
    }

    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        throw new RuntimeException("Stub!");
    }
}
