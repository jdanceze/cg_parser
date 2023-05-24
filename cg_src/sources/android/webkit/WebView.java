package android.webkit;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.net.http.SslCertificate;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.AbsoluteLayout;
import java.io.File;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebView.class */
public class WebView extends AbsoluteLayout implements ViewTreeObserver.OnGlobalFocusChangeListener, ViewGroup.OnHierarchyChangeListener {
    public static final String SCHEME_TEL = "tel:";
    public static final String SCHEME_MAILTO = "mailto:";
    public static final String SCHEME_GEO = "geo:0,0?q=";

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebView$FindListener.class */
    public interface FindListener {
        void onFindResultReceived(int i, int i2, boolean z);
    }

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebView$PictureListener.class */
    public interface PictureListener {
        @Deprecated
        void onNewPicture(WebView webView, Picture picture);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebView$WebViewTransport.class */
    public class WebViewTransport {
        public WebViewTransport() {
            throw new RuntimeException("Stub!");
        }

        public synchronized void setWebView(WebView webview) {
            throw new RuntimeException("Stub!");
        }

        public synchronized WebView getWebView() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebView$HitTestResult.class */
    public static class HitTestResult {
        public static final int UNKNOWN_TYPE = 0;
        @Deprecated
        public static final int ANCHOR_TYPE = 1;
        public static final int PHONE_TYPE = 2;
        public static final int GEO_TYPE = 3;
        public static final int EMAIL_TYPE = 4;
        public static final int IMAGE_TYPE = 5;
        @Deprecated
        public static final int IMAGE_ANCHOR_TYPE = 6;
        public static final int SRC_ANCHOR_TYPE = 7;
        public static final int SRC_IMAGE_ANCHOR_TYPE = 8;
        public static final int EDIT_TEXT_TYPE = 9;

        HitTestResult() {
            throw new RuntimeException("Stub!");
        }

        public int getType() {
            throw new RuntimeException("Stub!");
        }

        public String getExtra() {
            throw new RuntimeException("Stub!");
        }
    }

    public WebView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public WebView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public WebView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public WebView(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setHorizontalScrollbarOverlay(boolean overlay) {
        throw new RuntimeException("Stub!");
    }

    public void setVerticalScrollbarOverlay(boolean overlay) {
        throw new RuntimeException("Stub!");
    }

    public boolean overlayHorizontalScrollbar() {
        throw new RuntimeException("Stub!");
    }

    public boolean overlayVerticalScrollbar() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getVisibleTitleHeight() {
        throw new RuntimeException("Stub!");
    }

    public SslCertificate getCertificate() {
        throw new RuntimeException("Stub!");
    }

    public void setCertificate(SslCertificate certificate) {
        throw new RuntimeException("Stub!");
    }

    public void savePassword(String host, String username, String password) {
        throw new RuntimeException("Stub!");
    }

    public void setHttpAuthUsernamePassword(String host, String realm, String username, String password) {
        throw new RuntimeException("Stub!");
    }

    public String[] getHttpAuthUsernamePassword(String host, String realm) {
        throw new RuntimeException("Stub!");
    }

    public void destroy() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void enablePlatformNotifications() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void disablePlatformNotifications() {
        throw new RuntimeException("Stub!");
    }

    public void setNetworkAvailable(boolean networkUp) {
        throw new RuntimeException("Stub!");
    }

    public WebBackForwardList saveState(Bundle outState) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean savePicture(Bundle b, File dest) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean restorePicture(Bundle b, File src) {
        throw new RuntimeException("Stub!");
    }

    public WebBackForwardList restoreState(Bundle inState) {
        throw new RuntimeException("Stub!");
    }

    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        throw new RuntimeException("Stub!");
    }

    public void loadUrl(String url) {
        throw new RuntimeException("Stub!");
    }

    public void postUrl(String url, byte[] postData) {
        throw new RuntimeException("Stub!");
    }

    public void loadData(String data, String mimeType, String encoding) {
        throw new RuntimeException("Stub!");
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        throw new RuntimeException("Stub!");
    }

    public void saveWebArchive(String filename) {
        throw new RuntimeException("Stub!");
    }

    public void saveWebArchive(String basename, boolean autoname, ValueCallback<String> callback) {
        throw new RuntimeException("Stub!");
    }

    public void stopLoading() {
        throw new RuntimeException("Stub!");
    }

    public void reload() {
        throw new RuntimeException("Stub!");
    }

    public boolean canGoBack() {
        throw new RuntimeException("Stub!");
    }

    public void goBack() {
        throw new RuntimeException("Stub!");
    }

    public boolean canGoForward() {
        throw new RuntimeException("Stub!");
    }

    public void goForward() {
        throw new RuntimeException("Stub!");
    }

    public boolean canGoBackOrForward(int steps) {
        throw new RuntimeException("Stub!");
    }

    public void goBackOrForward(int steps) {
        throw new RuntimeException("Stub!");
    }

