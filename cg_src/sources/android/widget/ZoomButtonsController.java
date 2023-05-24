package android.widget;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ZoomButtonsController.class */
public class ZoomButtonsController implements View.OnTouchListener {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ZoomButtonsController$OnZoomListener.class */
    public interface OnZoomListener {
        void onVisibilityChanged(boolean z);

        void onZoom(boolean z);
    }

    public ZoomButtonsController(View ownerView) {
        throw new RuntimeException("Stub!");
    }

    public void setZoomInEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public void setZoomOutEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public void setZoomSpeed(long speed) {
        throw new RuntimeException("Stub!");
    }

    public void setOnZoomListener(OnZoomListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setFocusable(boolean focusable) {
        throw new RuntimeException("Stub!");
    }

    public boolean isAutoDismissed() {
        throw new RuntimeException("Stub!");
    }

    public void setAutoDismissed(boolean autoDismiss) {
        throw new RuntimeException("Stub!");
    }

    public boolean isVisible() {
        throw new RuntimeException("Stub!");
    }

    public void setVisible(boolean visible) {
        throw new RuntimeException("Stub!");
    }

    public ViewGroup getContainer() {
        throw new RuntimeException("Stub!");
    }

    public View getZoomControls() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
        throw new RuntimeException("Stub!");
    }
}
