package android.gesture;

import java.util.ArrayList;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/gesture/GestureLibrary.class */
public abstract class GestureLibrary {
    protected final GestureStore mStore;

    public abstract boolean save();

    public abstract boolean load();

    protected GestureLibrary() {
        throw new RuntimeException("Stub!");
    }

    public boolean isReadOnly() {
        throw new RuntimeException("Stub!");
    }

    public void setOrientationStyle(int style) {
        throw new RuntimeException("Stub!");
    }

    public int getOrientationStyle() {
        throw new RuntimeException("Stub!");
    }

    public void setSequenceType(int type) {
        throw new RuntimeException("Stub!");
    }

    public int getSequenceType() {
        throw new RuntimeException("Stub!");
    }

    public Set<String> getGestureEntries() {
        throw new RuntimeException("Stub!");
    }

    public ArrayList<Prediction> recognize(Gesture gesture) {
        throw new RuntimeException("Stub!");
    }

    public void addGesture(String entryName, Gesture gesture) {
        throw new RuntimeException("Stub!");
    }

    public void removeGesture(String entryName, Gesture gesture) {
        throw new RuntimeException("Stub!");
    }

    public void removeEntry(String entryName) {
        throw new RuntimeException("Stub!");
    }

    public ArrayList<Gesture> getGestures(String entryName) {
        throw new RuntimeException("Stub!");
    }
}
