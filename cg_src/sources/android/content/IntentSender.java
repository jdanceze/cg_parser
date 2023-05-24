package android.content;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AndroidException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/IntentSender.class */
public class IntentSender implements Parcelable {
    public static final Parcelable.Creator<IntentSender> CREATOR = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/IntentSender$OnFinished.class */
    public interface OnFinished {
        void onSendFinished(IntentSender intentSender, Intent intent, int i, String str, Bundle bundle);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/IntentSender$SendIntentException.class */
    public static class SendIntentException extends AndroidException {
        public SendIntentException() {
            throw new RuntimeException("Stub!");
        }

        public SendIntentException(String name) {
            throw new RuntimeException("Stub!");
        }

        public SendIntentException(Exception cause) {
            throw new RuntimeException("Stub!");
        }
    }

    IntentSender() {
        throw new RuntimeException("Stub!");
    }

    public void sendIntent(Context context, int code, Intent intent, OnFinished onFinished, Handler handler) throws SendIntentException {
        throw new RuntimeException("Stub!");
    }

    public void sendIntent(Context context, int code, Intent intent, OnFinished onFinished, Handler handler, String requiredPermission) throws SendIntentException {
        throw new RuntimeException("Stub!");
    }

    public String getTargetPackage() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object otherObj) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
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
    public void writeToParcel(Parcel out, int flags) {
        throw new RuntimeException("Stub!");
    }

    public static void writeIntentSenderOrNullToParcel(IntentSender sender, Parcel out) {
        throw new RuntimeException("Stub!");
    }

    public static IntentSender readIntentSenderOrNullFromParcel(Parcel in) {
        throw new RuntimeException("Stub!");
    }
}
