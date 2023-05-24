package android.os;

import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/ResultReceiver.class */
public class ResultReceiver implements Parcelable {
    public static final Parcelable.Creator<ResultReceiver> CREATOR = null;

    public ResultReceiver(Handler handler) {
        throw new RuntimeException("Stub!");
    }

    public void send(int resultCode, Bundle resultData) {
        throw new RuntimeException("Stub!");
    }

    protected void onReceiveResult(int resultCode, Bundle resultData) {
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
}
