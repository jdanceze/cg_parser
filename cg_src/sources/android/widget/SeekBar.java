package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SeekBar.class */
public class SeekBar extends AbsSeekBar {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SeekBar$OnSeekBarChangeListener.class */
    public interface OnSeekBarChangeListener {
        void onProgressChanged(SeekBar seekBar, int i, boolean z);

        void onStartTrackingTouch(SeekBar seekBar);

        void onStopTrackingTouch(SeekBar seekBar);
    }

    public SeekBar(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public SeekBar(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public SeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
