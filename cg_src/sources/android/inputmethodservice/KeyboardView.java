package android.inputmethodservice;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/inputmethodservice/KeyboardView.class */
public class KeyboardView extends View implements View.OnClickListener {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/inputmethodservice/KeyboardView$OnKeyboardActionListener.class */
    public interface OnKeyboardActionListener {
        void onPress(int i);

        void onRelease(int i);

        void onKey(int i, int[] iArr);

        void onText(CharSequence charSequence);

        void swipeLeft();

        void swipeRight();

        void swipeDown();

        void swipeUp();
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setOnKeyboardActionListener(OnKeyboardActionListener listener) {
        throw new RuntimeException("Stub!");
    }

    protected OnKeyboardActionListener getOnKeyboardActionListener() {
        throw new RuntimeException("Stub!");
    }

    public void setKeyboard(Keyboard keyboard) {
        throw new RuntimeException("Stub!");
    }

    public Keyboard getKeyboard() {
        throw new RuntimeException("Stub!");
    }

    public boolean setShifted(boolean shifted) {
        throw new RuntimeException("Stub!");
    }

    public boolean isShifted() {
        throw new RuntimeException("Stub!");
    }

    public void setPreviewEnabled(boolean previewEnabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean isPreviewEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setVerticalCorrection(int verticalOffset) {
        throw new RuntimeException("Stub!");
    }

    public void setPopupParent(View v) {
        throw new RuntimeException("Stub!");
    }

    public void setPopupOffset(int x, int y) {
        throw new RuntimeException("Stub!");
    }

    public void setProximityCorrectionEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean isProximityCorrectionEnabled() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    public void invalidateAllKeys() {
        throw new RuntimeException("Stub!");
    }

    public void invalidateKey(int keyIndex) {
        throw new RuntimeException("Stub!");
    }

    protected boolean onLongPress(Keyboard.Key popupKey) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent me) {
        throw new RuntimeException("Stub!");
    }

    protected void swipeRight() {
        throw new RuntimeException("Stub!");
    }

    protected void swipeLeft() {
        throw new RuntimeException("Stub!");
    }

    protected void swipeUp() {
        throw new RuntimeException("Stub!");
    }

    protected void swipeDown() {
        throw new RuntimeException("Stub!");
    }

    public void closing() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }

    public boolean handleBack() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        throw new RuntimeException("Stub!");
    }
}
