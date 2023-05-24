package android.graphics;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Region.class */
public class Region implements Parcelable {
    public static final Parcelable.Creator<Region> CREATOR = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/Region$Op.class */
    public enum Op {
        DIFFERENCE,
        INTERSECT,
        REPLACE,
        REVERSE_DIFFERENCE,
        UNION,
        XOR
    }

    public native boolean isEmpty();

    public native boolean isRect();

    public native boolean isComplex();

    public native boolean contains(int i, int i2);

    public native boolean quickContains(int i, int i2, int i3, int i4);

    public native boolean quickReject(int i, int i2, int i3, int i4);

    public native boolean quickReject(Region region);

    public native void translate(int i, int i2, Region region);

    public Region() {
        throw new RuntimeException("Stub!");
    }

    public Region(Region region) {
        throw new RuntimeException("Stub!");
    }

    public Region(Rect r) {
        throw new RuntimeException("Stub!");
    }

    public Region(int left, int top, int right, int bottom) {
        throw new RuntimeException("Stub!");
    }

    public void setEmpty() {
        throw new RuntimeException("Stub!");
    }

    public boolean set(Region region) {
        throw new RuntimeException("Stub!");
    }

    public boolean set(Rect r) {
        throw new RuntimeException("Stub!");
    }

    public boolean set(int left, int top, int right, int bottom) {
        throw new RuntimeException("Stub!");
    }

    public boolean setPath(Path path, Region clip) {
        throw new RuntimeException("Stub!");
    }

    public Rect getBounds() {
        throw new RuntimeException("Stub!");
    }

    public boolean getBounds(Rect r) {
        throw new RuntimeException("Stub!");
    }

    public Path getBoundaryPath() {
        throw new RuntimeException("Stub!");
    }

    public boolean getBoundaryPath(Path path) {
        throw new RuntimeException("Stub!");
    }

    public boolean quickContains(Rect r) {
        throw new RuntimeException("Stub!");
    }

    public boolean quickReject(Rect r) {
        throw new RuntimeException("Stub!");
    }

    public void translate(int dx, int dy) {
        throw new RuntimeException("Stub!");
    }

    public final boolean union(Rect r) {
        throw new RuntimeException("Stub!");
    }

    public boolean op(Rect r, Op op) {
        throw new RuntimeException("Stub!");
    }

    public boolean op(int left, int top, int right, int bottom, Op op) {
        throw new RuntimeException("Stub!");
    }

    public boolean op(Region region, Op op) {
        throw new RuntimeException("Stub!");
    }

    public boolean op(Rect rect, Region region, Op op) {
        throw new RuntimeException("Stub!");
    }

    public boolean op(Region region1, Region region2, Op op) {
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
    public void writeToParcel(Parcel p, int flags) {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
