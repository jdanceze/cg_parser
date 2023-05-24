package android.view;

import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/InputEvent.class */
public abstract class InputEvent implements Parcelable {
    public static final Parcelable.Creator<InputEvent> CREATOR = null;

    public abstract int getDeviceId();

    public abstract int getSource();

    public abstract long getEventTime();

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputEvent() {
        throw new RuntimeException("Stub!");
    }

    public final InputDevice getDevice() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }
}
