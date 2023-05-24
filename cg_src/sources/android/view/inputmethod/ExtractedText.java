package android.view.inputmethod;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/inputmethod/ExtractedText.class */
public class ExtractedText implements Parcelable {
    public CharSequence text;
    public int startOffset;
    public int partialStartOffset;
    public int partialEndOffset;
    public int selectionStart;
    public int selectionEnd;
    public static final int FLAG_SINGLE_LINE = 1;
    public static final int FLAG_SELECTING = 2;
    public int flags;
    public static final Parcelable.Creator<ExtractedText> CREATOR = null;

    public ExtractedText() {
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
}
