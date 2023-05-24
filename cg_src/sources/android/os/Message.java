package android.os;

import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Message.class */
public final class Message implements Parcelable {
    public int what;
    public int arg1;
    public int arg2;
    public Object obj;
    public Messenger replyTo;
    public static final Parcelable.Creator<Message> CREATOR = null;

    public Message() {
        throw new RuntimeException("Stub!");
    }

    public static Message obtain() {
        throw new RuntimeException("Stub!");
    }

    public static Message obtain(Message orig) {
        throw new RuntimeException("Stub!");
    }

    public static Message obtain(Handler h) {
        throw new RuntimeException("Stub!");
    }

    public static Message obtain(Handler h, Runnable callback) {
        throw new RuntimeException("Stub!");
    }

    public static Message obtain(Handler h, int what) {
        throw new RuntimeException("Stub!");
    }

    public static Message obtain(Handler h, int what, Object obj) {
        throw new RuntimeException("Stub!");
    }

    public static Message obtain(Handler h, int what, int arg1, int arg2) {
        throw new RuntimeException("Stub!");
    }

    public static Message obtain(Handler h, int what, int arg1, int arg2, Object obj) {
        throw new RuntimeException("Stub!");
    }

    public void recycle() {
        throw new RuntimeException("Stub!");
    }

    public void copyFrom(Message o) {
        throw new RuntimeException("Stub!");
    }

    public long getWhen() {
        throw new RuntimeException("Stub!");
    }

    public void setTarget(Handler target) {
        throw new RuntimeException("Stub!");
    }

    public Handler getTarget() {
        throw new RuntimeException("Stub!");
    }

    public Runnable getCallback() {
        throw new RuntimeException("Stub!");
    }

    public Bundle getData() {
        throw new RuntimeException("Stub!");
    }

    public Bundle peekData() {
        throw new RuntimeException("Stub!");
    }

    public void setData(Bundle data) {
        throw new RuntimeException("Stub!");
    }

    public void sendToTarget() {
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
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }
}
