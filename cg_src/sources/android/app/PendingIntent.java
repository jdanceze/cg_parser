package android.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AndroidException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/PendingIntent.class */
public final class PendingIntent implements Parcelable {
    public static final int FLAG_ONE_SHOT = 1073741824;
    public static final int FLAG_NO_CREATE = 536870912;
    public static final int FLAG_CANCEL_CURRENT = 268435456;
    public static final int FLAG_UPDATE_CURRENT = 134217728;
    public static final Parcelable.Creator<PendingIntent> CREATOR = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/PendingIntent$OnFinished.class */
    public interface OnFinished {
        void onSendFinished(PendingIntent pendingIntent, Intent intent, int i, String str, Bundle bundle);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/PendingIntent$CanceledException.class */
    public static class CanceledException extends AndroidException {
        public CanceledException() {
            throw new RuntimeException("Stub!");
        }

        public CanceledException(String name) {
            throw new RuntimeException("Stub!");
        }

        public CanceledException(Exception cause) {
            throw new RuntimeException("Stub!");
        }
    }

    PendingIntent() {
        throw new RuntimeException("Stub!");
    }

    public static PendingIntent getActivity(Context context, int requestCode, Intent intent, int flags) {
        throw new RuntimeException("Stub!");
    }

    public static PendingIntent getActivity(Context context, int requestCode, Intent intent, int flags, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    public static PendingIntent getActivities(Context context, int requestCode, Intent[] intents, int flags) {
        throw new RuntimeException("Stub!");
    }

    public static PendingIntent getActivities(Context context, int requestCode, Intent[] intents, int flags, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    public static PendingIntent getBroadcast(Context context, int requestCode, Intent intent, int flags) {
        throw new RuntimeException("Stub!");
    }

    public static PendingIntent getService(Context context, int requestCode, Intent intent, int flags) {
        throw new RuntimeException("Stub!");
    }

    public IntentSender getIntentSender() {
        throw new RuntimeException("Stub!");
    }

    public void cancel() {
        throw new RuntimeException("Stub!");
    }

    public void send() throws CanceledException {
        throw new RuntimeException("Stub!");
    }

    public void send(int code) throws CanceledException {
        throw new RuntimeException("Stub!");
    }

    public void send(Context context, int code, Intent intent) throws CanceledException {
        throw new RuntimeException("Stub!");
    }

    public void send(int code, OnFinished onFinished, Handler handler) throws CanceledException {
        throw new RuntimeException("Stub!");
    }

    public void send(Context context, int code, Intent intent, OnFinished onFinished, Handler handler) throws CanceledException {
        throw new RuntimeException("Stub!");
    }

    public void send(Context context, int code, Intent intent, OnFinished onFinished, Handler handler, String requiredPermission) throws CanceledException {
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

    public static void writePendingIntentOrNullToParcel(PendingIntent sender, Parcel out) {
        throw new RuntimeException("Stub!");
    }

    public static PendingIntent readPendingIntentOrNullFromParcel(Parcel in) {
        throw new RuntimeException("Stub!");
    }
}
