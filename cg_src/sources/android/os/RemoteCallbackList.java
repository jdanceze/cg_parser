package android.os;

import android.os.IInterface;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/RemoteCallbackList.class */
public class RemoteCallbackList<E extends IInterface> {
    public RemoteCallbackList() {
        throw new RuntimeException("Stub!");
    }

    public boolean register(E callback) {
        throw new RuntimeException("Stub!");
    }

    public boolean register(E callback, Object cookie) {
        throw new RuntimeException("Stub!");
    }

    public boolean unregister(E callback) {
        throw new RuntimeException("Stub!");
    }

    public void kill() {
        throw new RuntimeException("Stub!");
    }

    public void onCallbackDied(E callback) {
        throw new RuntimeException("Stub!");
    }

    public void onCallbackDied(E callback, Object cookie) {
        throw new RuntimeException("Stub!");
    }

    public int beginBroadcast() {
        throw new RuntimeException("Stub!");
    }

    public E getBroadcastItem(int index) {
        throw new RuntimeException("Stub!");
    }

    public Object getBroadcastCookie(int index) {
        throw new RuntimeException("Stub!");
    }

    public void finishBroadcast() {
        throw new RuntimeException("Stub!");
    }
}
