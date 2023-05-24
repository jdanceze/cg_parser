package android.bluetooth;

import android.os.ParcelFileDescriptor;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/bluetooth/BluetoothHealthCallback.class */
public abstract class BluetoothHealthCallback {
    public BluetoothHealthCallback() {
        throw new RuntimeException("Stub!");
    }

    public void onHealthAppConfigurationStatusChange(BluetoothHealthAppConfiguration config, int status) {
        throw new RuntimeException("Stub!");
    }

    public void onHealthChannelStateChange(BluetoothHealthAppConfiguration config, BluetoothDevice device, int prevState, int newState, ParcelFileDescriptor fd, int channelId) {
        throw new RuntimeException("Stub!");
    }
}
