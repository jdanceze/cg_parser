package android.accounts;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/accounts/AccountAuthenticatorResponse.class */
public class AccountAuthenticatorResponse implements Parcelable {
    public static final Parcelable.Creator<AccountAuthenticatorResponse> CREATOR = null;

    public AccountAuthenticatorResponse(Parcel parcel) {
        throw new RuntimeException("Stub!");
    }

    public void onResult(Bundle result) {
        throw new RuntimeException("Stub!");
    }

    public void onRequestContinued() {
        throw new RuntimeException("Stub!");
    }

    public void onError(int errorCode, String errorMessage) {
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
}
