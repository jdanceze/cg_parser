package android.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRouter.class */
public class MediaRouter {
    public static final int ROUTE_TYPE_LIVE_AUDIO = 1;
    public static final int ROUTE_TYPE_USER = 8388608;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRouter$RouteInfo.class */
    public static class RouteInfo {
        public static final int PLAYBACK_TYPE_LOCAL = 0;
        public static final int PLAYBACK_TYPE_REMOTE = 1;
        public static final int PLAYBACK_VOLUME_FIXED = 0;
        public static final int PLAYBACK_VOLUME_VARIABLE = 1;

        RouteInfo() {
            throw new RuntimeException("Stub!");
        }

        public CharSequence getName() {
            throw new RuntimeException("Stub!");
        }

        public CharSequence getName(Context context) {
            throw new RuntimeException("Stub!");
        }

        public CharSequence getStatus() {
            throw new RuntimeException("Stub!");
        }

        public int getSupportedTypes() {
            throw new RuntimeException("Stub!");
        }

        public RouteGroup getGroup() {
            throw new RuntimeException("Stub!");
        }

        public RouteCategory getCategory() {
            throw new RuntimeException("Stub!");
        }

        public Drawable getIconDrawable() {
            throw new RuntimeException("Stub!");
        }

        public void setTag(Object tag) {
            throw new RuntimeException("Stub!");
        }

        public Object getTag() {
            throw new RuntimeException("Stub!");
        }

        public int getPlaybackType() {
            throw new RuntimeException("Stub!");
        }

        public int getPlaybackStream() {
            throw new RuntimeException("Stub!");
        }

        public int getVolume() {
            throw new RuntimeException("Stub!");
        }

        public void requestSetVolume(int volume) {
            throw new RuntimeException("Stub!");
        }

        public void requestUpdateVolume(int direction) {
            throw new RuntimeException("Stub!");
        }

        public int getVolumeMax() {
            throw new RuntimeException("Stub!");
        }

        public int getVolumeHandling() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRouter$UserRouteInfo.class */
    public static class UserRouteInfo extends RouteInfo {
        UserRouteInfo() {
            throw new RuntimeException("Stub!");
        }

        public void setName(CharSequence name) {
            throw new RuntimeException("Stub!");
        }

        public void setName(int resId) {
            throw new RuntimeException("Stub!");
        }

        public void setStatus(CharSequence status) {
            throw new RuntimeException("Stub!");
        }

        public void setRemoteControlClient(RemoteControlClient rcc) {
            throw new RuntimeException("Stub!");
        }

        public RemoteControlClient getRemoteControlClient() {
            throw new RuntimeException("Stub!");
        }

        public void setIconDrawable(Drawable icon) {
            throw new RuntimeException("Stub!");
        }

        public void setIconResource(int resId) {
            throw new RuntimeException("Stub!");
        }

        public void setVolumeCallback(VolumeCallback vcb) {
            throw new RuntimeException("Stub!");
        }

        public void setPlaybackType(int type) {
            throw new RuntimeException("Stub!");
        }

        public void setVolumeHandling(int volumeHandling) {
            throw new RuntimeException("Stub!");
        }

        public void setVolume(int volume) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.media.MediaRouter.RouteInfo
        public void requestSetVolume(int volume) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.media.MediaRouter.RouteInfo
        public void requestUpdateVolume(int direction) {
            throw new RuntimeException("Stub!");
        }

        public void setVolumeMax(int volumeMax) {
            throw new RuntimeException("Stub!");
        }

        public void setPlaybackStream(int stream) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRouter$RouteGroup.class */
    public static class RouteGroup extends RouteInfo {
        RouteGroup() {
            throw new RuntimeException("Stub!");
        }

        public void addRoute(RouteInfo route) {
            throw new RuntimeException("Stub!");
        }

        public void addRoute(RouteInfo route, int insertAt) {
            throw new RuntimeException("Stub!");
        }

        public void removeRoute(RouteInfo route) {
            throw new RuntimeException("Stub!");
        }

        public void removeRoute(int index) {
            throw new RuntimeException("Stub!");
        }

        public int getRouteCount() {
            throw new RuntimeException("Stub!");
        }

        public RouteInfo getRouteAt(int index) {
            throw new RuntimeException("Stub!");
        }

        public void setIconDrawable(Drawable icon) {
            throw new RuntimeException("Stub!");
        }

