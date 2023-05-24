package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Printer;
import java.util.Comparator;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/ApplicationInfo.class */
public class ApplicationInfo extends PackageItemInfo implements Parcelable {
    public String taskAffinity;
    public String permission;
    public String processName;
    public String className;
    public int descriptionRes;
    public int theme;
    public String manageSpaceActivityName;
    public String backupAgentName;
    public int uiOptions;
    public static final int FLAG_SYSTEM = 1;
    public static final int FLAG_DEBUGGABLE = 2;
    public static final int FLAG_HAS_CODE = 4;
    public static final int FLAG_PERSISTENT = 8;
    public static final int FLAG_FACTORY_TEST = 16;
    public static final int FLAG_ALLOW_TASK_REPARENTING = 32;
    public static final int FLAG_ALLOW_CLEAR_USER_DATA = 64;
    public static final int FLAG_UPDATED_SYSTEM_APP = 128;
    public static final int FLAG_TEST_ONLY = 256;
    public static final int FLAG_SUPPORTS_SMALL_SCREENS = 512;
    public static final int FLAG_SUPPORTS_NORMAL_SCREENS = 1024;
    public static final int FLAG_SUPPORTS_LARGE_SCREENS = 2048;
    public static final int FLAG_RESIZEABLE_FOR_SCREENS = 4096;
    public static final int FLAG_SUPPORTS_SCREEN_DENSITIES = 8192;
    public static final int FLAG_VM_SAFE_MODE = 16384;
    public static final int FLAG_ALLOW_BACKUP = 32768;
    public static final int FLAG_KILL_AFTER_RESTORE = 65536;
    public static final int FLAG_RESTORE_ANY_VERSION = 131072;
    public static final int FLAG_EXTERNAL_STORAGE = 262144;
    public static final int FLAG_SUPPORTS_XLARGE_SCREENS = 524288;
    public static final int FLAG_LARGE_HEAP = 1048576;
    public static final int FLAG_STOPPED = 2097152;
    public int flags;
    public int requiresSmallestWidthDp;
    public int compatibleWidthLimitDp;
    public int largestWidthLimitDp;
    public String sourceDir;
    public String publicSourceDir;
    public String[] sharedLibraryFiles = null;
    public String dataDir;
    public String nativeLibraryDir;
    public int uid;
    public int targetSdkVersion;
    public boolean enabled;
    public static final Parcelable.Creator<ApplicationInfo> CREATOR = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/ApplicationInfo$DisplayNameComparator.class */
    public static class DisplayNameComparator implements Comparator<ApplicationInfo> {
        public DisplayNameComparator(PackageManager pm) {
            throw new RuntimeException("Stub!");
        }

        @Override // java.util.Comparator
        public final int compare(ApplicationInfo aa, ApplicationInfo ab) {
            throw new RuntimeException("Stub!");
        }
    }

    public ApplicationInfo() {
        throw new RuntimeException("Stub!");
    }

    public ApplicationInfo(ApplicationInfo orig) {
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

    @Override // android.content.pm.PackageItemInfo, android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence loadDescription(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }
}
