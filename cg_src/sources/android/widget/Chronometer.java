package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RemoteViews;
@RemoteViews.RemoteView
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/Chronometer.class */
public class Chronometer extends TextView {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/Chronometer$OnChronometerTickListener.class */
    public interface OnChronometerTickListener {
        void onChronometerTick(Chronometer chronometer);
    }

    public Chronometer(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public Chronometer(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public Chronometer(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setBase(long base) {
        throw new RuntimeException("Stub!");
    }

    public long getBase() {
        throw new RuntimeException("Stub!");
    }

    public void setFormat(String format) {
        throw new RuntimeException("Stub!");
    }

    public String getFormat() {
        throw new RuntimeException("Stub!");
    }

    public void setOnChronometerTickListener(OnChronometerTickListener listener) {
        throw new RuntimeException("Stub!");
    }

    public OnChronometerTickListener getOnChronometerTickListener() {
        throw new RuntimeException("Stub!");
    }

    public void start() {
        throw new RuntimeException("Stub!");
    }

    public void stop() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int visibility) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
