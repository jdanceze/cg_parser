package android.gesture;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/gesture/Gesture.class */
public class Gesture implements Parcelable {
    public static final Parcelable.Creator<Gesture> CREATOR = null;

    public Gesture() {
        throw new RuntimeException("Stub!");
    }

    public Object clone() {
        throw new RuntimeException("Stub!");
    }

    public ArrayList<GestureStroke> getStrokes() {
        throw new RuntimeException("Stub!");
    }

    public int getStrokesCount() {
        throw new RuntimeException("Stub!");
    }

    public void addStroke(GestureStroke stroke) {
        throw new RuntimeException("Stub!");
    }

    public float getLength() {
        throw new RuntimeException("Stub!");
    }

    public RectF getBoundingBox() {
        throw new RuntimeException("Stub!");
    }

    public Path toPath() {
        throw new RuntimeException("Stub!");
    }

    public Path toPath(Path path) {
        throw new RuntimeException("Stub!");
    }

    public Path toPath(int width, int height, int edge, int numSample) {
        throw new RuntimeException("Stub!");
    }

    public Path toPath(Path path, int width, int height, int edge, int numSample) {
        throw new RuntimeException("Stub!");
    }

    public long getID() {
        throw new RuntimeException("Stub!");
    }

    public Bitmap toBitmap(int width, int height, int edge, int numSample, int color) {
        throw new RuntimeException("Stub!");
    }

    public Bitmap toBitmap(int width, int height, int inset, int color) {
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
}
