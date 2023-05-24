package android.app;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/AlarmManager.class */
public class AlarmManager {
    public static final int RTC_WAKEUP = 0;
    public static final int RTC = 1;
    public static final int ELAPSED_REALTIME_WAKEUP = 2;
    public static final int ELAPSED_REALTIME = 3;
    public static final long INTERVAL_FIFTEEN_MINUTES = 900000;
    public static final long INTERVAL_HALF_HOUR = 1800000;
    public static final long INTERVAL_HOUR = 3600000;
    public static final long INTERVAL_HALF_DAY = 43200000;
    public static final long INTERVAL_DAY = 86400000;

    AlarmManager() {
        throw new RuntimeException("Stub!");
    }

    public void set(int type, long triggerAtMillis, PendingIntent operation) {
        throw new RuntimeException("Stub!");
    }

    public void setRepeating(int type, long triggerAtMillis, long intervalMillis, PendingIntent operation) {
        throw new RuntimeException("Stub!");
    }

    public void setInexactRepeating(int type, long triggerAtMillis, long intervalMillis, PendingIntent operation) {
        throw new RuntimeException("Stub!");
    }

    public void cancel(PendingIntent operation) {
        throw new RuntimeException("Stub!");
    }

    public void setTime(long millis) {
        throw new RuntimeException("Stub!");
    }

    public void setTimeZone(String timeZone) {
        throw new RuntimeException("Stub!");
    }
}
