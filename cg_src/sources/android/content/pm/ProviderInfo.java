package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.PatternMatcher;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/ProviderInfo.class */
public final class ProviderInfo extends ComponentInfo implements Parcelable {
    public String authority;
    public String readPermission;
    public String writePermission;
    public boolean grantUriPermissions;
    public PatternMatcher[] uriPermissionPatterns = null;
    public PathPermission[] pathPermissions = null;
    public boolean multiprocess;
    public int initOrder;
    @Deprecated
    public boolean isSyncable;
    public static final Parcelable.Creator<ProviderInfo> CREATOR = null;

    public ProviderInfo() {
        throw new RuntimeException("Stub!");
    }

    public ProviderInfo(ProviderInfo orig) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.pm.ComponentInfo, android.content.pm.PackageItemInfo, android.os.Parcelable
    public void writeToParcel(Parcel out, int parcelableFlags) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
