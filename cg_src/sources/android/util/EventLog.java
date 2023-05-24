package android.util;

import java.io.IOException;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/util/EventLog.class */
public class EventLog {
    public static native int writeEvent(int i, int i2);

    public static native int writeEvent(int i, long j);

    public static native int writeEvent(int i, String str);

    public static native int writeEvent(int i, Object... objArr);

    public static native void readEvents(int[] iArr, Collection<Event> collection) throws IOException;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/util/EventLog$Event.class */
    public static final class Event {
        Event() {
            throw new RuntimeException("Stub!");
        }

        public int getProcessId() {
            throw new RuntimeException("Stub!");
        }

        public int getThreadId() {
            throw new RuntimeException("Stub!");
        }

        public long getTimeNanos() {
            throw new RuntimeException("Stub!");
        }

        public int getTag() {
            throw new RuntimeException("Stub!");
        }

        public synchronized Object getData() {
            throw new RuntimeException("Stub!");
        }
    }

    EventLog() {
        throw new RuntimeException("Stub!");
    }

    public static String getTagName(int tag) {
        throw new RuntimeException("Stub!");
    }

    public static int getTagCode(String name) {
        throw new RuntimeException("Stub!");
    }
}
