package android.os;

import android.os.IBinder;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Binder.class */
public class Binder implements IBinder {
    public static final native int getCallingPid();

    public static final native int getCallingUid();

    public static final native long clearCallingIdentity();

    public static final native void restoreCallingIdentity(long j);

    public static final native void flushPendingCommands();

    public static final native void joinThreadPool();

    public Binder() {
        throw new RuntimeException("Stub!");
    }

    public void attachInterface(IInterface owner, String descriptor) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.IBinder
    public String getInterfaceDescriptor() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.IBinder
    public boolean pingBinder() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.IBinder
    public boolean isBinderAlive() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.IBinder
    public IInterface queryLocalInterface(String descriptor) {
        throw new RuntimeException("Stub!");
    }

    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.IBinder
    public void dump(FileDescriptor fd, String[] args) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.IBinder
    public void dumpAsync(FileDescriptor fd, String[] args) {
        throw new RuntimeException("Stub!");
    }

    protected void dump(FileDescriptor fd, PrintWriter fout, String[] args) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.IBinder
    public final boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.IBinder
    public void linkToDeath(IBinder.DeathRecipient recipient, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.IBinder
    public boolean unlinkToDeath(IBinder.DeathRecipient recipient, int flags) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
