package android.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.ActionMode;
import android.view.InputQueue;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/Window.class */
public abstract class Window {
    public static final int FEATURE_OPTIONS_PANEL = 0;
    public static final int FEATURE_NO_TITLE = 1;
    public static final int FEATURE_PROGRESS = 2;
    public static final int FEATURE_LEFT_ICON = 3;
    public static final int FEATURE_RIGHT_ICON = 4;
    public static final int FEATURE_INDETERMINATE_PROGRESS = 5;
    public static final int FEATURE_CONTEXT_MENU = 6;
    public static final int FEATURE_CUSTOM_TITLE = 7;
    public static final int FEATURE_ACTION_BAR = 8;
    public static final int FEATURE_ACTION_BAR_OVERLAY = 9;
    public static final int FEATURE_ACTION_MODE_OVERLAY = 10;
    public static final int PROGRESS_VISIBILITY_ON = -1;
    public static final int PROGRESS_VISIBILITY_OFF = -2;
    public static final int PROGRESS_INDETERMINATE_ON = -3;
    public static final int PROGRESS_INDETERMINATE_OFF = -4;
    public static final int PROGRESS_START = 0;
    public static final int PROGRESS_END = 10000;
    public static final int PROGRESS_SECONDARY_START = 20000;
    public static final int PROGRESS_SECONDARY_END = 30000;
    protected static final int DEFAULT_FEATURES = 65;
    public static final int ID_ANDROID_CONTENT = 16908290;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/Window$Callback.class */
    public interface Callback {
        boolean dispatchKeyEvent(KeyEvent keyEvent);

        boolean dispatchKeyShortcutEvent(KeyEvent keyEvent);

        boolean dispatchTouchEvent(MotionEvent motionEvent);

        boolean dispatchTrackballEvent(MotionEvent motionEvent);

        boolean dispatchGenericMotionEvent(MotionEvent motionEvent);

        boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        View onCreatePanelView(int i);

        boolean onCreatePanelMenu(int i, Menu menu);

        boolean onPreparePanel(int i, View view, Menu menu);

        boolean onMenuOpened(int i, Menu menu);

        boolean onMenuItemSelected(int i, MenuItem menuItem);

        void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams);

        void onContentChanged();

        void onWindowFocusChanged(boolean z);

        void onAttachedToWindow();

        void onDetachedFromWindow();

        void onPanelClosed(int i, Menu menu);

        boolean onSearchRequested();

        ActionMode onWindowStartingActionMode(ActionMode.Callback callback);

        void onActionModeStarted(ActionMode actionMode);

