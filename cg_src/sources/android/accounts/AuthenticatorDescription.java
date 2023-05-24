package android.accounts;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/accounts/AuthenticatorDescription.class */
public class AuthenticatorDescription implements Parcelable {
    public final String type;
    public final int labelId;
    public final int iconId;
    public final int smallIconId;
    public final int accountPreferencesId;
    public final String packageName;
    public final boolean customTokens;
    public static final Parcelable.Creator<AuthenticatorDescription> CREATOR = null;

    public AuthenticatorDescription(String type, String packageName, int labelId, int iconId, int smallIconId, int prefId, boolean customTokens) {
        throw new RuntimeException("Stub!");
    }

    public AuthenticatorDescription(String type, String packageName, int labelId, int iconId, int smallIconId, int prefId) {
        throw new RuntimeException("Stub!");
    }

    public static AuthenticatorDescription newKey(String type) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object o) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }
}
