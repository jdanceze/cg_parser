package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/RatingBar.class */
public class RatingBar extends AbsSeekBar {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/RatingBar$OnRatingBarChangeListener.class */
    public interface OnRatingBarChangeListener {
        void onRatingChanged(RatingBar ratingBar, float f, boolean z);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public RatingBar(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public RatingBar(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setOnRatingBarChangeListener(OnRatingBarChangeListener listener) {
        throw new RuntimeException("Stub!");
    }

    public OnRatingBarChangeListener getOnRatingBarChangeListener() {
        throw new RuntimeException("Stub!");
    }

    public void setIsIndicator(boolean isIndicator) {
        throw new RuntimeException("Stub!");
    }

    public boolean isIndicator() {
        throw new RuntimeException("Stub!");
    }

    public void setNumStars(int numStars) {
        throw new RuntimeException("Stub!");
    }

    public int getNumStars() {
        throw new RuntimeException("Stub!");
    }

    public void setRating(float rating) {
        throw new RuntimeException("Stub!");
    }

    public float getRating() {
        throw new RuntimeException("Stub!");
    }

    public void setStepSize(float stepSize) {
        throw new RuntimeException("Stub!");
    }

    public float getStepSize() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar
    public synchronized void setMax(int max) {
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
