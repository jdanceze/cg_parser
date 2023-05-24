package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.ArrayList;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/NumberPicker.class */
public class NumberPicker extends LinearLayout {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/NumberPicker$Formatter.class */
    public interface Formatter {
        String format(int i);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/NumberPicker$OnScrollListener.class */
    public interface OnScrollListener {
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;
        public static final int SCROLL_STATE_FLING = 2;

        void onScrollStateChange(NumberPicker numberPicker, int i);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/NumberPicker$OnValueChangeListener.class */
    public interface OnValueChangeListener {
        void onValueChange(NumberPicker numberPicker, int i, int i2);
    }

    public NumberPicker(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTrackballEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected boolean dispatchHoverEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void computeScroll() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void scrollBy(int x, int y) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public int getSolidColor() {
        throw new RuntimeException("Stub!");
    }

    public void setOnValueChangedListener(OnValueChangeListener onValueChangedListener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        throw new RuntimeException("Stub!");
    }

    public void setFormatter(Formatter formatter) {
        throw new RuntimeException("Stub!");
    }

    public void setValue(int value) {
        throw new RuntimeException("Stub!");
    }

    public boolean getWrapSelectorWheel() {
        throw new RuntimeException("Stub!");
    }

    public void setWrapSelectorWheel(boolean wrapSelectorWheel) {
        throw new RuntimeException("Stub!");
    }

    public void setOnLongPressUpdateInterval(long intervalMillis) {
        throw new RuntimeException("Stub!");
    }

    public int getValue() {
        throw new RuntimeException("Stub!");
    }

    public int getMinValue() {
        throw new RuntimeException("Stub!");
    }

    public void setMinValue(int minValue) {
        throw new RuntimeException("Stub!");
    }

    public int getMaxValue() {
        throw new RuntimeException("Stub!");
    }

    public void setMaxValue(int maxValue) {
        throw new RuntimeException("Stub!");
    }

    public String[] getDisplayedValues() {
        throw new RuntimeException("Stub!");
    }

    public void setDisplayedValues(String[] displayedValues) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected float getTopFadingEdgeStrength() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected float getBottomFadingEdgeStrength() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        throw new RuntimeException("Stub!");
    }
}
