package android.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ServiceInfo;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/accessibility/AccessibilityManager.class */
public final class AccessibilityManager {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/accessibility/AccessibilityManager$AccessibilityStateChangeListener.class */
    public interface AccessibilityStateChangeListener {
        void onAccessibilityStateChanged(boolean z);
    }

    AccessibilityManager() {
        throw new RuntimeException("Stub!");
    }

    public boolean isEnabled() {
        throw new RuntimeException("Stub!");
    }

    public boolean isTouchExplorationEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void sendAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    public void interrupt() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public List<ServiceInfo> getAccessibilityServiceList() {
        throw new RuntimeException("Stub!");
    }

    public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList() {
        throw new RuntimeException("Stub!");
    }

    public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int feedbackTypeFlags) {
        throw new RuntimeException("Stub!");
    }

    public boolean addAccessibilityStateChangeListener(AccessibilityStateChangeListener listener) {
        throw new RuntimeException("Stub!");
    }

    public boolean removeAccessibilityStateChangeListener(AccessibilityStateChangeListener listener) {
        throw new RuntimeException("Stub!");
    }
}