        void onActionModeFinished(ActionMode actionMode);
    }

    public abstract void takeSurface(SurfaceHolder.Callback2 callback2);

    public abstract void takeInputQueue(InputQueue.Callback callback);

    public abstract boolean isFloating();

    public abstract void setContentView(int i);

    public abstract void setContentView(View view);

    public abstract void setContentView(View view, ViewGroup.LayoutParams layoutParams);

    public abstract void addContentView(View view, ViewGroup.LayoutParams layoutParams);

    public abstract View getCurrentFocus();

    public abstract LayoutInflater getLayoutInflater();

    public abstract void setTitle(CharSequence charSequence);

    public abstract void setTitleColor(int i);

    public abstract void openPanel(int i, KeyEvent keyEvent);

    public abstract void closePanel(int i);

    public abstract void togglePanel(int i, KeyEvent keyEvent);

    public abstract void invalidatePanelMenu(int i);

    public abstract boolean performPanelShortcut(int i, int i2, KeyEvent keyEvent, int i3);

    public abstract boolean performPanelIdentifierAction(int i, int i2, int i3);

    public abstract void closeAllPanels();

    public abstract boolean performContextMenuIdentifierAction(int i, int i2);

    public abstract void onConfigurationChanged(Configuration configuration);

    public abstract void setBackgroundDrawable(Drawable drawable);

    public abstract void setFeatureDrawableResource(int i, int i2);

    public abstract void setFeatureDrawableUri(int i, Uri uri);

    public abstract void setFeatureDrawable(int i, Drawable drawable);

    public abstract void setFeatureDrawableAlpha(int i, int i2);

    public abstract void setFeatureInt(int i, int i2);

    public abstract void takeKeyEvents(boolean z);

    public abstract boolean superDispatchKeyEvent(KeyEvent keyEvent);

    public abstract boolean superDispatchKeyShortcutEvent(KeyEvent keyEvent);

    public abstract boolean superDispatchTouchEvent(MotionEvent motionEvent);

    public abstract boolean superDispatchTrackballEvent(MotionEvent motionEvent);

    public abstract boolean superDispatchGenericMotionEvent(MotionEvent motionEvent);

    public abstract View getDecorView();

    public abstract View peekDecorView();

    public abstract Bundle saveHierarchyState();

    public abstract void restoreHierarchyState(Bundle bundle);

    protected abstract void onActive();

    public abstract void setChildDrawable(int i, Drawable drawable);

    public abstract void setChildInt(int i, int i2);

    public abstract boolean isShortcutKey(int i, KeyEvent keyEvent);

    public abstract void setVolumeControlStream(int i);

    public abstract int getVolumeControlStream();

    public Window(Context context) {
        throw new RuntimeException("Stub!");
    }

    public final Context getContext() {
        throw new RuntimeException("Stub!");
    }

    public final TypedArray getWindowStyle() {
        throw new RuntimeException("Stub!");
    }

    public void setContainer(Window container) {
        throw new RuntimeException("Stub!");
    }

    public final Window getContainer() {
        throw new RuntimeException("Stub!");
    }

    public final boolean hasChildren() {
        throw new RuntimeException("Stub!");
    }

    public void setWindowManager(WindowManager wm, IBinder appToken, String appName) {
        throw new RuntimeException("Stub!");
    }

    public void setWindowManager(WindowManager wm, IBinder appToken, String appName, boolean hardwareAccelerated) {
        throw new RuntimeException("Stub!");
    }

    public WindowManager getWindowManager() {
        throw new RuntimeException("Stub!");
    }

    public void setCallback(Callback callback) {
        throw new RuntimeException("Stub!");
    }

    public final Callback getCallback() {
        throw new RuntimeException("Stub!");
    }

    public void setLayout(int width, int height) {
        throw new RuntimeException("Stub!");
    }

    public void setGravity(int gravity) {
        throw new RuntimeException("Stub!");
    }

    public void setType(int type) {
        throw new RuntimeException("Stub!");
    }

    public void setFormat(int format) {
        throw new RuntimeException("Stub!");
    }

    public void setWindowAnimations(int resId) {
        throw new RuntimeException("Stub!");
    }

    public void setSoftInputMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    public void addFlags(int flags) {
        throw new RuntimeException("Stub!");
    }

    public void clearFlags(int flags) {
        throw new RuntimeException("Stub!");
    }

    public void setFlags(int flags, int mask) {
        throw new RuntimeException("Stub!");
    }

    public void setDimAmount(float amount) {
        throw new RuntimeException("Stub!");
    }

    public void setAttributes(WindowManager.LayoutParams a) {
        throw new RuntimeException("Stub!");
    }

    public final WindowManager.LayoutParams getAttributes() {
        throw new RuntimeException("Stub!");
    }

    protected final int getForcedWindowFlags() {
        throw new RuntimeException("Stub!");
    }

    protected final boolean hasSoftInputMode() {
        throw new RuntimeException("Stub!");
    }

    public boolean requestFeature(int featureId) {
        throw new RuntimeException("Stub!");
    }

    public final void makeActive() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isActive() {
        throw new RuntimeException("Stub!");
    }

    public View findViewById(int id) {
        throw new RuntimeException("Stub!");
    }

    public void setBackgroundDrawableResource(int resid) {
        throw new RuntimeException("Stub!");
    }

    protected final int getFeatures() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasFeature(int feature) {
        throw new RuntimeException("Stub!");
    }

    protected final int getLocalFeatures() {
        throw new RuntimeException("Stub!");
    }

    protected void setDefaultWindowFormat(int format) {
        throw new RuntimeException("Stub!");
    }

    public void setUiOptions(int uiOptions) {
        throw new RuntimeException("Stub!");
    }

    public void setUiOptions(int uiOptions, int mask) {
        throw new RuntimeException("Stub!");
    }
}
