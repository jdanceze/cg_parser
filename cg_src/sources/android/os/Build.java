package android.os;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Build.class */
public class Build {
    public static final String UNKNOWN = "unknown";
    public static final String ID = null;
    public static final String DISPLAY = null;
    public static final String PRODUCT = null;
    public static final String DEVICE = null;
    public static final String BOARD = null;
    public static final String CPU_ABI = null;
    public static final String CPU_ABI2 = null;
    public static final String MANUFACTURER = null;
    public static final String BRAND = null;
    public static final String MODEL = null;
    public static final String BOOTLOADER = null;
    @Deprecated
    public static final String RADIO = null;
    public static final String HARDWARE = null;
    public static final String SERIAL = null;
    public static final String TYPE = null;
    public static final String TAGS = null;
    public static final String FINGERPRINT = null;
    public static final long TIME = 0;
    public static final String USER = null;
    public static final String HOST = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Build$VERSION.class */
    public static class VERSION {
        public static final String INCREMENTAL = null;
        public static final String RELEASE = null;
        @Deprecated
        public static final String SDK = null;
        public static final int SDK_INT = 0;
        public static final String CODENAME = null;

        public VERSION() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Build$VERSION_CODES.class */
    public static class VERSION_CODES {
        public static final int CUR_DEVELOPMENT = 10000;
        public static final int BASE = 1;
        public static final int BASE_1_1 = 2;
        public static final int CUPCAKE = 3;
        public static final int DONUT = 4;
        public static final int ECLAIR = 5;
        public static final int ECLAIR_0_1 = 6;
        public static final int ECLAIR_MR1 = 7;
        public static final int FROYO = 8;
        public static final int GINGERBREAD = 9;
        public static final int GINGERBREAD_MR1 = 10;
        public static final int HONEYCOMB = 11;
        public static final int HONEYCOMB_MR1 = 12;
        public static final int HONEYCOMB_MR2 = 13;
        public static final int ICE_CREAM_SANDWICH = 14;
        public static final int ICE_CREAM_SANDWICH_MR1 = 15;
        public static final int JELLY_BEAN = 16;

        public VERSION_CODES() {
            throw new RuntimeException("Stub!");
        }
    }

    public Build() {
        throw new RuntimeException("Stub!");
    }

    public static String getRadioVersion() {
        throw new RuntimeException("Stub!");
    }
}
