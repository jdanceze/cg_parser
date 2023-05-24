package android.content;

import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ContextWrapper.class */
public class ContextWrapper extends Context {
    public ContextWrapper(Context base) {
        throw new RuntimeException("Stub!");
    }

    protected void attachBaseContext(Context base) {
        throw new RuntimeException("Stub!");
    }

    public Context getBaseContext() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public AssetManager getAssets() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public Resources getResources() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public PackageManager getPackageManager() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public ContentResolver getContentResolver() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public Looper getMainLooper() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public Context getApplicationContext() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void setTheme(int resid) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public Resources.Theme getTheme() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public ClassLoader getClassLoader() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public String getPackageName() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public ApplicationInfo getApplicationInfo() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public String getPackageResourcePath() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public String getPackageCodePath() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public SharedPreferences getSharedPreferences(String name, int mode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public boolean deleteFile(String name) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public File getFileStreamPath(String name) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public String[] fileList() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public File getFilesDir() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public File getExternalFilesDir(String type) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public File getObbDir() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public File getCacheDir() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public File getExternalCacheDir() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public File getDir(String name, int mode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public boolean deleteDatabase(String name) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public File getDatabasePath(String name) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public String[] databaseList() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public Drawable getWallpaper() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public Drawable peekWallpaper() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public int getWallpaperDesiredMinimumWidth() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public int getWallpaperDesiredMinimumHeight() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void setWallpaper(Bitmap bitmap) throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void setWallpaper(InputStream data) throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void clearWallpaper() throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void startActivity(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void startActivity(Intent intent, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void startActivities(Intent[] intents) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void startActivities(Intent[] intents, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void sendBroadcast(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void sendBroadcast(Intent intent, String receiverPermission) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void sendOrderedBroadcast(Intent intent, String receiverPermission) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void sendStickyBroadcast(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void removeStickyBroadcast(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void unregisterReceiver(BroadcastReceiver receiver) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public ComponentName startService(Intent service) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public boolean stopService(Intent name) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void unbindService(ServiceConnection conn) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public Object getSystemService(String name) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public int checkPermission(String permission, int pid, int uid) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public int checkCallingPermission(String permission) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public int checkCallingOrSelfPermission(String permission) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void enforcePermission(String permission, int pid, int uid, String message) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void enforceCallingPermission(String permission, String message) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void enforceCallingOrSelfPermission(String permission, String message) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void revokeUriPermission(Uri uri, int modeFlags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public int checkCallingUriPermission(Uri uri, int modeFlags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags, String message) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.Context
    public boolean isRestricted() {
        throw new RuntimeException("Stub!");
    }
}
