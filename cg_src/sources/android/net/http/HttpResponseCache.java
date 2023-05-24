package android.net.http;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/http/HttpResponseCache.class */
public final class HttpResponseCache extends ResponseCache implements Closeable {
    HttpResponseCache() {
        throw new RuntimeException("Stub!");
    }

    public static HttpResponseCache getInstalled() {
        throw new RuntimeException("Stub!");
    }

    public static HttpResponseCache install(File directory, long maxSize) throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // java.net.ResponseCache
    public CacheResponse get(URI uri, String requestMethod, Map<String, List<String>> requestHeaders) throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // java.net.ResponseCache
    public CacheRequest put(URI uri, URLConnection urlConnection) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public long size() {
        throw new RuntimeException("Stub!");
    }

    public long maxSize() {
        throw new RuntimeException("Stub!");
    }

    public void flush() {
        throw new RuntimeException("Stub!");
    }

    public int getNetworkCount() {
        throw new RuntimeException("Stub!");
    }

    public int getHitCount() {
        throw new RuntimeException("Stub!");
    }

    public int getRequestCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void delete() throws IOException {
        throw new RuntimeException("Stub!");
    }
}
