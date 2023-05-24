package android.webkit;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/CacheManager.class */
public final class CacheManager {

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/CacheManager$CacheResult.class */
    public static class CacheResult {
        public CacheResult() {
            throw new RuntimeException("Stub!");
        }

        public int getHttpStatusCode() {
            throw new RuntimeException("Stub!");
        }

        public long getContentLength() {
            throw new RuntimeException("Stub!");
        }

        public String getLocalPath() {
            throw new RuntimeException("Stub!");
        }

        public long getExpires() {
            throw new RuntimeException("Stub!");
        }

        public String getExpiresString() {
            throw new RuntimeException("Stub!");
        }

        public String getLastModified() {
            throw new RuntimeException("Stub!");
        }

        public String getETag() {
            throw new RuntimeException("Stub!");
        }

        public String getMimeType() {
            throw new RuntimeException("Stub!");
        }

        public String getLocation() {
            throw new RuntimeException("Stub!");
        }

        public String getEncoding() {
            throw new RuntimeException("Stub!");
        }

        public String getContentDisposition() {
            throw new RuntimeException("Stub!");
        }

        public InputStream getInputStream() {
            throw new RuntimeException("Stub!");
        }

        public OutputStream getOutputStream() {
            throw new RuntimeException("Stub!");
        }

        public void setInputStream(InputStream stream) {
            throw new RuntimeException("Stub!");
        }

        public void setEncoding(String encoding) {
            throw new RuntimeException("Stub!");
        }
    }

    public CacheManager() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static File getCacheFileBaseDir() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static boolean cacheDisabled() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static boolean startCacheTransaction() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static boolean endCacheTransaction() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static CacheResult getCacheFile(String url, Map<String, String> headers) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static void saveCacheFile(String url, CacheResult cacheResult) {
        throw new RuntimeException("Stub!");
    }
}
