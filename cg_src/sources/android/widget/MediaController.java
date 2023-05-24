package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/MediaController.class */
public class MediaController extends FrameLayout {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/MediaController$MediaPlayerControl.class */
    public interface MediaPlayerControl {
        void start();

        void pause();

        int getDuration();

        int getCurrentPosition();

        void seekTo(int i);

        boolean isPlaying();

        int getBufferPercentage();

        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();
    }

    public MediaController(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public MediaController(Context context, boolean useFastForward) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public MediaController(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onFinishInflate() {
        throw new RuntimeException("Stub!");
    }

    public void setMediaPlayer(MediaPlayerControl player) {
        throw new RuntimeException("Stub!");
    }

    public void setAnchorView(View view) {
        throw new RuntimeException("Stub!");
    }

    public void show() {
        throw new RuntimeException("Stub!");
    }

    public void show(int timeout) {
        throw new RuntimeException("Stub!");
    }

    public boolean isShowing() {
        throw new RuntimeException("Stub!");
    }

    public void hide() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }

    public void setPrevNextListeners(View.OnClickListener next, View.OnClickListener prev) {
        throw new RuntimeException("Stub!");
    }
}
