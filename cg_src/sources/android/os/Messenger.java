package android.os;

import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Messenger.class */
public final class Messenger implements Parcelable {
    public static final Parcelable.Creator<Messenger> CREATOR = null;

    public Messenger(Handler target) {
        throw new RuntimeException("Stub!");
    }

    public Messenger(IBinder target) {
        throw new RuntimeException("Stub!");
    }

    public void send(Message message) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public IBinder getBinder() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object otherObj) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        throw new RuntimeException("Stub!");
    }

    public static void writeMessengerOrNullToParcel(Messenger messenger, Parcel out) {
        throw new RuntimeException("Stub!");
    }

    public static Messenger readMessengerOrNullFromParcel(Parcel in) {
        throw new RuntimeException("Stub!");
    }
}
