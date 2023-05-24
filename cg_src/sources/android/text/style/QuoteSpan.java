package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/QuoteSpan.class */
public class QuoteSpan implements LeadingMarginSpan, ParcelableSpan {
    public QuoteSpan() {
        throw new RuntimeException("Stub!");
    }

    public QuoteSpan(int color) {
        throw new RuntimeException("Stub!");
    }

    public QuoteSpan(Parcel src) {
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

    public int getColor() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.LeadingMarginSpan
    public int getLeadingMargin(boolean first) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.LeadingMarginSpan
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        throw new RuntimeException("Stub!");
    }
}
