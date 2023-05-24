package android.appwidget;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/appwidget/AppWidgetProviderInfo.class */
public class AppWidgetProviderInfo implements Parcelable {
    public static final int RESIZE_NONE = 0;
    public static final int RESIZE_HORIZONTAL = 1;
    public static final int RESIZE_VERTICAL = 2;
    public static final int RESIZE_BOTH = 3;
    public ComponentName provider;
    public int minWidth;
    public int minHeight;
    public int minResizeWidth;
    public int minResizeHeight;
    public int updatePeriodMillis;
    public int initialLayout;
    public ComponentName configure;
    public String label;
    public int icon;
    public int autoAdvanceViewId;
    public int previewImage;
    public int resizeMode;
    public static final Parcelable.Creator<AppWidgetProviderInfo> CREATOR = null;

    public AppWidgetProviderInfo() {
        throw new RuntimeException("Stub!");
    }

    public AppWidgetProviderInfo(Parcel in) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
