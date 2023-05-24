package android.graphics;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/RectF.class */
public class RectF implements Parcelable {
    public float left;
    public float top;
    public float right;
    public float bottom;
    public static final Parcelable.Creator<RectF> CREATOR = null;

    public RectF() {
        throw new RuntimeException("Stub!");
    }

    public RectF(float left, float top, float right, float bottom) {
        throw new RuntimeException("Stub!");
    }

    public RectF(RectF r) {
        throw new RuntimeException("Stub!");
    }

    public RectF(Rect r) {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object o) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public String toShortString() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isEmpty() {
        throw new RuntimeException("Stub!");
    }

    public final float width() {
        throw new RuntimeException("Stub!");
    }

    public final float height() {
        throw new RuntimeException("Stub!");
    }

    public final float centerX() {
        throw new RuntimeException("Stub!");
    }

    public final float centerY() {
        throw new RuntimeException("Stub!");
    }

    public void setEmpty() {
        throw new RuntimeException("Stub!");
    }

    public void set(float left, float top, float right, float bottom) {
        throw new RuntimeException("Stub!");
    }

    public void set(RectF src) {
        throw new RuntimeException("Stub!");
    }

    public void set(Rect src) {
        throw new RuntimeException("Stub!");
    }

    public void offset(float dx, float dy) {
        throw new RuntimeException("Stub!");
    }

    public void offsetTo(float newLeft, float newTop) {
        throw new RuntimeException("Stub!");
    }

    public void inset(float dx, float dy) {
        throw new RuntimeException("Stub!");
    }

    public boolean contains(float x, float y) {
        throw new RuntimeException("Stub!");
    }

    public boolean contains(float left, float top, float right, float bottom) {
        throw new RuntimeException("Stub!");
    }

    public boolean contains(RectF r) {
        throw new RuntimeException("Stub!");
    }

    public boolean intersect(float left, float top, float right, float bottom) {
        throw new RuntimeException("Stub!");
    }

    public boolean intersect(RectF r) {
        throw new RuntimeException("Stub!");
    }

    public boolean setIntersect(RectF a, RectF b) {
        throw new RuntimeException("Stub!");
    }

    public boolean intersects(float left, float top, float right, float bottom) {
        throw new RuntimeException("Stub!");
    }

    public static boolean intersects(RectF a, RectF b) {
        throw new RuntimeException("Stub!");
    }

    public void round(Rect dst) {
        throw new RuntimeException("Stub!");
    }

    public void roundOut(Rect dst) {
        throw new RuntimeException("Stub!");
    }

    public void union(float left, float top, float right, float bottom) {
        throw new RuntimeException("Stub!");
    }

    public void union(RectF r) {
        throw new RuntimeException("Stub!");
    }

    public void union(float x, float y) {
        throw new RuntimeException("Stub!");
    }

    public void sort() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        throw new RuntimeException("Stub!");
    }

    public void readFromParcel(Parcel in) {
        throw new RuntimeException("Stub!");
    }
}
