package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/PermissionGroupInfo.class */
public class PermissionGroupInfo extends PackageItemInfo implements Parcelable {
    public int descriptionRes;
    public CharSequence nonLocalizedDescription;
    public static final Parcelable.Creator<PermissionGroupInfo> CREATOR = null;

    public PermissionGroupInfo() {
        throw new RuntimeException("Stub!");
    }

    public PermissionGroupInfo(PermissionGroupInfo orig) {
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