    public boolean isPrivateBrowsingEnabled() {
        throw new RuntimeException("Stub!");
    }

    public boolean pageUp(boolean top) {
        throw new RuntimeException("Stub!");
    }

    public boolean pageDown(boolean bottom) {
        throw new RuntimeException("Stub!");
    }

    public void clearView() {
        throw new RuntimeException("Stub!");
    }

    public Picture capturePicture() {
        throw new RuntimeException("Stub!");
    }

    public float getScale() {
        throw new RuntimeException("Stub!");
    }

    public void setInitialScale(int scaleInPercent) {
        throw new RuntimeException("Stub!");
    }

    public void invokeZoomPicker() {
        throw new RuntimeException("Stub!");
    }

    public HitTestResult getHitTestResult() {
        throw new RuntimeException("Stub!");
    }

    public void requestFocusNodeHref(Message hrefMsg) {
        throw new RuntimeException("Stub!");
    }

    public void requestImageRef(Message msg) {
        throw new RuntimeException("Stub!");
    }

    public String getUrl() {
        throw new RuntimeException("Stub!");
    }

    public String getOriginalUrl() {
        throw new RuntimeException("Stub!");
    }

    public String getTitle() {
        throw new RuntimeException("Stub!");
    }

    public Bitmap getFavicon() {
        throw new RuntimeException("Stub!");
    }

    public int getProgress() {
        throw new RuntimeException("Stub!");
    }

    public int getContentHeight() {
        throw new RuntimeException("Stub!");
    }

    public void pauseTimers() {
        throw new RuntimeException("Stub!");
    }

    public void resumeTimers() {
        throw new RuntimeException("Stub!");
    }

    public void onPause() {
        throw new RuntimeException("Stub!");
    }

    public void onResume() {
        throw new RuntimeException("Stub!");
    }

    public void freeMemory() {
        throw new RuntimeException("Stub!");
    }

    public void clearCache(boolean includeDiskFiles) {
        throw new RuntimeException("Stub!");
    }

    public void clearFormData() {
        throw new RuntimeException("Stub!");
    }

    public void clearHistory() {
        throw new RuntimeException("Stub!");
    }

    public void clearSslPreferences() {
        throw new RuntimeException("Stub!");
    }

    public WebBackForwardList copyBackForwardList() {
        throw new RuntimeException("Stub!");
    }

    public void setFindListener(FindListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void findNext(boolean forward) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int findAll(String find) {
        throw new RuntimeException("Stub!");
    }

    public void findAllAsync(String find) {
        throw new RuntimeException("Stub!");
    }

    public boolean showFindDialog(String text, boolean showIme) {
        throw new RuntimeException("Stub!");
    }

    public static String findAddress(String addr) {
        throw new RuntimeException("Stub!");
    }

    public void clearMatches() {
        throw new RuntimeException("Stub!");
    }

    public void documentHasImages(Message response) {
        throw new RuntimeException("Stub!");
    }

    public void setWebViewClient(WebViewClient client) {
        throw new RuntimeException("Stub!");
    }

    public void setDownloadListener(DownloadListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setWebChromeClient(WebChromeClient client) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setPictureListener(PictureListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void addJavascriptInterface(Object object, String name) {
        throw new RuntimeException("Stub!");
    }

    public void removeJavascriptInterface(String name) {
        throw new RuntimeException("Stub!");
    }

    public WebSettings getSettings() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void emulateShiftHeld() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup.OnHierarchyChangeListener
    @Deprecated
    public void onChildViewAdded(View parent, View child) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup.OnHierarchyChangeListener
    @Deprecated
    public void onChildViewRemoved(View p, View child) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewTreeObserver.OnGlobalFocusChangeListener
    @Deprecated
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        throw new RuntimeException("Stub!");
    }

    public void setMapTrackballToArrowKeys(boolean setMap) {
        throw new RuntimeException("Stub!");
    }

    public void flingScroll(int vx, int vy) {
        throw new RuntimeException("Stub!");
    }

    public boolean canZoomIn() {
        throw new RuntimeException("Stub!");
    }

    public boolean canZoomOut() {
        throw new RuntimeException("Stub!");
    }

    public boolean zoomIn() {
        throw new RuntimeException("Stub!");
    }

    public boolean zoomOut() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void debugDump() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setOverScrollMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setScrollBarStyle(int style) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int computeHorizontalScrollRange() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int computeHorizontalScrollOffset() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int computeVerticalScrollRange() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int computeVerticalScrollOffset() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected int computeVerticalScrollExtent() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void computeScroll() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onGenericMotionEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsoluteLayout, android.view.ViewGroup
    @Deprecated
    public boolean shouldDelayChildPressedState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int visibility) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean performLongClick() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration newConfig) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View changedView, int visibility) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsoluteLayout, android.view.View
    @Deprecated
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setBackgroundColor(int color) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setLayerType(int layerType, Paint paint) {
        throw new RuntimeException("Stub!");
    }
}
