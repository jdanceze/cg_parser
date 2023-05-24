package android.os;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/StatFs.class */
public class StatFs {
    public native int getBlockSize();

    public native int getBlockCount();

    public native int getFreeBlocks();

    public native int getAvailableBlocks();

    public StatFs(String path) {
        throw new RuntimeException("Stub!");
    }

    public void restat(String path) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() {
        throw new RuntimeException("Stub!");
    }
}
