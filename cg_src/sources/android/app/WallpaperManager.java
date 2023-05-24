package android.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/WallpaperManager.class */
public class WallpaperManager {
    public static final String ACTION_LIVE_WALLPAPER_CHOOSER = "android.service.wallpaper.LIVE_WALLPAPER_CHOOSER";
    public static final String ACTION_CHANGE_LIVE_WALLPAPER = "android.service.wallpaper.CHANGE_LIVE_WALLPAPER";
    public static final String EXTRA_LIVE_WALLPAPER_COMPONENT = "android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT";
    public static final String WALLPAPER_PREVIEW_META_DATA = "android.wallpaper.preview";
    public static final String COMMAND_TAP = "android.wallpaper.tap";
    public static final String COMMAND_SECONDARY_TAP = "android.wallpaper.secondaryTap";
    public static final String COMMAND_DROP = "android.home.drop";

    WallpaperManager() {
        throw new RuntimeException("Stub!");
    }

    public static WallpaperManager getInstance(Context context) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getDrawable() {
        throw new RuntimeException("Stub!");
    }

    public Drawable peekDrawable() {
        throw new RuntimeException("Stub!");
    }

    public Drawable getFastDrawable() {
        throw new RuntimeException("Stub!");
    }

    public Drawable peekFastDrawable() {
        throw new RuntimeException("Stub!");
    }

    public void forgetLoadedWallpaper() {
        throw new RuntimeException("Stub!");
    }

    public WallpaperInfo getWallpaperInfo() {
        throw new RuntimeException("Stub!");
    }

    public void setResource(int resid) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void setBitmap(Bitmap bitmap) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void setStream(InputStream data) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public int getDesiredMinimumWidth() {
        throw new RuntimeException("Stub!");
    }

    public int getDesiredMinimumHeight() {
        throw new RuntimeException("Stub!");
    }

    public void suggestDesiredDimensions(int minimumWidth, int minimumHeight) {
        throw new RuntimeException("Stub!");
    }

    public void setWallpaperOffsets(IBinder windowToken, float xOffset, float yOffset) {
        throw new RuntimeException("Stub!");
    }

    public void setWallpaperOffsetSteps(float xStep, float yStep) {
        throw new RuntimeException("Stub!");
    }

    public void sendWallpaperCommand(IBinder windowToken, String action, int x, int y, int z, Bundle extras) {
        throw new RuntimeException("Stub!");
    }

    public void clearWallpaperOffsets(IBinder windowToken) {
        throw new RuntimeException("Stub!");
    }

    public void clear() throws IOException {
        throw new RuntimeException("Stub!");
    }
}
