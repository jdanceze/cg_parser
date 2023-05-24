package android.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.MediaController;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/VideoView.class */
public class VideoView extends SurfaceView implements MediaController.MediaPlayerControl {
    public VideoView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public VideoView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }

    public int resolveAdjustedSize(int desiredSize, int measureSpec) {
        throw new RuntimeException("Stub!");
    }

    public void setVideoPath(String path) {
        throw new RuntimeException("Stub!");
    }

    public void setVideoURI(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public void stopPlayback() {
        throw new RuntimeException("Stub!");
    }

    public void setMediaController(MediaController controller) {
        throw new RuntimeException("Stub!");
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
        throw new RuntimeException("Stub!");
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener l) {
        throw new RuntimeException("Stub!");
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener l) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void start() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void pause() {
        throw new RuntimeException("Stub!");
    }

    public void suspend() {
        throw new RuntimeException("Stub!");
    }

    public void resume() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getDuration() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getCurrentPosition() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void seekTo(int msec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean isPlaying() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getBufferPercentage() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canPause() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canSeekBackward() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canSeekForward() {
        throw new RuntimeException("Stub!");
    }
}
