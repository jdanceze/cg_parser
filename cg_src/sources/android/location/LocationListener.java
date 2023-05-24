package android.location;

import android.os.Bundle;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/location/LocationListener.class */
public interface LocationListener {
    void onLocationChanged(Location location);

    void onStatusChanged(String str, int i, Bundle bundle);

    void onProviderEnabled(String str);

    void onProviderDisabled(String str);
}
