package android.view;

import android.content.Context;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ActionProvider.class */
public abstract class ActionProvider {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ActionProvider$VisibilityListener.class */
    public interface VisibilityListener {
        void onActionProviderVisibilityChanged(boolean z);
    }

    @Deprecated
    public abstract View onCreateActionView();

    public ActionProvider(Context context) {
        throw new RuntimeException("Stub!");
    }

    public View onCreateActionView(MenuItem forItem) {
        throw new RuntimeException("Stub!");
    }

    public boolean overridesItemVisibility() {
        throw new RuntimeException("Stub!");
    }

    public boolean isVisible() {
        throw new RuntimeException("Stub!");
    }

    public void refreshVisibility() {
        throw new RuntimeException("Stub!");
    }

    public boolean onPerformDefaultAction() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasSubMenu() {
        throw new RuntimeException("Stub!");
    }

    public void onPrepareSubMenu(SubMenu subMenu) {
        throw new RuntimeException("Stub!");
    }

    public void setVisibilityListener(VisibilityListener listener) {
        throw new RuntimeException("Stub!");
    }
}
