package android.view.inputmethod;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/inputmethod/InputBinding.class */
public final class InputBinding implements Parcelable {
    public static final Parcelable.Creator<InputBinding> CREATOR = null;

    public InputBinding(InputConnection conn, IBinder connToken, int uid, int pid) {
        throw new RuntimeException("Stub!");
    }

    public InputBinding(InputConnection conn, InputBinding binding) {
        throw new RuntimeException("Stub!");
    }

    public InputConnection getConnection() {
        throw new RuntimeException("Stub!");
    }

    public IBinder getConnectionToken() {
        throw new RuntimeException("Stub!");
    }

    public int getUid() {
        throw new RuntimeException("Stub!");
    }

    public int getPid() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
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
