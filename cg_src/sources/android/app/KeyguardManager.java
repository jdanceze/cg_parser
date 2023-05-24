package android.app;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/KeyguardManager.class */
public class KeyguardManager {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/KeyguardManager$OnKeyguardExitResult.class */
    public interface OnKeyguardExitResult {
        void onKeyguardExitResult(boolean z);
    }

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/KeyguardManager$KeyguardLock.class */
    public class KeyguardLock {
        KeyguardLock() {
            throw new RuntimeException("Stub!");
        }

        public void disableKeyguard() {
            throw new RuntimeException("Stub!");
        }

        public void reenableKeyguard() {
            throw new RuntimeException("Stub!");
        }
    }

    KeyguardManager() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public KeyguardLock newKeyguardLock(String tag) {
        throw new RuntimeException("Stub!");
    }

    public boolean isKeyguardLocked() {
        throw new RuntimeException("Stub!");
    }

    public boolean isKeyguardSecure() {
        throw new RuntimeException("Stub!");
    }

    public boolean inKeyguardRestrictedInputMode() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void exitKeyguardSecurely(OnKeyguardExitResult callback) {
        throw new RuntimeException("Stub!");
    }
}
