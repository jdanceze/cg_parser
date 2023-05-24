package android.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/InputDevice.class */
public final class InputDevice implements Parcelable {
    public static final int SOURCE_CLASS_MASK = 255;
    public static final int SOURCE_CLASS_BUTTON = 1;
    public static final int SOURCE_CLASS_POINTER = 2;
    public static final int SOURCE_CLASS_TRACKBALL = 4;
    public static final int SOURCE_CLASS_POSITION = 8;
    public static final int SOURCE_CLASS_JOYSTICK = 16;
    public static final int SOURCE_UNKNOWN = 0;
    public static final int SOURCE_KEYBOARD = 257;
    public static final int SOURCE_DPAD = 513;
    public static final int SOURCE_GAMEPAD = 1025;
    public static final int SOURCE_TOUCHSCREEN = 4098;
    public static final int SOURCE_MOUSE = 8194;
    public static final int SOURCE_STYLUS = 16386;
    public static final int SOURCE_TRACKBALL = 65540;
    public static final int SOURCE_TOUCHPAD = 1048584;
    public static final int SOURCE_JOYSTICK = 16777232;
    public static final int SOURCE_ANY = -256;
    @Deprecated
    public static final int MOTION_RANGE_X = 0;
    @Deprecated
    public static final int MOTION_RANGE_Y = 1;
    @Deprecated
    public static final int MOTION_RANGE_PRESSURE = 2;
    @Deprecated
    public static final int MOTION_RANGE_SIZE = 3;
    @Deprecated
    public static final int MOTION_RANGE_TOUCH_MAJOR = 4;
    @Deprecated
    public static final int MOTION_RANGE_TOUCH_MINOR = 5;
    @Deprecated
    public static final int MOTION_RANGE_TOOL_MAJOR = 6;
    @Deprecated
    public static final int MOTION_RANGE_TOOL_MINOR = 7;
    @Deprecated
    public static final int MOTION_RANGE_ORIENTATION = 8;
    public static final int KEYBOARD_TYPE_NONE = 0;
    public static final int KEYBOARD_TYPE_NON_ALPHABETIC = 1;
    public static final int KEYBOARD_TYPE_ALPHABETIC = 2;
    public static final Parcelable.Creator<InputDevice> CREATOR = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/InputDevice$MotionRange.class */
    public static final class MotionRange {
        MotionRange() {
            throw new RuntimeException("Stub!");
        }

        public int getAxis() {
            throw new RuntimeException("Stub!");
        }

        public int getSource() {
            throw new RuntimeException("Stub!");
        }

        public float getMin() {
            throw new RuntimeException("Stub!");
        }

        public float getMax() {
            throw new RuntimeException("Stub!");
        }

        public float getRange() {
            throw new RuntimeException("Stub!");
        }

        public float getFlat() {
            throw new RuntimeException("Stub!");
        }

        public float getFuzz() {
            throw new RuntimeException("Stub!");
        }
    }

    InputDevice() {
        throw new RuntimeException("Stub!");
    }

    public static InputDevice getDevice(int id) {
        throw new RuntimeException("Stub!");
    }

    public static int[] getDeviceIds() {
        throw new RuntimeException("Stub!");
    }

    public int getId() {
        throw new RuntimeException("Stub!");
    }

    public String getDescriptor() {
        throw new RuntimeException("Stub!");
    }

    public boolean isVirtual() {
        throw new RuntimeException("Stub!");
    }

    public String getName() {
        throw new RuntimeException("Stub!");
    }

    public int getSources() {
        throw new RuntimeException("Stub!");
    }

    public int getKeyboardType() {
        throw new RuntimeException("Stub!");
    }

    public KeyCharacterMap getKeyCharacterMap() {
        throw new RuntimeException("Stub!");
    }

    public MotionRange getMotionRange(int axis) {
        throw new RuntimeException("Stub!");
    }

    public MotionRange getMotionRange(int axis, int source) {
        throw new RuntimeException("Stub!");
    }

    public List<MotionRange> getMotionRanges() {
        throw new RuntimeException("Stub!");
    }

    public Vibrator getVibrator() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
