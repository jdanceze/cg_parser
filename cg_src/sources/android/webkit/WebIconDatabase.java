package android.webkit;

import android.graphics.Bitmap;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebIconDatabase.class */
public class WebIconDatabase {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebIconDatabase$IconListener.class */
    public interface IconListener {
        void onReceivedIcon(String str, Bitmap bitmap);
    }

    WebIconDatabase() {
        throw new RuntimeException("Stub!");
    }

    public void open(String path) {
        throw new RuntimeException("Stub!");
    }

    public void close() {
        throw new RuntimeException("Stub!");
    }

    public void removeAllIcons() {
        throw new RuntimeException("Stub!");
    }

    public void requestIconForPageUrl(String url, IconListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void retainIconForPageUrl(String url) {
        throw new RuntimeException("Stub!");
    }

    public void releaseIconForPageUrl(String url) {
        throw new RuntimeException("Stub!");
    }

    public static WebIconDatabase getInstance() {
        throw new RuntimeException("Stub!");
    }
}
