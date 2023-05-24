package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RemoteViews;
@RemoteViews.RemoteView
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/LinearLayout.class */
public class LinearLayout extends ViewGroup {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_END = 4;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/LinearLayout$LayoutParams.class */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        @ViewDebug.ExportedProperty(category = "layout")
        public float weight;
        @ViewDebug.ExportedProperty(category = "layout", mapping = {@ViewDebug.IntToString(from = -1, to = "NONE"), @ViewDebug.IntToString(from = 0, to = "NONE"), @ViewDebug.IntToString(from = 48, to = "TOP"), @ViewDebug.IntToString(from = 80, to = "BOTTOM"), @ViewDebug.IntToString(from = 3, to = "LEFT"), @ViewDebug.IntToString(from = 5, to = "RIGHT"), @ViewDebug.IntToString(from = Gravity.START, to = "START"), @ViewDebug.IntToString(from = Gravity.END, to = "END"), @ViewDebug.IntToString(from = 16, to = "CENTER_VERTICAL"), @ViewDebug.IntToString(from = 112, to = "FILL_VERTICAL"), @ViewDebug.IntToString(from = 1, to = "CENTER_HORIZONTAL"), @ViewDebug.IntToString(from = 7, to = "FILL_HORIZONTAL"), @ViewDebug.IntToString(from = 17, to = "CENTER"), @ViewDebug.IntToString(from = 119, to = "FILL")})
        public int gravity;

        public LayoutParams(Context c, AttributeSet attrs) {
            super((ViewGroup.LayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(int width, int height) {
            super((ViewGroup.LayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(int width, int height, float weight) {
            super((ViewGroup.LayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super((ViewGroup.LayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super((ViewGroup.LayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public String debug(String output) {
            throw new RuntimeException("Stub!");
        }
    }

    public LinearLayout(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public LinearLayout(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public LinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setShowDividers(int showDividers) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        throw new RuntimeException("Stub!");
    }

    public int getShowDividers() {
        throw new RuntimeException("Stub!");
    }

    public Drawable getDividerDrawable() {
        throw new RuntimeException("Stub!");
    }

    public void setDividerDrawable(Drawable divider) {
        throw new RuntimeException("Stub!");
    }

    public void setDividerPadding(int padding) {
        throw new RuntimeException("Stub!");
    }

    public int getDividerPadding() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    public boolean isBaselineAligned() {
        throw new RuntimeException("Stub!");
    }

    public void setBaselineAligned(boolean baselineAligned) {
        throw new RuntimeException("Stub!");
    }

    public boolean isMeasureWithLargestChildEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setMeasureWithLargestChildEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public int getBaseline() {
        throw new RuntimeException("Stub!");
    }

    public int getBaselineAlignedChildIndex() {
        throw new RuntimeException("Stub!");
    }

    public void setBaselineAlignedChildIndex(int i) {
        throw new RuntimeException("Stub!");
    }

    public float getWeightSum() {
        throw new RuntimeException("Stub!");
    }

    public void setWeightSum(float weightSum) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        throw new RuntimeException("Stub!");
    }

    public void setOrientation(int orientation) {
        throw new RuntimeException("Stub!");
    }

    public int getOrientation() {
        throw new RuntimeException("Stub!");
    }

    public void setGravity(int gravity) {
        throw new RuntimeException("Stub!");
    }

    public void setHorizontalGravity(int horizontalGravity) {
        throw new RuntimeException("Stub!");
    }

    public void setVerticalGravity(int verticalGravity) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        throw new RuntimeException("Stub!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
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
}
