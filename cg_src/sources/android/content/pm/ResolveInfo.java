package android.content.pm;

import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Printer;
import java.util.Comparator;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/ResolveInfo.class */
public class ResolveInfo implements Parcelable {
    public ActivityInfo activityInfo;
    public ServiceInfo serviceInfo;
    public IntentFilter filter;
    public int priority;
    public int preferredOrder;
    public int match;
    public int specificIndex;
    public boolean isDefault;
    public int labelRes;
    public CharSequence nonLocalizedLabel;
    public int icon;
    public String resolvePackageName;
    public static final Parcelable.Creator<ResolveInfo> CREATOR = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/ResolveInfo$DisplayNameComparator.class */
    public static class DisplayNameComparator implements Comparator<ResolveInfo> {
        public DisplayNameComparator(PackageManager pm) {
            throw new RuntimeException("Stub!");
        }

        @Override // java.util.Comparator
        public final int compare(ResolveInfo a, ResolveInfo b) {
            throw new RuntimeException("Stub!");
        }
    }

    public ResolveInfo() {
        throw new RuntimeException("Stub!");
    }

    public CharSequence loadLabel(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }

    public Drawable loadIcon(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }

    public final int getIconResource() {
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

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        throw new RuntimeException("Stub!");
    }
}
