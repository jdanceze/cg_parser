package android.app.backup;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/backup/RestoreObserver.class */
public abstract class RestoreObserver {
    public RestoreObserver() {
        throw new RuntimeException("Stub!");
    }

    public void restoreStarting(int numPackages) {
        throw new RuntimeException("Stub!");
    }

    public void onUpdate(int nowBeingRestored, String currentPackage) {
        throw new RuntimeException("Stub!");
    }

    public void restoreFinished(int error) {
        throw new RuntimeException("Stub!");
    }
}
