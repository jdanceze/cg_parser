package android.bluetooth;

import android.os.ParcelFileDescriptor;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/bluetooth/BluetoothHealth.class */
public final class BluetoothHealth implements BluetoothProfile {
    public static final int SOURCE_ROLE = 1;
    public static final int SINK_ROLE = 2;
    public static final int CHANNEL_TYPE_RELIABLE = 10;
    public static final int CHANNEL_TYPE_STREAMING = 11;
    public static final int STATE_CHANNEL_DISCONNECTED = 0;
    public static final int STATE_CHANNEL_CONNECTING = 1;
    public static final int STATE_CHANNEL_CONNECTED = 2;
    public static final int STATE_CHANNEL_DISCONNECTING = 3;
    public static final int APP_CONFIG_REGISTRATION_SUCCESS = 0;
    public static final int APP_CONFIG_REGISTRATION_FAILURE = 1;
    public static final int APP_CONFIG_UNREGISTRATION_SUCCESS = 2;
    public static final int APP_CONFIG_UNREGISTRATION_FAILURE = 3;

    BluetoothHealth() {
        throw new RuntimeException("Stub!");
    }

    public boolean registerSinkAppConfiguration(String name, int dataType, BluetoothHealthCallback callback) {
        throw new RuntimeException("Stub!");
    }

    public boolean unregisterAppConfiguration(BluetoothHealthAppConfiguration config) {
        throw new RuntimeException("Stub!");
    }

    public boolean connectChannelToSource(BluetoothDevice device, BluetoothHealthAppConfiguration config) {
        throw new RuntimeException("Stub!");
    }

    public boolean disconnectChannel(BluetoothDevice device, BluetoothHealthAppConfiguration config, int channelId) {
        throw new RuntimeException("Stub!");
    }

    public ParcelFileDescriptor getMainChannelFd(BluetoothDevice device, BluetoothHealthAppConfiguration config) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.bluetooth.BluetoothProfile
    public int getConnectionState(BluetoothDevice device) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        throw new RuntimeException("Stub!");
    }
}
