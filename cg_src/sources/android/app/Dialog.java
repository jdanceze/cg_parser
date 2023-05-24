package android.app;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Dialog.class */
public class Dialog implements DialogInterface, Window.Callback, KeyEvent.Callback, View.OnCreateContextMenuListener {
    public Dialog(Context context) {
        throw new RuntimeException("Stub!");
    }

    public Dialog(Context context, int theme) {
        throw new RuntimeException("Stub!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Dialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        throw new RuntimeException("Stub!");
    }

    public final Context getContext() {
        throw new RuntimeException("Stub!");
    }

    public ActionBar getActionBar() {
        throw new RuntimeException("Stub!");
    }

    public final void setOwnerActivity(Activity activity) {
        throw new RuntimeException("Stub!");
    }

    public final Activity getOwnerActivity() {
        throw new RuntimeException("Stub!");
    }

    public boolean isShowing() {
        throw new RuntimeException("Stub!");
    }

    public void show() {
        throw new RuntimeException("Stub!");
    }

    public void hide() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.DialogInterface
    public void dismiss() {
        throw new RuntimeException("Stub!");
    }

    protected void onCreate(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    protected void onStart() {
        throw new RuntimeException("Stub!");
    }

    protected void onStop() {
        throw new RuntimeException("Stub!");
    }

    public Bundle onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    public Window getWindow() {
        throw new RuntimeException("Stub!");
    }

    public View getCurrentFocus() {
        throw new RuntimeException("Stub!");
    }

    public View findViewById(int id) {
        throw new RuntimeException("Stub!");
    }

    public void setContentView(int layoutResID) {
        throw new RuntimeException("Stub!");
    }

    public void setContentView(View view) {
        throw new RuntimeException("Stub!");
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    public void setTitle(CharSequence title) {
        throw new RuntimeException("Stub!");
    }

    public void setTitle(int titleId) {
        throw new RuntimeException("Stub!");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.KeyEvent.Callback
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.KeyEvent.Callback
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public void onBackPressed() {
        throw new RuntimeException("Stub!");
    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean onTouchEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean onTrackballEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onContentChanged() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onAttachedToWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public View onCreatePanelView(int featureId) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean onMenuOpened(int featureId, Menu menu) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onPanelClosed(int featureId, Menu menu) {
        throw new RuntimeException("Stub!");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        throw new RuntimeException("Stub!");
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        throw new RuntimeException("Stub!");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        throw new RuntimeException("Stub!");
    }

    public void onOptionsMenuClosed(Menu menu) {
        throw new RuntimeException("Stub!");
    }

    public void openOptionsMenu() {
        throw new RuntimeException("Stub!");
    }

    public void closeOptionsMenu() {
        throw new RuntimeException("Stub!");
    }

    public void invalidateOptionsMenu() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        throw new RuntimeException("Stub!");
    }

    public void registerForContextMenu(View view) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterForContextMenu(View view) {
        throw new RuntimeException("Stub!");
    }

    public void openContextMenu(View view) {
        throw new RuntimeException("Stub!");
    }

    public boolean onContextItemSelected(MenuItem item) {
        throw new RuntimeException("Stub!");
    }

    public void onContextMenuClosed(Menu menu) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean onSearchRequested() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onActionModeStarted(ActionMode mode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onActionModeFinished(ActionMode mode) {
        throw new RuntimeException("Stub!");
    }

    public void takeKeyEvents(boolean get) {
        throw new RuntimeException("Stub!");
    }

    public final boolean requestWindowFeature(int featureId) {
        throw new RuntimeException("Stub!");
    }

    public final void setFeatureDrawableResource(int featureId, int resId) {
        throw new RuntimeException("Stub!");
    }

    public final void setFeatureDrawableUri(int featureId, Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public final void setFeatureDrawable(int featureId, Drawable drawable) {
        throw new RuntimeException("Stub!");
    }

    public final void setFeatureDrawableAlpha(int featureId, int alpha) {
        throw new RuntimeException("Stub!");
    }

    public LayoutInflater getLayoutInflater() {
        throw new RuntimeException("Stub!");
    }

    public void setCancelable(boolean flag) {
        throw new RuntimeException("Stub!");
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.DialogInterface
    public void cancel() {
        throw new RuntimeException("Stub!");
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setCancelMessage(Message msg) {
        throw new RuntimeException("Stub!");
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnShowListener(DialogInterface.OnShowListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setDismissMessage(Message msg) {
        throw new RuntimeException("Stub!");
    }

    public final void setVolumeControlStream(int streamType) {
        throw new RuntimeException("Stub!");
    }

    public final int getVolumeControlStream() {
        throw new RuntimeException("Stub!");
    }

    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        throw new RuntimeException("Stub!");
    }
}
