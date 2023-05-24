package android.text.style;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import java.util.Locale;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/style/SuggestionSpan.class */
public class SuggestionSpan extends CharacterStyle implements ParcelableSpan {
    public static final int FLAG_EASY_CORRECT = 1;
    public static final int FLAG_MISSPELLED = 2;
    public static final int FLAG_AUTO_CORRECTION = 4;
    public static final String ACTION_SUGGESTION_PICKED = "android.text.style.SUGGESTION_PICKED";
    public static final String SUGGESTION_SPAN_PICKED_AFTER = "after";
    public static final String SUGGESTION_SPAN_PICKED_BEFORE = "before";
    public static final String SUGGESTION_SPAN_PICKED_HASHCODE = "hashcode";
    public static final int SUGGESTIONS_MAX_SIZE = 5;
    public static final Parcelable.Creator<SuggestionSpan> CREATOR = null;

    public SuggestionSpan(Context context, String[] suggestions, int flags) {
        throw new RuntimeException("Stub!");
    }

    public SuggestionSpan(Locale locale, String[] suggestions, int flags) {
        throw new RuntimeException("Stub!");
    }

    public SuggestionSpan(Context context, Locale locale, String[] suggestions, int flags, Class<?> notificationTargetClass) {
        throw new RuntimeException("Stub!");
    }

    public SuggestionSpan(Parcel src) {
        throw new RuntimeException("Stub!");
    }

    public String[] getSuggestions() {
        throw new RuntimeException("Stub!");
    }

    public String getLocale() {
        throw new RuntimeException("Stub!");
    }

    public int getFlags() {
        throw new RuntimeException("Stub!");
    }

    public void setFlags(int flags) {
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

    @Override // android.text.ParcelableSpan
    public int getSpanTypeId() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object o) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint tp) {
        throw new RuntimeException("Stub!");
    }
}