        public void setIconResource(int resId) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.media.MediaRouter.RouteInfo
        public void requestSetVolume(int volume) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.media.MediaRouter.RouteInfo
        public void requestUpdateVolume(int direction) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.media.MediaRouter.RouteInfo
        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRouter$RouteCategory.class */
    public static class RouteCategory {
        RouteCategory() {
            throw new RuntimeException("Stub!");
        }

        public CharSequence getName() {
            throw new RuntimeException("Stub!");
        }

        public CharSequence getName(Context context) {
            throw new RuntimeException("Stub!");
        }

        public List<RouteInfo> getRoutes(List<RouteInfo> out) {
            throw new RuntimeException("Stub!");
        }

        public int getSupportedTypes() {
            throw new RuntimeException("Stub!");
        }

        public boolean isGroupable() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRouter$Callback.class */
    public static abstract class Callback {
        public abstract void onRouteSelected(MediaRouter mediaRouter, int i, RouteInfo routeInfo);

        public abstract void onRouteUnselected(MediaRouter mediaRouter, int i, RouteInfo routeInfo);

        public abstract void onRouteAdded(MediaRouter mediaRouter, RouteInfo routeInfo);

        public abstract void onRouteRemoved(MediaRouter mediaRouter, RouteInfo routeInfo);

        public abstract void onRouteChanged(MediaRouter mediaRouter, RouteInfo routeInfo);

        public abstract void onRouteGrouped(MediaRouter mediaRouter, RouteInfo routeInfo, RouteGroup routeGroup, int i);

        public abstract void onRouteUngrouped(MediaRouter mediaRouter, RouteInfo routeInfo, RouteGroup routeGroup);

        public abstract void onRouteVolumeChanged(MediaRouter mediaRouter, RouteInfo routeInfo);

        public Callback() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRouter$SimpleCallback.class */
    public static class SimpleCallback extends Callback {
        public SimpleCallback() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteSelected(MediaRouter router, int type, RouteInfo info) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteUnselected(MediaRouter router, int type, RouteInfo info) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteAdded(MediaRouter router, RouteInfo info) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteRemoved(MediaRouter router, RouteInfo info) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteChanged(MediaRouter router, RouteInfo info) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteGrouped(MediaRouter router, RouteInfo info, RouteGroup group, int index) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteUngrouped(MediaRouter router, RouteInfo info, RouteGroup group) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.media.MediaRouter.Callback
        public void onRouteVolumeChanged(MediaRouter router, RouteInfo info) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/MediaRouter$VolumeCallback.class */
    public static abstract class VolumeCallback {
        public abstract void onVolumeUpdateRequest(RouteInfo routeInfo, int i);

        public abstract void onVolumeSetRequest(RouteInfo routeInfo, int i);

        public VolumeCallback() {
            throw new RuntimeException("Stub!");
        }
    }

    MediaRouter() {
        throw new RuntimeException("Stub!");
    }

    public RouteInfo getSelectedRoute(int type) {
        throw new RuntimeException("Stub!");
    }

    public void addCallback(int types, Callback cb) {
        throw new RuntimeException("Stub!");
    }

    public void removeCallback(Callback cb) {
        throw new RuntimeException("Stub!");
    }

    public void selectRoute(int types, RouteInfo route) {
        throw new RuntimeException("Stub!");
    }

    public void addUserRoute(UserRouteInfo info) {
        throw new RuntimeException("Stub!");
    }

    public void removeUserRoute(UserRouteInfo info) {
        throw new RuntimeException("Stub!");
    }

    public void clearUserRoutes() {
        throw new RuntimeException("Stub!");
    }

    public int getCategoryCount() {
        throw new RuntimeException("Stub!");
    }

    public RouteCategory getCategoryAt(int index) {
        throw new RuntimeException("Stub!");
    }

    public int getRouteCount() {
        throw new RuntimeException("Stub!");
    }

    public RouteInfo getRouteAt(int index) {
        throw new RuntimeException("Stub!");
    }

    public UserRouteInfo createUserRoute(RouteCategory category) {
        throw new RuntimeException("Stub!");
    }

    public RouteCategory createRouteCategory(CharSequence name, boolean isGroupable) {
        throw new RuntimeException("Stub!");
    }

    public RouteCategory createRouteCategory(int nameResId, boolean isGroupable) {
        throw new RuntimeException("Stub!");
    }
}
