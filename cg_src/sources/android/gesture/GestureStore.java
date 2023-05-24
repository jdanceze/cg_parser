package android.gesture;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/gesture/GestureStore.class */
public class GestureStore {
    public static final int SEQUENCE_INVARIANT = 1;
    public static final int SEQUENCE_SENSITIVE = 2;
    public static final int ORIENTATION_INVARIANT = 1;
    public static final int ORIENTATION_SENSITIVE = 2;

    public GestureStore() {
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

    public boolean hasChanged() {
        throw new RuntimeException("Stub!");
    }

    public void save(OutputStream stream) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void save(OutputStream stream, boolean closeStream) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void load(InputStream stream) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void load(InputStream stream, boolean closeStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
