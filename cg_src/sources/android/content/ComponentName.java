package android.content;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ComponentName.class */
public final class ComponentName implements Parcelable, Cloneable, Comparable<ComponentName> {
    public static final Parcelable.Creator<ComponentName> CREATOR = null;

    public ComponentName(String pkg, String cls) {
        throw new RuntimeException("Stub!");
    }

    public ComponentName(Context pkg, String cls) {
        throw new RuntimeException("Stub!");
    }

    public ComponentName(Context pkg, Class<?> cls) {
        throw new RuntimeException("Stub!");
    }

    public ComponentName(Parcel in) {
        throw new RuntimeException("Stub!");
    }

    /* renamed from: clone */
    public ComponentName m29clone() {
        throw new RuntimeException("Stub!");
    }

    public String getPackageName() {
        throw new RuntimeException("Stub!");
    }

    public String getClassName() {
        throw new RuntimeException("Stub!");
    }

    public String getShortClassName() {
        throw new RuntimeException("Stub!");
    }

    public String flattenToString() {
        throw new RuntimeException("Stub!");
    }

    public String flattenToShortString() {
        throw new RuntimeException("Stub!");
    }

    public static ComponentName unflattenFromString(String str) {
        throw new RuntimeException("Stub!");
    }

    public String toShortString() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    @Override // java.lang.Comparable
    public int compareTo(ComponentName that) {
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

    public static void writeToParcel(ComponentName c, Parcel out) {
        throw new RuntimeException("Stub!");
    }

    public static ComponentName readFromParcel(Parcel in) {
        throw new RuntimeException("Stub!");
    }
}
