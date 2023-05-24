package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/PackageInfo.class */
public class PackageInfo implements Parcelable {
    public String packageName;
    public int versionCode;
    public String versionName;
    public String sharedUserId;
    public int sharedUserLabel;
    public ApplicationInfo applicationInfo;
    public long firstInstallTime;
    public long lastUpdateTime;
    public static final int REQUESTED_PERMISSION_REQUIRED = 1;
    public static final int REQUESTED_PERMISSION_GRANTED = 2;
    public static final Parcelable.Creator<PackageInfo> CREATOR = null;
    public int[] gids = null;
    public ActivityInfo[] activities = null;
    public ActivityInfo[] receivers = null;
    public ServiceInfo[] services = null;
    public ProviderInfo[] providers = null;
    public InstrumentationInfo[] instrumentation = null;
    public PermissionInfo[] permissions = null;
    public String[] requestedPermissions = null;
    public int[] requestedPermissionsFlags = null;
    public Signature[] signatures = null;
    public ConfigurationInfo[] configPreferences = null;
    public FeatureInfo[] reqFeatures = null;

    public PackageInfo() {
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
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        throw new RuntimeException("Stub!");
    }
}
