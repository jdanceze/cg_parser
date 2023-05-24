package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/TimePicker.class */
public class TimePicker extends FrameLayout {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/TimePicker$OnTimeChangedListener.class */
    public interface OnTimeChangedListener {
        void onTimeChanged(TimePicker timePicker, int i, int i2);
    }

    public TimePicker(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public TimePicker(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean isEnabled() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration newConfig) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable state) {
        throw new RuntimeException("Stub!");
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        throw new RuntimeException("Stub!");
    }

    public Integer getCurrentHour() {
        throw new RuntimeException("Stub!");
    }

    public void setCurrentHour(Integer currentHour) {
        throw new RuntimeException("Stub!");
    }

    public void setIs24HourView(Boolean is24HourView) {
        throw new RuntimeException("Stub!");
    }

    public boolean is24HourView() {
        throw new RuntimeException("Stub!");
    }

    public Integer getCurrentMinute() {
        throw new RuntimeException("Stub!");
    }

    public void setCurrentMinute(Integer currentMinute) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public int getBaseline() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
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
}
