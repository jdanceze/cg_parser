package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/PermissionInfo.class */
public class PermissionInfo extends PackageItemInfo implements Parcelable {
    public static final int PROTECTION_NORMAL = 0;
    public static final int PROTECTION_DANGEROUS = 1;
    public static final int PROTECTION_SIGNATURE = 2;
    public static final int PROTECTION_SIGNATURE_OR_SYSTEM = 3;
    public static final int PROTECTION_FLAG_SYSTEM = 16;
    public static final int PROTECTION_FLAG_DEVELOPMENT = 32;
    public static final int PROTECTION_MASK_BASE = 15;
    public static final int PROTECTION_MASK_FLAGS = 240;
    public String group;
    public int descriptionRes;
    public CharSequence nonLocalizedDescription;
    public int protectionLevel;
    public static final Parcelable.Creator<PermissionInfo> CREATOR = null;

    public PermissionInfo() {
        throw new RuntimeException("Stub!");
    }

    public PermissionInfo(PermissionInfo orig) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence loadDescription(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.pm.PackageItemInfo, android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        throw new RuntimeException("Stub!");
    }
}
