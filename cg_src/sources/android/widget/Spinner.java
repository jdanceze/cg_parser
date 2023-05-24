package android.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/Spinner.class */
public class Spinner extends AbsSpinner implements DialogInterface.OnClickListener {
    public static final int MODE_DIALOG = 0;
    public static final int MODE_DROPDOWN = 1;

    public Spinner(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public Spinner(Context context, int mode) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public Spinner(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public Spinner(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public Spinner(Context context, AttributeSet attrs, int defStyle, int mode) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setPopupBackgroundDrawable(Drawable background) {
        throw new RuntimeException("Stub!");
    }

    public void setPopupBackgroundResource(int resId) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getPopupBackground() {
        throw new RuntimeException("Stub!");
    }

    public void setDropDownVerticalOffset(int pixels) {
        throw new RuntimeException("Stub!");
    }

    public int getDropDownVerticalOffset() {
        throw new RuntimeException("Stub!");
    }

    public void setDropDownHorizontalOffset(int pixels) {
        throw new RuntimeException("Stub!");
    }

    public int getDropDownHorizontalOffset() {
        throw new RuntimeException("Stub!");
    }

    public void setDropDownWidth(int pixels) {
        throw new RuntimeException("Stub!");
    }

    public int getDropDownWidth() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public void setGravity(int gravity) {
        throw new RuntimeException("Stub!");
    }

    public int getGravity() {
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.widget.AbsSpinner, android.widget.AdapterView
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public int getBaseline() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView, android.view.View
    protected void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsSpinner, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean performClick() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsSpinner, android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AbsSpinner, android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }

    public void setPrompt(CharSequence prompt) {
        throw new RuntimeException("Stub!");
    }

    public void setPromptId(int promptId) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getPrompt() {
        throw new RuntimeException("Stub!");
    }
}
