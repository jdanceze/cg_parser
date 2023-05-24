package android.location;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/location/LocationProvider.class */
public abstract class LocationProvider {
    public static final int OUT_OF_SERVICE = 0;
    public static final int TEMPORARILY_UNAVAILABLE = 1;
    public static final int AVAILABLE = 2;

    public abstract boolean requiresNetwork();

    public abstract boolean requiresSatellite();

    public abstract boolean requiresCell();

    public abstract boolean hasMonetaryCost();

    public abstract boolean supportsAltitude();

    public abstract boolean supportsSpeed();

    public abstract boolean supportsBearing();

    public abstract int getPowerRequirement();

    public abstract int getAccuracy();

    LocationProvider() {
        throw new RuntimeException("Stub!");
    }

    public String getName() {
        throw new RuntimeException("Stub!");
    }

    public boolean meetsCriteria(Criteria criteria) {
        throw new RuntimeException("Stub!");
    }
}
