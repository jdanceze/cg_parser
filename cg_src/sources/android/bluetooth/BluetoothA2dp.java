package android.bluetooth;

import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/bluetooth/BluetoothA2dp.class */
public final class BluetoothA2dp implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_PLAYING_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.PLAYING_STATE_CHANGED";
    public static final int STATE_PLAYING = 10;
    public static final int STATE_NOT_PLAYING = 11;

    BluetoothA2dp() {
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

    @Override // android.bluetooth.BluetoothProfile
    public int getConnectionState(BluetoothDevice device) {
        throw new RuntimeException("Stub!");
    }

    public boolean isA2dpPlaying(BluetoothDevice device) {
        throw new RuntimeException("Stub!");
    }
}
