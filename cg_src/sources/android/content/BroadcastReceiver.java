package android.content;

import android.os.Bundle;
import android.os.IBinder;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/BroadcastReceiver.class */
public abstract class BroadcastReceiver {
    public abstract void onReceive(Context context, Intent intent);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/BroadcastReceiver$PendingResult.class */
    public static class PendingResult {
        PendingResult() {
            throw new RuntimeException("Stub!");
        }

        public final void setResultCode(int code) {
            throw new RuntimeException("Stub!");
        }

        public final int getResultCode() {
            throw new RuntimeException("Stub!");
        }

        public final void setResultData(String data) {
            throw new RuntimeException("Stub!");
        }

        public final String getResultData() {
            throw new RuntimeException("Stub!");
        }

        public final void setResultExtras(Bundle extras) {
            throw new RuntimeException("Stub!");
        }

        public final Bundle getResultExtras(boolean makeMap) {
            throw new RuntimeException("Stub!");
        }

        public final void setResult(int code, String data, Bundle extras) {
            throw new RuntimeException("Stub!");
        }

        public final boolean getAbortBroadcast() {
            throw new RuntimeException("Stub!");
        }

        public final void abortBroadcast() {
            throw new RuntimeException("Stub!");
        }

        public final void clearAbortBroadcast() {
            throw new RuntimeException("Stub!");
        }

        public final void finish() {
            throw new RuntimeException("Stub!");
        }
    }

    public BroadcastReceiver() {
        throw new RuntimeException("Stub!");
    }

    public final PendingResult goAsync() {
        throw new RuntimeException("Stub!");
    }

    public IBinder peekService(Context myContext, Intent service) {
        throw new RuntimeException("Stub!");
    }

    public final void setResultCode(int code) {
        throw new RuntimeException("Stub!");
    }

    public final int getResultCode() {
        throw new RuntimeException("Stub!");
    }

    public final void setResultData(String data) {
        throw new RuntimeException("Stub!");
    }

    public final String getResultData() {
        throw new RuntimeException("Stub!");
    }

    public final void setResultExtras(Bundle extras) {
        throw new RuntimeException("Stub!");
    }

    public final Bundle getResultExtras(boolean makeMap) {
        throw new RuntimeException("Stub!");
    }

    public final void setResult(int code, String data, Bundle extras) {
        throw new RuntimeException("Stub!");
    }

    public final boolean getAbortBroadcast() {
        throw new RuntimeException("Stub!");
    }

    public final void abortBroadcast() {
        throw new RuntimeException("Stub!");
    }

    public final void clearAbortBroadcast() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isOrderedBroadcast() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isInitialStickyBroadcast() {
        throw new RuntimeException("Stub!");
    }

    public final void setOrderedHint(boolean isOrdered) {
        throw new RuntimeException("Stub!");
    }

    public final void setDebugUnregister(boolean debug) {
        throw new RuntimeException("Stub!");
    }

    public final boolean getDebugUnregister() {
        throw new RuntimeException("Stub!");
    }
}
