package android.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/DragEvent.class */
public class DragEvent implements Parcelable {
    public static final int ACTION_DRAG_STARTED = 1;
    public static final int ACTION_DRAG_LOCATION = 2;
    public static final int ACTION_DROP = 3;
    public static final int ACTION_DRAG_ENDED = 4;
    public static final int ACTION_DRAG_ENTERED = 5;
    public static final int ACTION_DRAG_EXITED = 6;
    public static final Parcelable.Creator<DragEvent> CREATOR = null;

    DragEvent() {
        throw new RuntimeException("Stub!");
    }

    public int getAction() {
        throw new RuntimeException("Stub!");
    }

    public float getX() {
        throw new RuntimeException("Stub!");
    }

    public float getY() {
        throw new RuntimeException("Stub!");
    }

    public ClipData getClipData() {
        throw new RuntimeException("Stub!");
    }

    public ClipDescription getClipDescription() {
        throw new RuntimeException("Stub!");
    }

    public Object getLocalState() {
        throw new RuntimeException("Stub!");
    }

    public boolean getResult() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }
}
