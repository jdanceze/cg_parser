package android.content;

import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/Context.class */
public abstract class Context {
    public static final int MODE_PRIVATE = 0;
    public static final int MODE_WORLD_READABLE = 1;
    public static final int MODE_WORLD_WRITEABLE = 2;
    public static final int MODE_APPEND = 32768;
    public static final int MODE_MULTI_PROCESS = 4;
    public static final int MODE_ENABLE_WRITE_AHEAD_LOGGING = 8;
    public static final int BIND_AUTO_CREATE = 1;
    public static final int BIND_DEBUG_UNBIND = 2;
    public static final int BIND_NOT_FOREGROUND = 4;
    public static final int BIND_ABOVE_CLIENT = 8;
    public static final int BIND_ALLOW_OOM_MANAGEMENT = 16;
    public static final int BIND_WAIVE_PRIORITY = 32;
    public static final int BIND_IMPORTANT = 64;
    public static final int BIND_ADJUST_WITH_ACTIVITY = 128;
    public static final String POWER_SERVICE = "power";
    public static final String WINDOW_SERVICE = "window";
    public static final String LAYOUT_INFLATER_SERVICE = "layout_inflater";
    public static final String ACCOUNT_SERVICE = "account";
    public static final String ACTIVITY_SERVICE = "activity";
    public static final String ALARM_SERVICE = "alarm";
    public static final String NOTIFICATION_SERVICE = "notification";
    public static final String ACCESSIBILITY_SERVICE = "accessibility";
    public static final String KEYGUARD_SERVICE = "keyguard";
    public static final String LOCATION_SERVICE = "location";
    public static final String SEARCH_SERVICE = "search";
    public static final String SENSOR_SERVICE = "sensor";
    public static final String STORAGE_SERVICE = "storage";
    public static final String WALLPAPER_SERVICE = "wallpaper";
    public static final String VIBRATOR_SERVICE = "vibrator";
    public static final String CONNECTIVITY_SERVICE = "connectivity";
    public static final String WIFI_SERVICE = "wifi";
    public static final String WIFI_P2P_SERVICE = "wifip2p";
    public static final String NSD_SERVICE = "servicediscovery";
    public static final String AUDIO_SERVICE = "audio";
    public static final String MEDIA_ROUTER_SERVICE = "media_router";
    public static final String TELEPHONY_SERVICE = "phone";
    public static final String CLIPBOARD_SERVICE = "clipboard";
    public static final String INPUT_METHOD_SERVICE = "input_method";
    public static final String TEXT_SERVICES_MANAGER_SERVICE = "textservices";
    public static final String DROPBOX_SERVICE = "dropbox";
    public static final String DEVICE_POLICY_SERVICE = "device_policy";
    public static final String UI_MODE_SERVICE = "uimode";
    public static final String DOWNLOAD_SERVICE = "download";
    public static final String NFC_SERVICE = "nfc";
    public static final String USB_SERVICE = "usb";
    public static final String INPUT_SERVICE = "input";
    public static final int CONTEXT_INCLUDE_CODE = 1;
    public static final int CONTEXT_IGNORE_SECURITY = 2;
    public static final int CONTEXT_RESTRICTED = 4;

    public abstract AssetManager getAssets();

    public abstract Resources getResources();

    public abstract PackageManager getPackageManager();

    public abstract ContentResolver getContentResolver();

    public abstract Looper getMainLooper();

    public abstract Context getApplicationContext();

    public abstract void setTheme(int i);

    public abstract Resources.Theme getTheme();

    public abstract ClassLoader getClassLoader();

    public abstract String getPackageName();

    public abstract ApplicationInfo getApplicationInfo();

    public abstract String getPackageResourcePath();

    public abstract String getPackageCodePath();

    public abstract SharedPreferences getSharedPreferences(String str, int i);

    public abstract FileInputStream openFileInput(String str) throws FileNotFoundException;

    public abstract FileOutputStream openFileOutput(String str, int i) throws FileNotFoundException;

    public abstract boolean deleteFile(String str);

    public abstract File getFileStreamPath(String str);

    public abstract File getFilesDir();

    public abstract File getExternalFilesDir(String str);

    public abstract File getObbDir();

    public abstract File getCacheDir();

    public abstract File getExternalCacheDir();

    public abstract String[] fileList();

    public abstract File getDir(String str, int i);

    public abstract SQLiteDatabase openOrCreateDatabase(String str, int i, SQLiteDatabase.CursorFactory cursorFactory);

    public abstract SQLiteDatabase openOrCreateDatabase(String str, int i, SQLiteDatabase.CursorFactory cursorFactory, DatabaseErrorHandler databaseErrorHandler);

    public abstract boolean deleteDatabase(String str);

