package android.view;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewTreeObserver.class */
public final class ViewTreeObserver {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewTreeObserver$OnDrawListener.class */
    public interface OnDrawListener {
        void onDraw();
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewTreeObserver$OnGlobalFocusChangeListener.class */
    public interface OnGlobalFocusChangeListener {
        void onGlobalFocusChanged(View view, View view2);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewTreeObserver$OnGlobalLayoutListener.class */
    public interface OnGlobalLayoutListener {
        void onGlobalLayout();
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewTreeObserver$OnPreDrawListener.class */
    public interface OnPreDrawListener {
        boolean onPreDraw();
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewTreeObserver$OnScrollChangedListener.class */
    public interface OnScrollChangedListener {
        void onScrollChanged();
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewTreeObserver$OnTouchModeChangeListener.class */
    public interface OnTouchModeChangeListener {
        void onTouchModeChanged(boolean z);
    }

    ViewTreeObserver() {
        throw new RuntimeException("Stub!");
    }

    public void addOnGlobalFocusChangeListener(OnGlobalFocusChangeListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void removeOnGlobalFocusChangeListener(OnGlobalFocusChangeListener victim) {
        throw new RuntimeException("Stub!");
    }

    public void addOnGlobalLayoutListener(OnGlobalLayoutListener listener) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void removeGlobalOnLayoutListener(OnGlobalLayoutListener victim) {
        throw new RuntimeException("Stub!");
    }

    public void removeOnGlobalLayoutListener(OnGlobalLayoutListener victim) {
        throw new RuntimeException("Stub!");
    }

    public void addOnPreDrawListener(OnPreDrawListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void removeOnPreDrawListener(OnPreDrawListener victim) {
        throw new RuntimeException("Stub!");
    }

    public void addOnDrawListener(OnDrawListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void removeOnDrawListener(OnDrawListener victim) {
        throw new RuntimeException("Stub!");
    }

    public void addOnScrollChangedListener(OnScrollChangedListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void removeOnScrollChangedListener(OnScrollChangedListener victim) {
        throw new RuntimeException("Stub!");
    }

    public void addOnTouchModeChangeListener(OnTouchModeChangeListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void removeOnTouchModeChangeListener(OnTouchModeChangeListener victim) {
        throw new RuntimeException("Stub!");
    }

    public boolean isAlive() {
        throw new RuntimeException("Stub!");
    }

    public final void dispatchOnGlobalLayout() {
        throw new RuntimeException("Stub!");
    }

    public final boolean dispatchOnPreDraw() {
        throw new RuntimeException("Stub!");
    }

    public final void dispatchOnDraw() {
        throw new RuntimeException("Stub!");
    }
}
