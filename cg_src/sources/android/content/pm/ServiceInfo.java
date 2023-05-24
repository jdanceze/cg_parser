package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Printer;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/ServiceInfo.class */
public class ServiceInfo extends ComponentInfo implements Parcelable {
    public String permission;
    public static final int FLAG_STOP_WITH_TASK = 1;
    public static final int FLAG_ISOLATED_PROCESS = 2;
    public int flags;
    public static final Parcelable.Creator<ServiceInfo> CREATOR = null;

    public ServiceInfo() {
        throw new RuntimeException("Stub!");
    }

    public ServiceInfo(ServiceInfo orig) {
        throw new RuntimeException("Stub!");
    }

    public void dump(Printer pw, String prefix) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.pm.ComponentInfo, android.content.pm.PackageItemInfo, android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        throw new RuntimeException("Stub!");
    }
}
