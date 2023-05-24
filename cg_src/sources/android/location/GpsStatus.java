package android.location;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/location/GpsStatus.class */
public final class GpsStatus {
    public static final int GPS_EVENT_STARTED = 1;
    public static final int GPS_EVENT_STOPPED = 2;
    public static final int GPS_EVENT_FIRST_FIX = 3;
    public static final int GPS_EVENT_SATELLITE_STATUS = 4;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/location/GpsStatus$Listener.class */
    public interface Listener {
        void onGpsStatusChanged(int i);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/location/GpsStatus$NmeaListener.class */
    public interface NmeaListener {
        void onNmeaReceived(long j, String str);
    }

    GpsStatus() {
        throw new RuntimeException("Stub!");
    }

    public int getTimeToFirstFix() {
        throw new RuntimeException("Stub!");
    }

    public Iterable<GpsSatellite> getSatellites() {
        throw new RuntimeException("Stub!");
    }

    public int getMaxSatellites() {
        throw new RuntimeException("Stub!");
    }
}
