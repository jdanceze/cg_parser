package android.os;

import android.os.Parcelable;
import java.io.FileDescriptor;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Debug.class */
public final class Debug {
    public static final int TRACE_COUNT_ALLOCS = 1;
    public static final int SHOW_FULL_DETAIL = 1;
    public static final int SHOW_CLASSLOADER = 2;
    public static final int SHOW_INITIALIZED = 4;

    public static native long getNativeHeapSize();

    public static native long getNativeHeapAllocatedSize();

    public static native long getNativeHeapFreeSize();

    public static native void getMemoryInfo(MemoryInfo memoryInfo);

    public static native long getPss();

    public static native int getBinderSentTransactions();

    public static native int getBinderReceivedTransactions();

    public static final native int getBinderLocalObjectCount();

    public static final native int getBinderProxyObjectCount();

    public static final native int getBinderDeathObjectCount();

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Debug$MemoryInfo.class */
    public static class MemoryInfo implements Parcelable {
        public int dalvikPss;
        public int dalvikPrivateDirty;
        public int dalvikSharedDirty;
        public int nativePss;
        public int nativePrivateDirty;
        public int nativeSharedDirty;
        public int otherPss;
        public int otherPrivateDirty;
        public int otherSharedDirty;
        public static final Parcelable.Creator<MemoryInfo> CREATOR = null;

        public MemoryInfo() {
            throw new RuntimeException("Stub!");
        }

        public int getTotalPss() {
            throw new RuntimeException("Stub!");
        }

        public int getTotalPrivateDirty() {
            throw new RuntimeException("Stub!");
        }

        public int getTotalSharedDirty() {
            throw new RuntimeException("Stub!");
        }

        public int getOtherPss(int which) {
            throw new RuntimeException("Stub!");
        }

        public int getOtherPrivateDirty(int which) {
            throw new RuntimeException("Stub!");
        }

        public int getOtherSharedDirty(int which) {
            throw new RuntimeException("Stub!");
        }

        public static String getOtherLabel(int which) {
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

        public void readFromParcel(Parcel source) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Debug$InstructionCount.class */
    public static class InstructionCount {
        public InstructionCount() {
            throw new RuntimeException("Stub!");
        }

        public boolean resetAndStart() {
            throw new RuntimeException("Stub!");
        }

        public boolean collect() {
            throw new RuntimeException("Stub!");
        }

        public int globalTotal() {
            throw new RuntimeException("Stub!");
        }

        public int globalMethodInvocations() {
            throw new RuntimeException("Stub!");
        }
    }

    Debug() {
        throw new RuntimeException("Stub!");
    }

    public static void waitForDebugger() {
        throw new RuntimeException("Stub!");
    }

    public static boolean waitingForDebugger() {
        throw new RuntimeException("Stub!");
    }

    public static boolean isDebuggerConnected() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void changeDebugPort(int port) {
        throw new RuntimeException("Stub!");
    }

    public static void startNativeTracing() {
        throw new RuntimeException("Stub!");
    }

    public static void stopNativeTracing() {
        throw new RuntimeException("Stub!");
    }

    public static void enableEmulatorTraceOutput() {
        throw new RuntimeException("Stub!");
    }

    public static void startMethodTracing() {
        throw new RuntimeException("Stub!");
    }

    public static void startMethodTracing(String traceName) {
        throw new RuntimeException("Stub!");
    }

    public static void startMethodTracing(String traceName, int bufferSize) {
        throw new RuntimeException("Stub!");
    }

    public static void startMethodTracing(String traceName, int bufferSize, int flags) {
        throw new RuntimeException("Stub!");
    }

    public static void stopMethodTracing() {
        throw new RuntimeException("Stub!");
    }

    public static long threadCpuTimeNanos() {
        throw new RuntimeException("Stub!");
    }

    public static void startAllocCounting() {
        throw new RuntimeException("Stub!");
    }

    public static void stopAllocCounting() {
        throw new RuntimeException("Stub!");
    }

    public static int getGlobalAllocCount() {
        throw new RuntimeException("Stub!");
    }

    public static int getGlobalAllocSize() {
        throw new RuntimeException("Stub!");
    }

    public static int getGlobalFreedCount() {
        throw new RuntimeException("Stub!");
    }

    public static int getGlobalFreedSize() {
        throw new RuntimeException("Stub!");
    }

    public static int getGlobalClassInitCount() {
        throw new RuntimeException("Stub!");
    }

    public static int getGlobalClassInitTime() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static int getGlobalExternalAllocCount() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static int getGlobalExternalAllocSize() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static int getGlobalExternalFreedCount() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static int getGlobalExternalFreedSize() {
        throw new RuntimeException("Stub!");
    }

    public static int getGlobalGcInvocationCount() {
        throw new RuntimeException("Stub!");
    }

    public static int getThreadAllocCount() {
        throw new RuntimeException("Stub!");
    }

    public static int getThreadAllocSize() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static int getThreadExternalAllocCount() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static int getThreadExternalAllocSize() {
        throw new RuntimeException("Stub!");
    }

    public static int getThreadGcInvocationCount() {
        throw new RuntimeException("Stub!");
    }

    public static void resetGlobalAllocCount() {
        throw new RuntimeException("Stub!");
    }

    public static void resetGlobalAllocSize() {
        throw new RuntimeException("Stub!");
    }

    public static void resetGlobalFreedCount() {
        throw new RuntimeException("Stub!");
    }

    public static void resetGlobalFreedSize() {
        throw new RuntimeException("Stub!");
    }

    public static void resetGlobalClassInitCount() {
        throw new RuntimeException("Stub!");
    }

    public static void resetGlobalClassInitTime() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void resetGlobalExternalAllocCount() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void resetGlobalExternalAllocSize() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void resetGlobalExternalFreedCount() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void resetGlobalExternalFreedSize() {
        throw new RuntimeException("Stub!");
    }

    public static void resetGlobalGcInvocationCount() {
        throw new RuntimeException("Stub!");
    }

    public static void resetThreadAllocCount() {
        throw new RuntimeException("Stub!");
    }

    public static void resetThreadAllocSize() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void resetThreadExternalAllocCount() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void resetThreadExternalAllocSize() {
        throw new RuntimeException("Stub!");
    }

    public static void resetThreadGcInvocationCount() {
        throw new RuntimeException("Stub!");
    }

    public static void resetAllCounts() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static int setAllocationLimit(int limit) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static int setGlobalAllocationLimit(int limit) {
        throw new RuntimeException("Stub!");
    }

    public static void printLoadedClasses(int flags) {
        throw new RuntimeException("Stub!");
    }

    public static int getLoadedClassCount() {
        throw new RuntimeException("Stub!");
    }

    public static void dumpHprofData(String fileName) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public static boolean dumpService(String name, FileDescriptor fd, String[] args) {
        throw new RuntimeException("Stub!");
    }
}
