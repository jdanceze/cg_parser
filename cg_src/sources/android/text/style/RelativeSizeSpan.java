package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/RelativeSizeSpan.class */
public class RelativeSizeSpan extends MetricAffectingSpan implements ParcelableSpan {
    public RelativeSizeSpan(float proportion) {
        throw new RuntimeException("Stub!");
    }

    public RelativeSizeSpan(Parcel src) {
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

    public float getSizeChange() {
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
