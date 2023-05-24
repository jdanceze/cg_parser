package android.os;

import android.util.Printer;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Handler.class */
public class Handler {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Handler$Callback.class */
    public interface Callback {
        boolean handleMessage(Message message);
    }

    public Handler() {
        throw new RuntimeException("Stub!");
    }

    public Handler(Callback callback) {
        throw new RuntimeException("Stub!");
    }

    public Handler(Looper looper) {
        throw new RuntimeException("Stub!");
    }

    public Handler(Looper looper, Callback callback) {
        throw new RuntimeException("Stub!");
    }

    public void handleMessage(Message msg) {
        throw new RuntimeException("Stub!");
    }

    public void dispatchMessage(Message msg) {
        throw new RuntimeException("Stub!");
    }

    public String getMessageName(Message message) {
        throw new RuntimeException("Stub!");
    }

    public final Message obtainMessage() {
        throw new RuntimeException("Stub!");
    }

    public final Message obtainMessage(int what) {
        throw new RuntimeException("Stub!");
    }

    public final Message obtainMessage(int what, Object obj) {
        throw new RuntimeException("Stub!");
    }

    public final Message obtainMessage(int what, int arg1, int arg2) {
        throw new RuntimeException("Stub!");
    }

    public final Message obtainMessage(int what, int arg1, int arg2, Object obj) {
        throw new RuntimeException("Stub!");
    }

    public final boolean post(Runnable r) {
        throw new RuntimeException("Stub!");
    }

    public final boolean postAtTime(Runnable r, long uptimeMillis) {
        throw new RuntimeException("Stub!");
    }

    public final boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        throw new RuntimeException("Stub!");
    }

    public final boolean postDelayed(Runnable r, long delayMillis) {
        throw new RuntimeException("Stub!");
    }

    public final boolean postAtFrontOfQueue(Runnable r) {
        throw new RuntimeException("Stub!");
    }

    public final void removeCallbacks(Runnable r) {
        throw new RuntimeException("Stub!");
    }

    public final void removeCallbacks(Runnable r, Object token) {
        throw new RuntimeException("Stub!");
    }

    public final boolean sendMessage(Message msg) {
        throw new RuntimeException("Stub!");
    }

    public final boolean sendEmptyMessage(int what) {
        throw new RuntimeException("Stub!");
    }

    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        throw new RuntimeException("Stub!");
    }

    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        throw new RuntimeException("Stub!");
    }

    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        throw new RuntimeException("Stub!");
    }

    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        throw new RuntimeException("Stub!");
    }

    public final boolean sendMessageAtFrontOfQueue(Message msg) {
        throw new RuntimeException("Stub!");
    }

    public final void removeMessages(int what) {
        throw new RuntimeException("Stub!");
    }

    public final void removeMessages(int what, Object object) {
        throw new RuntimeException("Stub!");
    }

    public final void removeCallbacksAndMessages(Object token) {
        throw new RuntimeException("Stub!");
    }

    public final boolean hasMessages(int what) {
        throw new RuntimeException("Stub!");
    }

    public final boolean hasMessages(int what, Object object) {
        throw new RuntimeException("Stub!");
    }

    public final Looper getLooper() {
        throw new RuntimeException("Stub!");
    }

    public final void dump(Printer pw, String prefix) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
