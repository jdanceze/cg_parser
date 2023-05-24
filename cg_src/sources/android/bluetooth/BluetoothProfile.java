package android.bluetooth;

import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/bluetooth/BluetoothProfile.class */
public interface BluetoothProfile {
    public static final String EXTRA_STATE = "android.bluetooth.profile.extra.STATE";
    public static final String EXTRA_PREVIOUS_STATE = "android.bluetooth.profile.extra.PREVIOUS_STATE";
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public static final int STATE_DISCONNECTING = 3;
    public static final int HEADSET = 1;
    public static final int A2DP = 2;
    public static final int HEALTH = 3;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/bluetooth/BluetoothProfile$ServiceListener.class */
    public interface ServiceListener {
        void onServiceConnected(int i, BluetoothProfile bluetoothProfile);

        void onServiceDisconnected(int i);
    }

    List<BluetoothDevice> getConnectedDevices();

    List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] iArr);

    int getConnectionState(BluetoothDevice bluetoothDevice);
}
