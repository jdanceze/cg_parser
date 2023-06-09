package android.bluetooth;

import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/bluetooth/BluetoothHeadset.class */
public final class BluetoothHeadset implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_AUDIO_STATE_CHANGED = "android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED";
    public static final String ACTION_VENDOR_SPECIFIC_HEADSET_EVENT = "android.bluetooth.headset.action.VENDOR_SPECIFIC_HEADSET_EVENT";
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_CMD";
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE";
    public static final int AT_CMD_TYPE_READ = 0;
    public static final int AT_CMD_TYPE_TEST = 1;
    public static final int AT_CMD_TYPE_SET = 2;
    public static final int AT_CMD_TYPE_BASIC = 3;
    public static final int AT_CMD_TYPE_ACTION = 4;
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_ARGS";
    public static final String VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID_CATEGORY = "android.bluetooth.headset.intent.category.companyid";
    public static final int STATE_AUDIO_DISCONNECTED = 10;
    public static final int STATE_AUDIO_CONNECTING = 11;
    public static final int STATE_AUDIO_CONNECTED = 12;

    BluetoothHeadset() {
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

    public boolean startVoiceRecognition(BluetoothDevice device) {
        throw new RuntimeException("Stub!");
    }

    public boolean stopVoiceRecognition(BluetoothDevice device) {
        throw new RuntimeException("Stub!");
    }

    public boolean isAudioConnected(BluetoothDevice device) {
        throw new RuntimeException("Stub!");
    }
}
