package android.media;

import android.content.res.AssetFileDescriptor;
import android.os.Handler;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/JetPlayer.class */
public class JetPlayer {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/JetPlayer$OnJetEventListener.class */
    public interface OnJetEventListener {
        void onJetEvent(JetPlayer jetPlayer, short s, byte b, byte b2, byte b3, byte b4);

        void onJetUserIdUpdate(JetPlayer jetPlayer, int i, int i2);

        void onJetNumQueuedSegmentUpdate(JetPlayer jetPlayer, int i);

        void onJetPauseUpdate(JetPlayer jetPlayer, int i);
    }

    JetPlayer() {
        throw new RuntimeException("Stub!");
    }

    public static JetPlayer getJetPlayer() {
        throw new RuntimeException("Stub!");
    }

    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() {
        throw new RuntimeException("Stub!");
    }

    public void release() {
        throw new RuntimeException("Stub!");
    }

    public static int getMaxTracks() {
        throw new RuntimeException("Stub!");
    }

    public boolean loadJetFile(String path) {
        throw new RuntimeException("Stub!");
    }

    public boolean loadJetFile(AssetFileDescriptor afd) {
        throw new RuntimeException("Stub!");
    }

    public boolean closeJetFile() {
        throw new RuntimeException("Stub!");
    }

    public boolean play() {
        throw new RuntimeException("Stub!");
    }

    public boolean pause() {
        throw new RuntimeException("Stub!");
    }

    public boolean queueJetSegment(int segmentNum, int libNum, int repeatCount, int transpose, int muteFlags, byte userID) {
        throw new RuntimeException("Stub!");
    }

    public boolean queueJetSegmentMuteArray(int segmentNum, int libNum, int repeatCount, int transpose, boolean[] muteArray, byte userID) {
        throw new RuntimeException("Stub!");
    }

    public boolean setMuteFlags(int muteFlags, boolean sync) {
        throw new RuntimeException("Stub!");
    }

    public boolean setMuteArray(boolean[] muteArray, boolean sync) {
        throw new RuntimeException("Stub!");
    }

    public boolean setMuteFlag(int trackId, boolean muteFlag, boolean sync) {
        throw new RuntimeException("Stub!");
    }

    public boolean triggerClip(int clipId) {
        throw new RuntimeException("Stub!");
    }

    public boolean clearQueue() {
        throw new RuntimeException("Stub!");
    }

    public void setEventListener(OnJetEventListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setEventListener(OnJetEventListener listener, Handler handler) {
        throw new RuntimeException("Stub!");
    }
}
