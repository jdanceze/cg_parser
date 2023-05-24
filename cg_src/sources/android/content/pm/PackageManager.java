package android.content.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.AndroidException;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/PackageManager.class */
public abstract class PackageManager {
    public static final int GET_ACTIVITIES = 1;
    public static final int GET_RECEIVERS = 2;
    public static final int GET_SERVICES = 4;
    public static final int GET_PROVIDERS = 8;
    public static final int GET_INSTRUMENTATION = 16;
    public static final int GET_INTENT_FILTERS = 32;
    public static final int GET_SIGNATURES = 64;
    public static final int GET_RESOLVED_FILTER = 64;
    public static final int GET_META_DATA = 128;
    public static final int GET_GIDS = 256;
    public static final int GET_DISABLED_COMPONENTS = 512;
    public static final int GET_SHARED_LIBRARY_FILES = 1024;
    public static final int GET_URI_PERMISSION_PATTERNS = 2048;
    public static final int GET_PERMISSIONS = 4096;
    public static final int GET_UNINSTALLED_PACKAGES = 8192;
    public static final int GET_CONFIGURATIONS = 16384;
    public static final int MATCH_DEFAULT_ONLY = 65536;
    public static final int PERMISSION_GRANTED = 0;
    public static final int PERMISSION_DENIED = -1;
    public static final int SIGNATURE_MATCH = 0;
    public static final int SIGNATURE_NEITHER_SIGNED = 1;
    public static final int SIGNATURE_FIRST_NOT_SIGNED = -1;
    public static final int SIGNATURE_SECOND_NOT_SIGNED = -2;
    public static final int SIGNATURE_NO_MATCH = -3;
    public static final int SIGNATURE_UNKNOWN_PACKAGE = -4;
    public static final int COMPONENT_ENABLED_STATE_DEFAULT = 0;
    public static final int COMPONENT_ENABLED_STATE_ENABLED = 1;
    public static final int COMPONENT_ENABLED_STATE_DISABLED = 2;
    public static final int COMPONENT_ENABLED_STATE_DISABLED_USER = 3;
    public static final int DONT_KILL_APP = 1;
    public static final int VERIFICATION_ALLOW = 1;
    public static final int VERIFICATION_REJECT = -1;
    public static final String FEATURE_AUDIO_LOW_LATENCY = "android.hardware.audio.low_latency";
    public static final String FEATURE_BLUETOOTH = "android.hardware.bluetooth";
    public static final String FEATURE_CAMERA = "android.hardware.camera";
    public static final String FEATURE_CAMERA_AUTOFOCUS = "android.hardware.camera.autofocus";
    public static final String FEATURE_CAMERA_FLASH = "android.hardware.camera.flash";
    public static final String FEATURE_CAMERA_FRONT = "android.hardware.camera.front";
    public static final String FEATURE_LOCATION = "android.hardware.location";
    public static final String FEATURE_LOCATION_GPS = "android.hardware.location.gps";
    public static final String FEATURE_LOCATION_NETWORK = "android.hardware.location.network";
    public static final String FEATURE_MICROPHONE = "android.hardware.microphone";
    public static final String FEATURE_NFC = "android.hardware.nfc";
    public static final String FEATURE_SENSOR_ACCELEROMETER = "android.hardware.sensor.accelerometer";
    public static final String FEATURE_SENSOR_BAROMETER = "android.hardware.sensor.barometer";
    public static final String FEATURE_SENSOR_COMPASS = "android.hardware.sensor.compass";
    public static final String FEATURE_SENSOR_GYROSCOPE = "android.hardware.sensor.gyroscope";
    public static final String FEATURE_SENSOR_LIGHT = "android.hardware.sensor.light";
    public static final String FEATURE_SENSOR_PROXIMITY = "android.hardware.sensor.proximity";
    public static final String FEATURE_TELEPHONY = "android.hardware.telephony";
    public static final String FEATURE_TELEPHONY_CDMA = "android.hardware.telephony.cdma";
    public static final String FEATURE_TELEPHONY_GSM = "android.hardware.telephony.gsm";
    public static final String FEATURE_USB_HOST = "android.hardware.usb.host";
    public static final String FEATURE_USB_ACCESSORY = "android.hardware.usb.accessory";
    public static final String FEATURE_SIP = "android.software.sip";
    public static final String FEATURE_SIP_VOIP = "android.software.sip.voip";
    public static final String FEATURE_TOUCHSCREEN = "android.hardware.touchscreen";
    public static final String FEATURE_TOUCHSCREEN_MULTITOUCH = "android.hardware.touchscreen.multitouch";
    public static final String FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT = "android.hardware.touchscreen.multitouch.distinct";
    public static final String FEATURE_TOUCHSCREEN_MULTITOUCH_JAZZHAND = "android.hardware.touchscreen.multitouch.jazzhand";
    public static final String FEATURE_FAKETOUCH = "android.hardware.faketouch";
    public static final String FEATURE_FAKETOUCH_MULTITOUCH_DISTINCT = "android.hardware.faketouch.multitouch.distinct";
    public static final String FEATURE_FAKETOUCH_MULTITOUCH_JAZZHAND = "android.hardware.faketouch.multitouch.jazzhand";
    public static final String FEATURE_SCREEN_PORTRAIT = "android.hardware.screen.portrait";
    public static final String FEATURE_SCREEN_LANDSCAPE = "android.hardware.screen.landscape";
    public static final String FEATURE_LIVE_WALLPAPER = "android.software.live_wallpaper";
    public static final String FEATURE_WIFI = "android.hardware.wifi";
    public static final String FEATURE_WIFI_DIRECT = "android.hardware.wifi.direct";
    public static final String FEATURE_TELEVISION = "android.hardware.type.television";
    public static final String EXTRA_VERIFICATION_ID = "android.content.pm.extra.VERIFICATION_ID";

