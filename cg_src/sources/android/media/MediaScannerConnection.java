package android.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaScannerConnection.class */
public class MediaScannerConnection implements ServiceConnection {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaScannerConnection$MediaScannerConnectionClient.class */
    public interface MediaScannerConnectionClient extends OnScanCompletedListener {
        void onMediaScannerConnected();

        @Override // android.media.MediaScannerConnection.OnScanCompletedListener
        void onScanCompleted(String str, Uri uri);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaScannerConnection$OnScanCompletedListener.class */
    public interface OnScanCompletedListener {
        void onScanCompleted(String str, Uri uri);
    }

    public MediaScannerConnection(Context context, MediaScannerConnectionClient client) {
        throw new RuntimeException("Stub!");
    }

    public void connect() {
        throw new RuntimeException("Stub!");
    }

    public void disconnect() {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean isConnected() {
        throw new RuntimeException("Stub!");
    }

    public void scanFile(String path, String mimeType) {
        throw new RuntimeException("Stub!");
    }

    public static void scanFile(Context context, String[] paths, String[] mimeTypes, OnScanCompletedListener callback) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName className, IBinder service) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName className) {
        throw new RuntimeException("Stub!");
    }
}
