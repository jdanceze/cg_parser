package android.content;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ClipDescription.class */
public class ClipDescription implements Parcelable {
    public static final String MIMETYPE_TEXT_PLAIN = "text/plain";
    public static final String MIMETYPE_TEXT_HTML = "text/html";
    public static final String MIMETYPE_TEXT_URILIST = "text/uri-list";
    public static final String MIMETYPE_TEXT_INTENT = "text/vnd.android.intent";
    public static final Parcelable.Creator<ClipDescription> CREATOR = null;

    public ClipDescription(CharSequence label, String[] mimeTypes) {
        throw new RuntimeException("Stub!");
    }

    public ClipDescription(ClipDescription o) {
        throw new RuntimeException("Stub!");
    }

    public static boolean compareMimeTypes(String concreteType, String desiredType) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getLabel() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasMimeType(String mimeType) {
        throw new RuntimeException("Stub!");
    }

    public String[] filterMimeTypes(String mimeType) {
        throw new RuntimeException("Stub!");
    }

    public int getMimeTypeCount() {
        throw new RuntimeException("Stub!");
    }

    public String getMimeType(int index) {
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
