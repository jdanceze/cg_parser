package android.net;

import java.net.Socket;
import java.net.SocketException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/TrafficStats.class */
public class TrafficStats {
    public static final int UNSUPPORTED = -1;

    public static native long getUidTxBytes(int i);

    public static native long getUidRxBytes(int i);

    public static native long getUidTxPackets(int i);

    public static native long getUidRxPackets(int i);

    public static native long getUidTcpTxBytes(int i);

    public static native long getUidTcpRxBytes(int i);

    public static native long getUidUdpTxBytes(int i);

    public static native long getUidUdpRxBytes(int i);

    public static native long getUidTcpTxSegments(int i);

    public static native long getUidTcpRxSegments(int i);

    public static native long getUidUdpTxPackets(int i);

    public static native long getUidUdpRxPackets(int i);

    public TrafficStats() {
        throw new RuntimeException("Stub!");
    }

    public static void setThreadStatsTag(int tag) {
        throw new RuntimeException("Stub!");
    }

    public static int getThreadStatsTag() {
        throw new RuntimeException("Stub!");
    }

    public static void clearThreadStatsTag() {
        throw new RuntimeException("Stub!");
    }

    public static void tagSocket(Socket socket) throws SocketException {
        throw new RuntimeException("Stub!");
    }

    public static void untagSocket(Socket socket) throws SocketException {
        throw new RuntimeException("Stub!");
    }

    public static void incrementOperationCount(int operationCount) {
        throw new RuntimeException("Stub!");
    }

    public static void incrementOperationCount(int tag, int operationCount) {
        throw new RuntimeException("Stub!");
    }

    public static long getMobileTxPackets() {
        throw new RuntimeException("Stub!");
    }

    public static long getMobileRxPackets() {
        throw new RuntimeException("Stub!");
    }

    public static long getMobileTxBytes() {
        throw new RuntimeException("Stub!");
    }

    public static long getMobileRxBytes() {
        throw new RuntimeException("Stub!");
    }

    public static long getTotalTxPackets() {
        throw new RuntimeException("Stub!");
    }

    public static long getTotalRxPackets() {
        throw new RuntimeException("Stub!");
    }

    public static long getTotalTxBytes() {
        throw new RuntimeException("Stub!");
    }

    public static long getTotalRxBytes() {
        throw new RuntimeException("Stub!");
    }
}
