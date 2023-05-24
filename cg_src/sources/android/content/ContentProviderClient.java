package android.content;

import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ContentProviderClient.class */
public class ContentProviderClient {
    ContentProviderClient() {
        throw new RuntimeException("Stub!");
    }

    public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sortOrder) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public String getType(Uri url) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public String[] getStreamTypes(Uri url, String mimeTypeFilter) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public Uri insert(Uri url, ContentValues initialValues) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public int bulkInsert(Uri url, ContentValues[] initialValues) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public int delete(Uri url, String selection, String[] selectionArgs) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public int update(Uri url, ContentValues values, String selection, String[] selectionArgs) throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public ParcelFileDescriptor openFile(Uri url, String mode) throws RemoteException, FileNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public AssetFileDescriptor openAssetFile(Uri url, String mode) throws RemoteException, FileNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri uri, String mimeType, Bundle opts) throws RemoteException, FileNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws RemoteException, OperationApplicationException {
        throw new RuntimeException("Stub!");
    }

    public boolean release() {
        throw new RuntimeException("Stub!");
    }

    public ContentProvider getLocalContentProvider() {
        throw new RuntimeException("Stub!");
    }
}
