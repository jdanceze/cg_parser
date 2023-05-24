package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.PatternMatcher;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/PathPermission.class */
public class PathPermission extends PatternMatcher {
    public static final Parcelable.Creator<PathPermission> CREATOR = null;

    public PathPermission(String pattern, int type, String readPermission, String writePermission) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public PathPermission(Parcel src) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public String getReadPermission() {
        throw new RuntimeException("Stub!");
    }

    public String getWritePermission() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.PatternMatcher, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }
}
