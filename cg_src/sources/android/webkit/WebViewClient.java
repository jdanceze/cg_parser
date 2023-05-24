package android.webkit;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebViewClient.class */
public class WebViewClient {
    public static final int ERROR_UNKNOWN = -1;
    public static final int ERROR_HOST_LOOKUP = -2;
    public static final int ERROR_UNSUPPORTED_AUTH_SCHEME = -3;
    public static final int ERROR_AUTHENTICATION = -4;
    public static final int ERROR_PROXY_AUTHENTICATION = -5;
    public static final int ERROR_CONNECT = -6;
    public static final int ERROR_IO = -7;
    public static final int ERROR_TIMEOUT = -8;
    public static final int ERROR_REDIRECT_LOOP = -9;
    public static final int ERROR_UNSUPPORTED_SCHEME = -10;
    public static final int ERROR_FAILED_SSL_HANDSHAKE = -11;
    public static final int ERROR_BAD_URL = -12;
    public static final int ERROR_FILE = -13;
    public static final int ERROR_FILE_NOT_FOUND = -14;
    public static final int ERROR_TOO_MANY_REQUESTS = -15;

    public WebViewClient() {
        throw new RuntimeException("Stub!");
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        throw new RuntimeException("Stub!");
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        throw new RuntimeException("Stub!");
    }

    public void onPageFinished(WebView view, String url) {
        throw new RuntimeException("Stub!");
    }

    public void onLoadResource(WebView view, String url) {
        throw new RuntimeException("Stub!");
    }

    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        throw new RuntimeException("Stub!");
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        throw new RuntimeException("Stub!");
    }

    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        throw new RuntimeException("Stub!");
    }

    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        throw new RuntimeException("Stub!");
    }

    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        throw new RuntimeException("Stub!");
    }

    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        throw new RuntimeException("Stub!");
    }

    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        throw new RuntimeException("Stub!");
    }

    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        throw new RuntimeException("Stub!");
    }
}
