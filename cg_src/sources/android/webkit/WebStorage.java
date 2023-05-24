package android.webkit;

import java.util.Map;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebStorage.class */
public class WebStorage {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebStorage$QuotaUpdater.class */
    public interface QuotaUpdater {
        void updateQuota(long j);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/webkit/WebStorage$Origin.class */
    public static class Origin {
        Origin() {
            throw new RuntimeException("Stub!");
        }

        public String getOrigin() {
            throw new RuntimeException("Stub!");
        }

        public long getQuota() {
            throw new RuntimeException("Stub!");
        }

        public long getUsage() {
            throw new RuntimeException("Stub!");
        }
    }

    WebStorage() {
        throw new RuntimeException("Stub!");
    }

    public void getOrigins(ValueCallback<Map> callback) {
        throw new RuntimeException("Stub!");
    }

    public void getUsageForOrigin(String origin, ValueCallback<Long> callback) {
        throw new RuntimeException("Stub!");
    }

    public void getQuotaForOrigin(String origin, ValueCallback<Long> callback) {
        throw new RuntimeException("Stub!");
    }

    public void setQuotaForOrigin(String origin, long quota) {
        throw new RuntimeException("Stub!");
    }

    public void deleteOrigin(String origin) {
        throw new RuntimeException("Stub!");
    }

    public void deleteAllData() {
        throw new RuntimeException("Stub!");
    }

    public static WebStorage getInstance() {
        throw new RuntimeException("Stub!");
    }
}
