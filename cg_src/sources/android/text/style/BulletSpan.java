package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/BulletSpan.class */
public class BulletSpan implements LeadingMarginSpan, ParcelableSpan {
    public static final int STANDARD_GAP_WIDTH = 2;

    public BulletSpan() {
        throw new RuntimeException("Stub!");
    }

    public BulletSpan(int gapWidth) {
        throw new RuntimeException("Stub!");
    }

    public BulletSpan(int gapWidth, int color) {
        throw new RuntimeException("Stub!");
    }

    public BulletSpan(Parcel src) {
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

    @Override // android.text.style.LeadingMarginSpan
    public int getLeadingMargin(boolean first) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.LeadingMarginSpan
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout l) {
        throw new RuntimeException("Stub!");
    }
}