    public abstract File getDatabasePath(String str);

    public abstract String[] databaseList();

    @Deprecated
    public abstract Drawable getWallpaper();

    @Deprecated
    public abstract Drawable peekWallpaper();

    @Deprecated
    public abstract int getWallpaperDesiredMinimumWidth();

    @Deprecated
    public abstract int getWallpaperDesiredMinimumHeight();

    @Deprecated
    public abstract void setWallpaper(Bitmap bitmap) throws IOException;

    @Deprecated
    public abstract void setWallpaper(InputStream inputStream) throws IOException;

    @Deprecated
    public abstract void clearWallpaper() throws IOException;

    public abstract void startActivity(Intent intent);

    public abstract void startActivity(Intent intent, Bundle bundle);

    public abstract void startActivities(Intent[] intentArr);

    public abstract void startActivities(Intent[] intentArr, Bundle bundle);

    public abstract void startIntentSender(IntentSender intentSender, Intent intent, int i, int i2, int i3) throws IntentSender.SendIntentException;

    public abstract void startIntentSender(IntentSender intentSender, Intent intent, int i, int i2, int i3, Bundle bundle) throws IntentSender.SendIntentException;

    public abstract void sendBroadcast(Intent intent);

    public abstract void sendBroadcast(Intent intent, String str);

    public abstract void sendOrderedBroadcast(Intent intent, String str);

    public abstract void sendOrderedBroadcast(Intent intent, String str, BroadcastReceiver broadcastReceiver, Handler handler, int i, String str2, Bundle bundle);

    public abstract void sendStickyBroadcast(Intent intent);

    public abstract void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver broadcastReceiver, Handler handler, int i, String str, Bundle bundle);

    public abstract void removeStickyBroadcast(Intent intent);

    public abstract Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter);

    public abstract Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, String str, Handler handler);

    public abstract void unregisterReceiver(BroadcastReceiver broadcastReceiver);

    public abstract ComponentName startService(Intent intent);

    public abstract boolean stopService(Intent intent);

    public abstract boolean bindService(Intent intent, ServiceConnection serviceConnection, int i);

    public abstract void unbindService(ServiceConnection serviceConnection);

    public abstract boolean startInstrumentation(ComponentName componentName, String str, Bundle bundle);

    public abstract Object getSystemService(String str);

    public abstract int checkPermission(String str, int i, int i2);

    public abstract int checkCallingPermission(String str);

    public abstract int checkCallingOrSelfPermission(String str);

    public abstract void enforcePermission(String str, int i, int i2, String str2);

    public abstract void enforceCallingPermission(String str, String str2);

    public abstract void enforceCallingOrSelfPermission(String str, String str2);

    public abstract void grantUriPermission(String str, Uri uri, int i);

    public abstract void revokeUriPermission(Uri uri, int i);

    public abstract int checkUriPermission(Uri uri, int i, int i2, int i3);

    public abstract int checkCallingUriPermission(Uri uri, int i);

    public abstract int checkCallingOrSelfUriPermission(Uri uri, int i);

    public abstract int checkUriPermission(Uri uri, String str, String str2, int i, int i2, int i3);

    public abstract void enforceUriPermission(Uri uri, int i, int i2, int i3, String str);

    public abstract void enforceCallingUriPermission(Uri uri, int i, String str);

    public abstract void enforceCallingOrSelfUriPermission(Uri uri, int i, String str);

    public abstract void enforceUriPermission(Uri uri, String str, String str2, int i, int i2, int i3, String str3);

    public abstract Context createPackageContext(String str, int i) throws PackageManager.NameNotFoundException;

    public Context() {
        throw new RuntimeException("Stub!");
    }

    public void registerComponentCallbacks(ComponentCallbacks callback) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterComponentCallbacks(ComponentCallbacks callback) {
        throw new RuntimeException("Stub!");
    }

    public final CharSequence getText(int resId) {
        throw new RuntimeException("Stub!");
    }

    public final String getString(int resId) {
        throw new RuntimeException("Stub!");
    }

    public final String getString(int resId, Object... formatArgs) {
        throw new RuntimeException("Stub!");
    }

    public final TypedArray obtainStyledAttributes(int[] attrs) {
        throw new RuntimeException("Stub!");
    }

    public final TypedArray obtainStyledAttributes(int resid, int[] attrs) throws Resources.NotFoundException {
        throw new RuntimeException("Stub!");
    }

    public final TypedArray obtainStyledAttributes(AttributeSet set, int[] attrs) {
        throw new RuntimeException("Stub!");
    }

    public final TypedArray obtainStyledAttributes(AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes) {
        throw new RuntimeException("Stub!");
    }

    public boolean isRestricted() {
        throw new RuntimeException("Stub!");
    }
}
