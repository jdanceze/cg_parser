package android.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.InputQueue;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.ViewTreeObserver;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/NativeActivity.class */
public class NativeActivity extends Activity implements SurfaceHolder.Callback2, InputQueue.Callback, ViewTreeObserver.OnGlobalLayoutListener {
    public static final String META_DATA_LIB_NAME = "android.app.lib_name";
    public static final String META_DATA_FUNC_NAME = "android.app.func_name";

    public NativeActivity() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onPause() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onResume() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onStart() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onStop() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onLowMemory() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.SurfaceHolder.Callback2
    public void surfaceRedrawNeeded(SurfaceHolder holder) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.InputQueue.Callback
    public void onInputQueueCreated(InputQueue queue) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.InputQueue.Callback
    public void onInputQueueDestroyed(InputQueue queue) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        throw new RuntimeException("Stub!");
    }
}
