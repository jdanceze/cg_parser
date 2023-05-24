package android.view.accessibility;

import android.os.Bundle;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/accessibility/AccessibilityNodeProvider.class */
public abstract class AccessibilityNodeProvider {
    public AccessibilityNodeProvider() {
        throw new RuntimeException("Stub!");
    }

    public AccessibilityNodeInfo createAccessibilityNodeInfo(int virtualViewId) {
        throw new RuntimeException("Stub!");
    }

    public boolean performAction(int virtualViewId, int action, Bundle arguments) {
        throw new RuntimeException("Stub!");
    }

    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String text, int virtualViewId) {
        throw new RuntimeException("Stub!");
    }
}
