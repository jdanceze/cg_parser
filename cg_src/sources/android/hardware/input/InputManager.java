package android.hardware.input;

import android.os.Handler;
import android.view.InputDevice;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/input/InputManager.class */
public final class InputManager {
    public static final String ACTION_QUERY_KEYBOARD_LAYOUTS = "android.hardware.input.action.QUERY_KEYBOARD_LAYOUTS";
    public static final String META_DATA_KEYBOARD_LAYOUTS = "android.hardware.input.metadata.KEYBOARD_LAYOUTS";

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/input/InputManager$InputDeviceListener.class */
    public interface InputDeviceListener {
        void onInputDeviceAdded(int i);

        void onInputDeviceRemoved(int i);

        void onInputDeviceChanged(int i);
    }

    InputManager() {
        throw new RuntimeException("Stub!");
    }

    public InputDevice getInputDevice(int id) {
        throw new RuntimeException("Stub!");
    }

    public int[] getInputDeviceIds() {
        throw new RuntimeException("Stub!");
    }

    public void registerInputDeviceListener(InputDeviceListener listener, Handler handler) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterInputDeviceListener(InputDeviceListener listener) {
        throw new RuntimeException("Stub!");
    }
}
