package android.service.wallpaper;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/service/wallpaper/WallpaperService.class */
public abstract class WallpaperService extends Service {
    public static final String SERVICE_INTERFACE = "android.service.wallpaper.WallpaperService";
    public static final String SERVICE_META_DATA = "android.service.wallpaper";

    public abstract Engine onCreateEngine();

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/service/wallpaper/WallpaperService$Engine.class */
    public class Engine {
        public Engine() {
            throw new RuntimeException("Stub!");
        }

        public SurfaceHolder getSurfaceHolder() {
            throw new RuntimeException("Stub!");
        }

        public int getDesiredMinimumWidth() {
            throw new RuntimeException("Stub!");
        }

        public int getDesiredMinimumHeight() {
            throw new RuntimeException("Stub!");
        }

        public boolean isVisible() {
            throw new RuntimeException("Stub!");
        }

        public boolean isPreview() {
            throw new RuntimeException("Stub!");
        }

        public void setTouchEventsEnabled(boolean enabled) {
            throw new RuntimeException("Stub!");
        }

        public void setOffsetNotificationsEnabled(boolean enabled) {
            throw new RuntimeException("Stub!");
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            throw new RuntimeException("Stub!");
        }

        public void onDestroy() {
            throw new RuntimeException("Stub!");
        }

        public void onVisibilityChanged(boolean visible) {
            throw new RuntimeException("Stub!");
        }

        public void onTouchEvent(MotionEvent event) {
            throw new RuntimeException("Stub!");
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            throw new RuntimeException("Stub!");
        }

        public Bundle onCommand(String action, int x, int y, int z, Bundle extras, boolean resultRequested) {
            throw new RuntimeException("Stub!");
        }

        public void onDesiredSizeChanged(int desiredWidth, int desiredHeight) {
            throw new RuntimeException("Stub!");
        }

        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            throw new RuntimeException("Stub!");
        }

        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            throw new RuntimeException("Stub!");
        }

        public void onSurfaceCreated(SurfaceHolder holder) {
            throw new RuntimeException("Stub!");
        }

        public void onSurfaceDestroyed(SurfaceHolder holder) {
            throw new RuntimeException("Stub!");
        }

        protected void dump(String prefix, FileDescriptor fd, PrintWriter out, String[] args) {
            throw new RuntimeException("Stub!");
        }
    }

    public WallpaperService() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public void onCreate() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public void onDestroy() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    protected void dump(FileDescriptor fd, PrintWriter out, String[] args) {
        throw new RuntimeException("Stub!");
    }
}