    public abstract PackageInfo getPackageInfo(String str, int i) throws NameNotFoundException;

    public abstract String[] currentToCanonicalPackageNames(String[] strArr);

    public abstract String[] canonicalToCurrentPackageNames(String[] strArr);

    public abstract Intent getLaunchIntentForPackage(String str);

    public abstract int[] getPackageGids(String str) throws NameNotFoundException;

    public abstract PermissionInfo getPermissionInfo(String str, int i) throws NameNotFoundException;

    public abstract List<PermissionInfo> queryPermissionsByGroup(String str, int i) throws NameNotFoundException;

    public abstract PermissionGroupInfo getPermissionGroupInfo(String str, int i) throws NameNotFoundException;

    public abstract List<PermissionGroupInfo> getAllPermissionGroups(int i);

    public abstract ApplicationInfo getApplicationInfo(String str, int i) throws NameNotFoundException;

    public abstract ActivityInfo getActivityInfo(ComponentName componentName, int i) throws NameNotFoundException;

    public abstract ActivityInfo getReceiverInfo(ComponentName componentName, int i) throws NameNotFoundException;

    public abstract ServiceInfo getServiceInfo(ComponentName componentName, int i) throws NameNotFoundException;

    public abstract ProviderInfo getProviderInfo(ComponentName componentName, int i) throws NameNotFoundException;

    public abstract List<PackageInfo> getInstalledPackages(int i);

    public abstract int checkPermission(String str, String str2);

    public abstract boolean addPermission(PermissionInfo permissionInfo);

    public abstract boolean addPermissionAsync(PermissionInfo permissionInfo);

    public abstract void removePermission(String str);

    public abstract int checkSignatures(String str, String str2);

    public abstract int checkSignatures(int i, int i2);

    public abstract String[] getPackagesForUid(int i);

    public abstract String getNameForUid(int i);

    public abstract List<ApplicationInfo> getInstalledApplications(int i);

    public abstract String[] getSystemSharedLibraryNames();

