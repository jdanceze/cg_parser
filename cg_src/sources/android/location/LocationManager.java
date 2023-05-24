package android.location;

import android.app.PendingIntent;
import android.location.GpsStatus;
import android.os.Bundle;
import android.os.Looper;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/location/LocationManager.class */
public class LocationManager {
    public static final String NETWORK_PROVIDER = "network";
    public static final String GPS_PROVIDER = "gps";
    public static final String PASSIVE_PROVIDER = "passive";
    public static final String KEY_PROXIMITY_ENTERING = "entering";
    public static final String KEY_STATUS_CHANGED = "status";
    public static final String KEY_PROVIDER_ENABLED = "providerEnabled";
    public static final String KEY_LOCATION_CHANGED = "location";
    public static final String PROVIDERS_CHANGED_ACTION = "android.location.PROVIDERS_CHANGED";

    LocationManager() {
        throw new RuntimeException("Stub!");
    }

    public List<String> getAllProviders() {
        throw new RuntimeException("Stub!");
    }

    public List<String> getProviders(boolean enabledOnly) {
        throw new RuntimeException("Stub!");
    }

    public LocationProvider getProvider(String name) {
        throw new RuntimeException("Stub!");
    }

    public List<String> getProviders(Criteria criteria, boolean enabledOnly) {
        throw new RuntimeException("Stub!");
    }

    public String getBestProvider(Criteria criteria, boolean enabledOnly) {
        throw new RuntimeException("Stub!");
    }

    public void requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener, Looper looper) {
        throw new RuntimeException("Stub!");
    }

    public void requestLocationUpdates(long minTime, float minDistance, Criteria criteria, LocationListener listener, Looper looper) {
        throw new RuntimeException("Stub!");
    }

    public void requestLocationUpdates(String provider, long minTime, float minDistance, PendingIntent intent) {
        throw new RuntimeException("Stub!");
    }

    public void requestLocationUpdates(long minTime, float minDistance, Criteria criteria, PendingIntent intent) {
        throw new RuntimeException("Stub!");
    }

    public void requestSingleUpdate(String provider, LocationListener listener, Looper looper) {
        throw new RuntimeException("Stub!");
    }

    public void requestSingleUpdate(Criteria criteria, LocationListener listener, Looper looper) {
        throw new RuntimeException("Stub!");
    }

    public void requestSingleUpdate(String provider, PendingIntent intent) {
        throw new RuntimeException("Stub!");
    }

    public void requestSingleUpdate(Criteria criteria, PendingIntent intent) {
        throw new RuntimeException("Stub!");
    }

    public void removeUpdates(LocationListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void removeUpdates(PendingIntent intent) {
        throw new RuntimeException("Stub!");
    }

    public void addProximityAlert(double latitude, double longitude, float radius, long expiration, PendingIntent intent) {
        throw new RuntimeException("Stub!");
    }

    public void removeProximityAlert(PendingIntent intent) {
        throw new RuntimeException("Stub!");
    }

    public boolean isProviderEnabled(String provider) {
        throw new RuntimeException("Stub!");
    }

    public Location getLastKnownLocation(String provider) {
        throw new RuntimeException("Stub!");
    }

    public void addTestProvider(String name, boolean requiresNetwork, boolean requiresSatellite, boolean requiresCell, boolean hasMonetaryCost, boolean supportsAltitude, boolean supportsSpeed, boolean supportsBearing, int powerRequirement, int accuracy) {
        throw new RuntimeException("Stub!");
    }

    public void removeTestProvider(String provider) {
        throw new RuntimeException("Stub!");
    }

    public void setTestProviderLocation(String provider, Location loc) {
        throw new RuntimeException("Stub!");
    }

    public void clearTestProviderLocation(String provider) {
        throw new RuntimeException("Stub!");
    }

    public void setTestProviderEnabled(String provider, boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public void clearTestProviderEnabled(String provider) {
        throw new RuntimeException("Stub!");
    }

    public void setTestProviderStatus(String provider, int status, Bundle extras, long updateTime) {
        throw new RuntimeException("Stub!");
    }

    public void clearTestProviderStatus(String provider) {
        throw new RuntimeException("Stub!");
    }

    public boolean addGpsStatusListener(GpsStatus.Listener listener) {
        throw new RuntimeException("Stub!");
    }

    public void removeGpsStatusListener(GpsStatus.Listener listener) {
        throw new RuntimeException("Stub!");
    }

    public boolean addNmeaListener(GpsStatus.NmeaListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void removeNmeaListener(GpsStatus.NmeaListener listener) {
        throw new RuntimeException("Stub!");
    }

    public GpsStatus getGpsStatus(GpsStatus status) {
        throw new RuntimeException("Stub!");
    }

    public boolean sendExtraCommand(String provider, String command, Bundle extras) {
        throw new RuntimeException("Stub!");
    }
}
