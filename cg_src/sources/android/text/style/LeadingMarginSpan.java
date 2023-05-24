package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/LeadingMarginSpan.class */
public interface LeadingMarginSpan extends ParagraphStyle {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/LeadingMarginSpan$LeadingMarginSpan2.class */
    public interface LeadingMarginSpan2 extends LeadingMarginSpan, WrapTogetherSpan {
        int getLeadingMarginLineCount();
    }

    int getLeadingMargin(boolean z);

    void drawLeadingMargin(Canvas canvas, Paint paint, int i, int i2, int i3, int i4, int i5, CharSequence charSequence, int i6, int i7, boolean z, Layout layout);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/LeadingMarginSpan$Standard.class */
    public static class Standard implements LeadingMarginSpan, ParcelableSpan {
        public Standard(int first, int rest) {
            throw new RuntimeException("Stub!");
        }

        public Standard(int every) {
            throw new RuntimeException("Stub!");
        }

        public Standard(Parcel src) {
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
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
            throw new RuntimeException("Stub!");
        }
    }
}
