package android.content.pm;

import android.os.Parcel;
import android.util.Printer;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/ComponentInfo.class */
public class ComponentInfo extends PackageItemInfo {
    public ApplicationInfo applicationInfo;
    public String processName;
    public int descriptionRes;
    public boolean enabled;
    public boolean exported;

    public ComponentInfo() {
        throw new RuntimeException("Stub!");
    }

    public ComponentInfo(ComponentInfo orig) {
        throw new RuntimeException("Stub!");
    }

    protected ComponentInfo(Parcel source) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.pm.PackageItemInfo
    public CharSequence loadLabel(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }

    public boolean isEnabled() {
        throw new RuntimeException("Stub!");
    }

    public final int getIconResource() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.pm.PackageItemInfo
    protected void dumpFront(Printer pw, String prefix) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.pm.PackageItemInfo
    protected void dumpBack(Printer pw, String prefix) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.pm.PackageItemInfo, android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        throw new RuntimeException("Stub!");
    }
}
