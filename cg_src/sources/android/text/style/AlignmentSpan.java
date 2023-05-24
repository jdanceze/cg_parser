package android.text.style;

import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/AlignmentSpan.class */
public interface AlignmentSpan extends ParagraphStyle {
    Layout.Alignment getAlignment();

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/AlignmentSpan$Standard.class */
    public static class Standard implements AlignmentSpan, ParcelableSpan {
        public Standard(Layout.Alignment align) {
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

        @Override // android.text.style.AlignmentSpan
        public Layout.Alignment getAlignment() {
            throw new RuntimeException("Stub!");
        }
    }
}
