package android.widget;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AbsSpinner.class */
public abstract class AbsSpinner extends AdapterView<SpinnerAdapter> {
    public AbsSpinner(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AbsSpinner(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AbsSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        throw new RuntimeException("Stub!");
    }

    public void setSelection(int position, boolean animate) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    public void setSelection(int position) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    public View getSelectedView() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        throw new RuntimeException("Stub!");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.widget.AdapterView
    public SpinnerAdapter getAdapter() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView
    public int getCount() {
        throw new RuntimeException("Stub!");
    }

    public int pointToPosition(int x, int y) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
