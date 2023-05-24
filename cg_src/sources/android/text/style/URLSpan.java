package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/URLSpan.class */
public class URLSpan extends ClickableSpan implements ParcelableSpan {
    public URLSpan(String url) {
        throw new RuntimeException("Stub!");
    }

    public URLSpan(Parcel src) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.ParcelableSpan
    public int getSpanTypeId() {
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

    public String getURL() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.ClickableSpan
    public void onClick(View widget) {
        throw new RuntimeException("Stub!");
    }
}
