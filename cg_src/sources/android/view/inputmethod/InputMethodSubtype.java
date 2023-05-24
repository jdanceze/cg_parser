package android.view.inputmethod;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/inputmethod/InputMethodSubtype.class */
public final class InputMethodSubtype implements Parcelable {
    public static final Parcelable.Creator<InputMethodSubtype> CREATOR = null;

    public InputMethodSubtype(int nameId, int iconId, String locale, String mode, String extraValue, boolean isAuxiliary, boolean overridesImplicitlyEnabledSubtype) {
        throw new RuntimeException("Stub!");
    }

    public int getNameResId() {
        throw new RuntimeException("Stub!");
    }

    public int getIconResId() {
        throw new RuntimeException("Stub!");
    }

    public String getLocale() {
        throw new RuntimeException("Stub!");
    }

    public String getMode() {
        throw new RuntimeException("Stub!");
    }

    public String getExtraValue() {
        throw new RuntimeException("Stub!");
    }

    public boolean isAuxiliary() {
        throw new RuntimeException("Stub!");
    }

    public boolean overridesImplicitlyEnabledSubtype() {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getDisplayName(Context context, String packageName, ApplicationInfo appInfo) {
        throw new RuntimeException("Stub!");
    }

    public boolean containsExtraValueKey(String key) {
        throw new RuntimeException("Stub!");
    }

    public String getExtraValueOf(String key) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object o) {
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
