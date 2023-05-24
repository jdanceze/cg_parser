package android.content;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ContentProviderResult.class */
public class ContentProviderResult implements Parcelable {
    public final Uri uri;
    public final Integer count;
    public static final Parcelable.Creator<ContentProviderResult> CREATOR = null;

    public ContentProviderResult(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public ContentProviderResult(int count) {
        throw new RuntimeException("Stub!");
    }

    public ContentProviderResult(Parcel source) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
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
