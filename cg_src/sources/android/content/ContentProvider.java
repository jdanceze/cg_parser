package android.content;

import android.content.pm.PathPermission;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ContentProvider.class */
public abstract class ContentProvider implements ComponentCallbacks2 {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ContentProvider$PipeDataWriter.class */
    public interface PipeDataWriter<T> {
        void writeDataToPipe(ParcelFileDescriptor parcelFileDescriptor, Uri uri, String str, Bundle bundle, T t);
    }

    public abstract boolean onCreate();

    public abstract Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2);

    public abstract String getType(Uri uri);

    public abstract Uri insert(Uri uri, ContentValues contentValues);

    public abstract int delete(Uri uri, String str, String[] strArr);

    public abstract int update(Uri uri, ContentValues contentValues, String str, String[] strArr);

    public ContentProvider() {
        throw new RuntimeException("Stub!");
    }

    public final Context getContext() {
        throw new RuntimeException("Stub!");
    }

    protected final void setReadPermission(String permission) {
        throw new RuntimeException("Stub!");
    }

    public final String getReadPermission() {
        throw new RuntimeException("Stub!");
    }

    protected final void setWritePermission(String permission) {
        throw new RuntimeException("Stub!");
    }

    public final String getWritePermission() {
        throw new RuntimeException("Stub!");
    }

    protected final void setPathPermissions(PathPermission[] permissions) {
        throw new RuntimeException("Stub!");
    }

    public final PathPermission[] getPathPermissions() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ComponentCallbacks2
    public void onTrimMemory(int level) {
        throw new RuntimeException("Stub!");
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        throw new RuntimeException("Stub!");
    }

    public int bulkInsert(Uri uri, ContentValues[] values) {
        throw new RuntimeException("Stub!");
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
        throw new RuntimeException("Stub!");
    }

    protected final ParcelFileDescriptor openFileHelper(Uri uri, String mode) throws FileNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public String[] getStreamTypes(Uri uri, String mimeTypeFilter) {
        throw new RuntimeException("Stub!");
    }

    public AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts) throws FileNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public <T> ParcelFileDescriptor openPipeHelper(Uri uri, String mimeType, Bundle opts, T args, PipeDataWriter<T> func) throws FileNotFoundException {
        throw new RuntimeException("Stub!");
    }

    protected boolean isTemporary() {
        throw new RuntimeException("Stub!");
    }

    public void attachInfo(Context context, ProviderInfo info) {
        throw new RuntimeException("Stub!");
    }

    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        throw new RuntimeException("Stub!");
    }

    public Bundle call(String method, String arg, Bundle extras) {
        throw new RuntimeException("Stub!");
    }

    public void shutdown() {
        throw new RuntimeException("Stub!");
    }
}
