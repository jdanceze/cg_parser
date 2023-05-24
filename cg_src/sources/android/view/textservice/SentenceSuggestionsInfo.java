package android.view.textservice;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/textservice/SentenceSuggestionsInfo.class */
public final class SentenceSuggestionsInfo implements Parcelable {
    public static final Parcelable.Creator<SentenceSuggestionsInfo> CREATOR = null;

    public SentenceSuggestionsInfo(SuggestionsInfo[] suggestionsInfos, int[] offsets, int[] lengths) {
        throw new RuntimeException("Stub!");
    }

    public SentenceSuggestionsInfo(Parcel source) {
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

    public int getSuggestionsCount() {
        throw new RuntimeException("Stub!");
    }

    public SuggestionsInfo getSuggestionsInfoAt(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getOffsetAt(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getLengthAt(int i) {
        throw new RuntimeException("Stub!");
    }
}
