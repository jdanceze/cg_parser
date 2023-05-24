package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/AbsoluteSizeSpan.class */
public class AbsoluteSizeSpan extends MetricAffectingSpan implements ParcelableSpan {
    public AbsoluteSizeSpan(int size) {
        throw new RuntimeException("Stub!");
    }

    public AbsoluteSizeSpan(int size, boolean dip) {
        throw new RuntimeException("Stub!");
    }

    public AbsoluteSizeSpan(Parcel src) {
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

    public int getSize() {
        throw new RuntimeException("Stub!");
    }

    public boolean getDip() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint ds) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.MetricAffectingSpan
    public void updateMeasureState(TextPaint ds) {
        throw new RuntimeException("Stub!");
    }
}
