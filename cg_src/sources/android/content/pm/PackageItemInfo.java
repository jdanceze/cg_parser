package android.content.pm;

import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Printer;
import java.util.Comparator;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/PackageItemInfo.class */
public class PackageItemInfo {
    public String name;
    public String packageName;
    public int labelRes;
    public CharSequence nonLocalizedLabel;
    public int icon;
    public int logo;
    public Bundle metaData;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/PackageItemInfo$DisplayNameComparator.class */
    public static class DisplayNameComparator implements Comparator<PackageItemInfo> {
        public DisplayNameComparator(PackageManager pm) {
            throw new RuntimeException("Stub!");
        }

        @Override // java.util.Comparator
        public final int compare(PackageItemInfo aa, PackageItemInfo ab) {
            throw new RuntimeException("Stub!");
        }
    }

    public PackageItemInfo() {
        throw new RuntimeException("Stub!");
    }

    public PackageItemInfo(PackageItemInfo orig) {
        throw new RuntimeException("Stub!");
    }

    protected PackageItemInfo(Parcel source) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence loadLabel(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }

    public Drawable loadIcon(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }

    public Drawable loadLogo(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }

    public XmlResourceParser loadXmlMetaData(PackageManager pm, String name) {
        throw new RuntimeException("Stub!");
    }

    protected void dumpFront(Printer pw, String prefix) {
        throw new RuntimeException("Stub!");
    }

    protected void dumpBack(Printer pw, String prefix) {
        throw new RuntimeException("Stub!");
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        throw new RuntimeException("Stub!");
    }
}
