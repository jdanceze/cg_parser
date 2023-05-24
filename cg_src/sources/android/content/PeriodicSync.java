package android.content;

import android.accounts.Account;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/PeriodicSync.class */
public class PeriodicSync implements Parcelable {
    public final Account account;
    public final String authority;
    public final Bundle extras;
    public final long period;
    public static final Parcelable.Creator<PeriodicSync> CREATOR = null;

    public PeriodicSync(Account account, String authority, Bundle extras, long period) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object o) {
        throw new RuntimeException("Stub!");
    }
}
