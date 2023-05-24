package android.os;

import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/PatternMatcher.class */
public class PatternMatcher implements Parcelable {
    public static final int PATTERN_LITERAL = 0;
    public static final int PATTERN_PREFIX = 1;
    public static final int PATTERN_SIMPLE_GLOB = 2;
    public static final Parcelable.Creator<PatternMatcher> CREATOR = null;

    public PatternMatcher(String pattern, int type) {
        throw new RuntimeException("Stub!");
    }

    public PatternMatcher(Parcel src) {
        throw new RuntimeException("Stub!");
    }

    public final String getPath() {
        throw new RuntimeException("Stub!");
    }

    public final int getType() {
        throw new RuntimeException("Stub!");
    }

    public boolean match(String str) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
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
}
