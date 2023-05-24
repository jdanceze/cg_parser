package android.media;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/ExifInterface.class */
public class ExifInterface {
    public static final String TAG_ORIENTATION = "Orientation";
    public static final String TAG_DATETIME = "DateTime";
    public static final String TAG_MAKE = "Make";
    public static final String TAG_MODEL = "Model";
    public static final String TAG_FLASH = "Flash";
    public static final String TAG_IMAGE_WIDTH = "ImageWidth";
    public static final String TAG_IMAGE_LENGTH = "ImageLength";
    public static final String TAG_GPS_LATITUDE = "GPSLatitude";
    public static final String TAG_GPS_LONGITUDE = "GPSLongitude";
    public static final String TAG_GPS_LATITUDE_REF = "GPSLatitudeRef";
    public static final String TAG_GPS_LONGITUDE_REF = "GPSLongitudeRef";
    public static final String TAG_EXPOSURE_TIME = "ExposureTime";
    public static final String TAG_APERTURE = "FNumber";
    public static final String TAG_ISO = "ISOSpeedRatings";
    public static final String TAG_GPS_ALTITUDE = "GPSAltitude";
    public static final String TAG_GPS_ALTITUDE_REF = "GPSAltitudeRef";
    public static final String TAG_GPS_TIMESTAMP = "GPSTimeStamp";
    public static final String TAG_GPS_DATESTAMP = "GPSDateStamp";
    public static final String TAG_WHITE_BALANCE = "WhiteBalance";
    public static final String TAG_FOCAL_LENGTH = "FocalLength";
    public static final String TAG_GPS_PROCESSING_METHOD = "GPSProcessingMethod";
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final int ORIENTATION_NORMAL = 1;
    public static final int ORIENTATION_FLIP_HORIZONTAL = 2;
    public static final int ORIENTATION_ROTATE_180 = 3;
    public static final int ORIENTATION_FLIP_VERTICAL = 4;
    public static final int ORIENTATION_TRANSPOSE = 5;
    public static final int ORIENTATION_ROTATE_90 = 6;
    public static final int ORIENTATION_TRANSVERSE = 7;
    public static final int ORIENTATION_ROTATE_270 = 8;
    public static final int WHITEBALANCE_AUTO = 0;
    public static final int WHITEBALANCE_MANUAL = 1;

    public ExifInterface(String filename) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public String getAttribute(String tag) {
        throw new RuntimeException("Stub!");
    }

    public int getAttributeInt(String tag, int defaultValue) {
        throw new RuntimeException("Stub!");
    }

    public double getAttributeDouble(String tag, double defaultValue) {
        throw new RuntimeException("Stub!");
    }

    public void setAttribute(String tag, String value) {
        throw new RuntimeException("Stub!");
    }

    public void saveAttributes() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public boolean hasThumbnail() {
        throw new RuntimeException("Stub!");
    }

    public byte[] getThumbnail() {
        throw new RuntimeException("Stub!");
    }

    public boolean getLatLong(float[] output) {
        throw new RuntimeException("Stub!");
    }

    public double getAltitude(double defaultValue) {
        throw new RuntimeException("Stub!");
    }
}
