package android.webkit;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebSettings.class */
public abstract class WebSettings {
    public static final int LOAD_DEFAULT = -1;
    public static final int LOAD_NORMAL = 0;
    public static final int LOAD_CACHE_ELSE_NETWORK = 1;
    public static final int LOAD_NO_CACHE = 2;
    public static final int LOAD_CACHE_ONLY = 3;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebSettings$LayoutAlgorithm.class */
    public enum LayoutAlgorithm {
        NARROW_COLUMNS,
        NORMAL,
        SINGLE_COLUMN
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebSettings$PluginState.class */
    public enum PluginState {
        OFF,
        ON,
        ON_DEMAND
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebSettings$RenderPriority.class */
    public enum RenderPriority {
        HIGH,
        LOW,
        NORMAL
    }

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebSettings$TextSize.class */
    public enum TextSize {
        LARGER,
        LARGEST,
        NORMAL,
        SMALLER,
        SMALLEST
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebSettings$ZoomDensity.class */
    public enum ZoomDensity {
        CLOSE,
        FAR,
        MEDIUM
    }

    public abstract void setAllowUniversalAccessFromFileURLs(boolean z);

    public abstract void setAllowFileAccessFromFileURLs(boolean z);

    public abstract boolean getAllowUniversalAccessFromFileURLs();

    public abstract boolean getAllowFileAccessFromFileURLs();

    WebSettings() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setNavDump(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean getNavDump() {
        throw new RuntimeException("Stub!");
    }

    public void setSupportZoom(boolean support) {
        throw new RuntimeException("Stub!");
    }

    public boolean supportZoom() {
        throw new RuntimeException("Stub!");
    }

    public void setBuiltInZoomControls(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean getBuiltInZoomControls() {
        throw new RuntimeException("Stub!");
    }

    public void setDisplayZoomControls(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean getDisplayZoomControls() {
        throw new RuntimeException("Stub!");
    }

    public void setAllowFileAccess(boolean allow) {
        throw new RuntimeException("Stub!");
    }

    public boolean getAllowFileAccess() {
        throw new RuntimeException("Stub!");
    }

    public void setAllowContentAccess(boolean allow) {
        throw new RuntimeException("Stub!");
    }

    public boolean getAllowContentAccess() {
        throw new RuntimeException("Stub!");
    }

    public void setLoadWithOverviewMode(boolean overview) {
        throw new RuntimeException("Stub!");
    }

    public boolean getLoadWithOverviewMode() {
        throw new RuntimeException("Stub!");
    }

    public void setEnableSmoothTransition(boolean enable) {
        throw new RuntimeException("Stub!");
    }

    public boolean enableSmoothTransition() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setUseWebViewBackgroundForOverscrollBackground(boolean view) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean getUseWebViewBackgroundForOverscrollBackground() {
        throw new RuntimeException("Stub!");
    }

    public void setSaveFormData(boolean save) {
        throw new RuntimeException("Stub!");
    }

    public boolean getSaveFormData() {
        throw new RuntimeException("Stub!");
    }

    public void setSavePassword(boolean save) {
        throw new RuntimeException("Stub!");
    }

    public boolean getSavePassword() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setTextZoom(int textZoom) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getTextZoom() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public synchronized void setTextSize(TextSize t) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public synchronized TextSize getTextSize() {
        throw new RuntimeException("Stub!");
    }

    public void setDefaultZoom(ZoomDensity zoom) {
        throw new RuntimeException("Stub!");
    }

    public ZoomDensity getDefaultZoom() {
        throw new RuntimeException("Stub!");
    }

    public void setLightTouchEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean getLightTouchEnabled() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public synchronized void setUseDoubleTree(boolean use) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public synchronized boolean getUseDoubleTree() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public synchronized void setUserAgent(int ua) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public synchronized int getUserAgent() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setUseWideViewPort(boolean use) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean getUseWideViewPort() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setSupportMultipleWindows(boolean support) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean supportMultipleWindows() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setLayoutAlgorithm(LayoutAlgorithm l) {
        throw new RuntimeException("Stub!");
    }

    public synchronized LayoutAlgorithm getLayoutAlgorithm() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setStandardFontFamily(String font) {
        throw new RuntimeException("Stub!");
    }

    public synchronized String getStandardFontFamily() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setFixedFontFamily(String font) {
        throw new RuntimeException("Stub!");
    }

    public synchronized String getFixedFontFamily() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setSansSerifFontFamily(String font) {
        throw new RuntimeException("Stub!");
    }

    public synchronized String getSansSerifFontFamily() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setSerifFontFamily(String font) {
        throw new RuntimeException("Stub!");
    }

    public synchronized String getSerifFontFamily() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setCursiveFontFamily(String font) {
        throw new RuntimeException("Stub!");
    }

    public synchronized String getCursiveFontFamily() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setFantasyFontFamily(String font) {
        throw new RuntimeException("Stub!");
    }

    public synchronized String getFantasyFontFamily() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMinimumFontSize(int size) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getMinimumFontSize() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setMinimumLogicalFontSize(int size) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getMinimumLogicalFontSize() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setDefaultFontSize(int size) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getDefaultFontSize() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setDefaultFixedFontSize(int size) {
        throw new RuntimeException("Stub!");
    }

    public synchronized int getDefaultFixedFontSize() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setLoadsImagesAutomatically(boolean flag) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean getLoadsImagesAutomatically() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setBlockNetworkImage(boolean flag) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean getBlockNetworkImage() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setBlockNetworkLoads(boolean flag) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean getBlockNetworkLoads() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setJavaScriptEnabled(boolean flag) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public synchronized void setPluginsEnabled(boolean flag) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setPluginState(PluginState state) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public synchronized void setPluginsPath(String pluginsPath) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setDatabasePath(String databasePath) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setGeolocationDatabasePath(String databasePath) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setAppCacheEnabled(boolean flag) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setAppCachePath(String appCachePath) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setAppCacheMaxSize(long appCacheMaxSize) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setDatabaseEnabled(boolean flag) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setDomStorageEnabled(boolean flag) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean getDomStorageEnabled() {
        throw new RuntimeException("Stub!");
    }

    public synchronized String getDatabasePath() {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean getDatabaseEnabled() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setGeolocationEnabled(boolean flag) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean getJavaScriptEnabled() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public synchronized boolean getPluginsEnabled() {
        throw new RuntimeException("Stub!");
    }

    public synchronized PluginState getPluginState() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public synchronized String getPluginsPath() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setJavaScriptCanOpenWindowsAutomatically(boolean flag) {
        throw new RuntimeException("Stub!");
    }

    public synchronized boolean getJavaScriptCanOpenWindowsAutomatically() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setDefaultTextEncodingName(String encoding) {
        throw new RuntimeException("Stub!");
    }

    public synchronized String getDefaultTextEncodingName() {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setUserAgentString(String ua) {
        throw new RuntimeException("Stub!");
    }

    public synchronized String getUserAgentString() {
        throw new RuntimeException("Stub!");
    }

    public void setNeedInitialFocus(boolean flag) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void setRenderPriority(RenderPriority priority) {
        throw new RuntimeException("Stub!");
    }

    public void setCacheMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    public int getCacheMode() {
        throw new RuntimeException("Stub!");
    }
}
