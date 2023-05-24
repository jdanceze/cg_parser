package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/InstrumentationInfo.class */
public class InstrumentationInfo extends PackageItemInfo implements Parcelable {
    public String targetPackage;
    public String sourceDir;
    public String publicSourceDir;
    public String dataDir;
    public boolean handleProfiling;
    public boolean functionalTest;
    public static final Parcelable.Creator<InstrumentationInfo> CREATOR = null;

    public InstrumentationInfo() {
        throw new RuntimeException("Stub!");
    }

    public InstrumentationInfo(InstrumentationInfo orig) {
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
