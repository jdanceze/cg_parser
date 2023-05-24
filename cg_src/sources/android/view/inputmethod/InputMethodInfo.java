package android.view.inputmethod;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Printer;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/inputmethod/InputMethodInfo.class */
public final class InputMethodInfo implements Parcelable {
    public static final Parcelable.Creator<InputMethodInfo> CREATOR = null;

    public InputMethodInfo(Context context, ResolveInfo service) throws XmlPullParserException, IOException {
        throw new RuntimeException("Stub!");
    }

    public InputMethodInfo(String packageName, String className, CharSequence label, String settingsActivity) {
        throw new RuntimeException("Stub!");
    }

    public String getId() {
        throw new RuntimeException("Stub!");
    }

    public String getPackageName() {
        throw new RuntimeException("Stub!");
    }

    public String getServiceName() {
        throw new RuntimeException("Stub!");
    }

    public ServiceInfo getServiceInfo() {
        throw new RuntimeException("Stub!");
    }

    public ComponentName getComponent() {
        throw new RuntimeException("Stub!");
    }

    public CharSequence loadLabel(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }

    public Drawable loadIcon(PackageManager pm) {
        throw new RuntimeException("Stub!");
    }

    public String getSettingsActivity() {
        throw new RuntimeException("Stub!");
    }

    public int getSubtypeCount() {
        throw new RuntimeException("Stub!");
    }

    public InputMethodSubtype getSubtypeAt(int index) {
        throw new RuntimeException("Stub!");
    }

    public int getIsDefaultResourceId() {
        throw new RuntimeException("Stub!");
    }

    public void dump(Printer pw, String prefix) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object o) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
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
