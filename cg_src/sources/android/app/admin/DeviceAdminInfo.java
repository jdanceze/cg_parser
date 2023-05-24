package android.app.admin;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Printer;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/admin/DeviceAdminInfo.class */
public final class DeviceAdminInfo implements Parcelable {
    public static final int USES_POLICY_LIMIT_PASSWORD = 0;
    public static final int USES_POLICY_WATCH_LOGIN = 1;
    public static final int USES_POLICY_RESET_PASSWORD = 2;
    public static final int USES_POLICY_FORCE_LOCK = 3;
    public static final int USES_POLICY_WIPE_DATA = 4;
    public static final int USES_POLICY_EXPIRE_PASSWORD = 6;
    public static final int USES_ENCRYPTED_STORAGE = 7;
    public static final int USES_POLICY_DISABLE_CAMERA = 8;
    public static final Parcelable.Creator<DeviceAdminInfo> CREATOR = null;

    public DeviceAdminInfo(Context context, ResolveInfo receiver) throws XmlPullParserException, IOException {
        throw new RuntimeException("Stub!");
    }

    public String getPackageName() {
        throw new RuntimeException("Stub!");
    }

    public String getReceiverName() {
        throw new RuntimeException("Stub!");
    }

    public ActivityInfo getActivityInfo() {
        throw new RuntimeException("Stub!");
    }

    public ComponentName getComponent() {
        throw new RuntimeException("Stub!");
    }

    public CharSequence loadLabel(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence loadDescription(PackageManager pm) throws Resources.NotFoundException {
        throw new RuntimeException("Stub!");
    }

    public Drawable loadIcon(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }

    public boolean isVisible() {
        throw new RuntimeException("Stub!");
    }

    public boolean usesPolicy(int policyIdent) {
        throw new RuntimeException("Stub!");
    }

    public String getTagForPolicy(int policyIdent) {
        throw new RuntimeException("Stub!");
    }

    public void dump(Printer pw, String prefix) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }
}
