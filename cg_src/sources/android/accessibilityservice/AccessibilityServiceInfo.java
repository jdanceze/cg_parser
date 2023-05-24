package android.accessibilityservice;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/accessibilityservice/AccessibilityServiceInfo.class */
public class AccessibilityServiceInfo implements Parcelable {
    public static final int FEEDBACK_SPOKEN = 1;
    public static final int FEEDBACK_HAPTIC = 2;
    public static final int FEEDBACK_AUDIBLE = 4;
    public static final int FEEDBACK_VISUAL = 8;
    public static final int FEEDBACK_GENERIC = 16;
    public static final int FEEDBACK_ALL_MASK = -1;
    public static final int DEFAULT = 1;
    public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 2;
    public static final int FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 4;
    public int eventTypes;
    public String[] packageNames = null;
    public int feedbackType;
    public long notificationTimeout;
    public int flags;
    public static final Parcelable.Creator<AccessibilityServiceInfo> CREATOR = null;

    public AccessibilityServiceInfo() {
        throw new RuntimeException("Stub!");
    }

    public String getId() {
        throw new RuntimeException("Stub!");
    }

    public ResolveInfo getResolveInfo() {
        throw new RuntimeException("Stub!");
    }

    public String getSettingsActivityName() {
        throw new RuntimeException("Stub!");
    }

    public boolean getCanRetrieveWindowContent() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public String getDescription() {
        throw new RuntimeException("Stub!");
    }

    public String loadDescription(PackageManager packageManager) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flagz) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public static String feedbackTypeToString(int feedbackType) {
        throw new RuntimeException("Stub!");
    }

    public static String flagToString(int flag) {
        throw new RuntimeException("Stub!");
    }
}
