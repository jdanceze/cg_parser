package android.view.textservice;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/textservice/SuggestionsInfo.class */
public final class SuggestionsInfo implements Parcelable {
    public static final int RESULT_ATTR_IN_THE_DICTIONARY = 1;
    public static final int RESULT_ATTR_LOOKS_LIKE_TYPO = 2;
    public static final int RESULT_ATTR_HAS_RECOMMENDED_SUGGESTIONS = 4;
    public static final Parcelable.Creator<SuggestionsInfo> CREATOR = null;

    public SuggestionsInfo(int suggestionsAttributes, String[] suggestions) {
        throw new RuntimeException("Stub!");
    }

    public SuggestionsInfo(int suggestionsAttributes, String[] suggestions, int cookie, int sequence) {
        throw new RuntimeException("Stub!");
    }

    public SuggestionsInfo(Parcel source) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }

    public void setCookieAndSequence(int cookie, int sequence) {
        throw new RuntimeException("Stub!");
    }

    public int getCookie() {
        throw new RuntimeException("Stub!");
    }

    public int getSequence() {
        throw new RuntimeException("Stub!");
    }

    public int getSuggestionsAttributes() {
        throw new RuntimeException("Stub!");
    }

    public int getSuggestionsCount() {
        throw new RuntimeException("Stub!");
    }

    public String getSuggestionAt(int i) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }
}