    public abstract FeatureInfo[] getSystemAvailableFeatures();

    public abstract boolean hasSystemFeature(String str);

    public abstract ResolveInfo resolveActivity(Intent intent, int i);

    public abstract List<ResolveInfo> queryIntentActivities(Intent intent, int i);

    public abstract List<ResolveInfo> queryIntentActivityOptions(ComponentName componentName, Intent[] intentArr, Intent intent, int i);

    public abstract List<ResolveInfo> queryBroadcastReceivers(Intent intent, int i);

    public abstract ResolveInfo resolveService(Intent intent, int i);

    public abstract List<ResolveInfo> queryIntentServices(Intent intent, int i);

    public abstract ProviderInfo resolveContentProvider(String str, int i);

    public abstract List<ProviderInfo> queryContentProviders(String str, int i, int i2);

    public abstract InstrumentationInfo getInstrumentationInfo(ComponentName componentName, int i) throws NameNotFoundException;

    public abstract List<InstrumentationInfo> queryInstrumentation(String str, int i);

    public abstract Drawable getDrawable(String str, int i, ApplicationInfo applicationInfo);

    public abstract Drawable getActivityIcon(ComponentName componentName) throws NameNotFoundException;

    public abstract Drawable getActivityIcon(Intent intent) throws NameNotFoundException;

    public abstract Drawable getDefaultActivityIcon();

    public abstract Drawable getApplicationIcon(ApplicationInfo applicationInfo);

    public abstract Drawable getApplicationIcon(String str) throws NameNotFoundException;

    public abstract Drawable getActivityLogo(ComponentName componentName) throws NameNotFoundException;

    public abstract Drawable getActivityLogo(Intent intent) throws NameNotFoundException;

    public abstract Drawable getApplicationLogo(ApplicationInfo applicationInfo);

    public abstract Drawable getApplicationLogo(String str) throws NameNotFoundException;

    public abstract CharSequence getText(String str, int i, ApplicationInfo applicationInfo);

    public abstract XmlResourceParser getXml(String str, int i, ApplicationInfo applicationInfo);

    public abstract CharSequence getApplicationLabel(ApplicationInfo applicationInfo);

    public abstract Resources getResourcesForActivity(ComponentName componentName) throws NameNotFoundException;

    public abstract Resources getResourcesForApplication(ApplicationInfo applicationInfo) throws NameNotFoundException;

    public abstract Resources getResourcesForApplication(String str) throws NameNotFoundException;

    public abstract void verifyPendingInstall(int i, int i2);

    public abstract void setInstallerPackageName(String str, String str2);

    public abstract String getInstallerPackageName(String str);

    @Deprecated
    public abstract void addPackageToPreferred(String str);

    @Deprecated
    public abstract void removePackageFromPreferred(String str);

    public abstract List<PackageInfo> getPreferredPackages(int i);

    @Deprecated
    public abstract void addPreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNameArr, ComponentName componentName);

    public abstract void clearPackagePreferredActivities(String str);

    public abstract int getPreferredActivities(List<IntentFilter> list, List<ComponentName> list2, String str);

    public abstract void setComponentEnabledSetting(ComponentName componentName, int i, int i2);

    public abstract int getComponentEnabledSetting(ComponentName componentName);

    public abstract void setApplicationEnabledSetting(String str, int i, int i2);

    public abstract int getApplicationEnabledSetting(String str);

    public abstract boolean isSafeMode();

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/pm/PackageManager$NameNotFoundException.class */
    public static class NameNotFoundException extends AndroidException {
        public NameNotFoundException() {
            throw new RuntimeException("Stub!");
        }

        public NameNotFoundException(String name) {
            throw new RuntimeException("Stub!");
        }
    }

    public PackageManager() {
        throw new RuntimeException("Stub!");
    }

    public PackageInfo getPackageArchiveInfo(String archiveFilePath, int flags) {
        throw new RuntimeException("Stub!");
    }
}
