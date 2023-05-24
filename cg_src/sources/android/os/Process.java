package android.os;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Process.class */
public class Process {
    public static final int SYSTEM_UID = 1000;
    public static final int PHONE_UID = 1001;
    public static final int FIRST_APPLICATION_UID = 10000;
    public static final int LAST_APPLICATION_UID = 19999;
    public static final int BLUETOOTH_GID = 2000;
    public static final int THREAD_PRIORITY_DEFAULT = 0;
    public static final int THREAD_PRIORITY_LOWEST = 19;
    public static final int THREAD_PRIORITY_BACKGROUND = 10;
    public static final int THREAD_PRIORITY_FOREGROUND = -2;
    public static final int THREAD_PRIORITY_DISPLAY = -4;
    public static final int THREAD_PRIORITY_URGENT_DISPLAY = -8;
    public static final int THREAD_PRIORITY_AUDIO = -16;
    public static final int THREAD_PRIORITY_URGENT_AUDIO = -19;
    public static final int THREAD_PRIORITY_MORE_FAVORABLE = -1;
    public static final int THREAD_PRIORITY_LESS_FAVORABLE = 1;
    public static final int SIGNAL_QUIT = 3;
    public static final int SIGNAL_KILL = 9;
    public static final int SIGNAL_USR1 = 10;

    public static final native long getElapsedCpuTime();

    public static final native int myPid();

    public static final native int myTid();

    public static final native int myUid();

    public static final native int getUidForName(String str);

    public static final native int getGidForName(String str);

    public static final native void setThreadPriority(int i, int i2) throws IllegalArgumentException, SecurityException;

    public static final native void setThreadPriority(int i) throws IllegalArgumentException, SecurityException;

    public static final native int getThreadPriority(int i) throws IllegalArgumentException;

    public static final native void sendSignal(int i, int i2);

    public Process() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static final boolean supportsProcesses() {
        throw new RuntimeException("Stub!");
    }

    public static final void killProcess(int pid) {
        throw new RuntimeException("Stub!");
    }
}
