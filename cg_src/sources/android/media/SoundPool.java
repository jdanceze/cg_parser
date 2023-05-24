package android.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import java.io.FileDescriptor;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/SoundPool.class */
public class SoundPool {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/SoundPool$OnLoadCompleteListener.class */
    public interface OnLoadCompleteListener {
        void onLoadComplete(SoundPool soundPool, int i, int i2);
    }

    public final native boolean unload(int i);

    public final native int play(int i, float f, float f2, int i2, int i3, float f3);

    public final native void pause(int i);

    public final native void resume(int i);

    public final native void autoPause();

    public final native void autoResume();

    public final native void stop(int i);

    public final native void setVolume(int i, float f, float f2);

    public final native void setPriority(int i, int i2);

    public final native void setLoop(int i, int i2);

    public final native void setRate(int i, float f);

    public final native void release();

    public SoundPool(int maxStreams, int streamType, int srcQuality) {
        throw new RuntimeException("Stub!");
    }

    public int load(String path, int priority) {
        throw new RuntimeException("Stub!");
    }

    public int load(Context context, int resId, int priority) {
        throw new RuntimeException("Stub!");
    }

    public int load(AssetFileDescriptor afd, int priority) {
        throw new RuntimeException("Stub!");
    }

    public int load(FileDescriptor fd, long offset, long length, int priority) {
        throw new RuntimeException("Stub!");
    }

    public void setOnLoadCompleteListener(OnLoadCompleteListener listener) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() {
        throw new RuntimeException("Stub!");
    }
}
