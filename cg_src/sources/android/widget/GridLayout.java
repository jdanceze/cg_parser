package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RemoteViews;
@RemoteViews.RemoteView
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/GridLayout.class */
public class GridLayout extends ViewGroup {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int UNDEFINED = Integer.MIN_VALUE;
    public static final int ALIGN_BOUNDS = 0;
    public static final int ALIGN_MARGINS = 1;
    public static final Alignment TOP = null;
    public static final Alignment BOTTOM = null;
    public static final Alignment START = null;
    public static final Alignment END = null;
    public static final Alignment LEFT = null;
    public static final Alignment RIGHT = null;
    public static final Alignment CENTER = null;
    public static final Alignment BASELINE = null;
    public static final Alignment FILL = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/GridLayout$LayoutParams.class */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public Spec rowSpec;
        public Spec columnSpec;

        public LayoutParams(Spec rowSpec, Spec columnSpec) {
            super((ViewGroup.LayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams() {
            super((ViewGroup.LayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(ViewGroup.LayoutParams params) {
            super((ViewGroup.LayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(ViewGroup.MarginLayoutParams params) {
            super((ViewGroup.LayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(LayoutParams that) {
            super((ViewGroup.LayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super((ViewGroup.LayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public void setGravity(int gravity) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.ViewGroup.LayoutParams
        protected void setBaseAttributes(TypedArray attributes, int widthAttr, int heightAttr) {
            throw new RuntimeException("Stub!");
        }

        public boolean equals(Object o) {
            throw new RuntimeException("Stub!");
        }

        public int hashCode() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/GridLayout$Spec.class */
    public static class Spec {
        Spec() {
            throw new RuntimeException("Stub!");
        }

        public boolean equals(Object that) {
            throw new RuntimeException("Stub!");
        }

        public int hashCode() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/GridLayout$Alignment.class */
    public static abstract class Alignment {
        Alignment() {
            throw new RuntimeException("Stub!");
        }
    }

    public GridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public GridLayout(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public GridLayout(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public int getOrientation() {
        throw new RuntimeException("Stub!");
    }

    public void setOrientation(int orientation) {
        throw new RuntimeException("Stub!");
    }

    public int getRowCount() {
        throw new RuntimeException("Stub!");
    }

    public void setRowCount(int rowCount) {
        throw new RuntimeException("Stub!");
    }

    public int getColumnCount() {
        throw new RuntimeException("Stub!");
    }

    public void setColumnCount(int columnCount) {
        throw new RuntimeException("Stub!");
    }

    public boolean getUseDefaultMargins() {
        throw new RuntimeException("Stub!");
    }

    public void setUseDefaultMargins(boolean useDefaultMargins) {
        throw new RuntimeException("Stub!");
    }

    public int getAlignmentMode() {
        throw new RuntimeException("Stub!");
    }

    public void setAlignmentMode(int alignmentMode) {
        throw new RuntimeException("Stub!");
    }

    public boolean isRowOrderPreserved() {
        throw new RuntimeException("Stub!");
    }

    public void setRowOrderPreserved(boolean rowOrderPreserved) {
        throw new RuntimeException("Stub!");
    }

    public boolean isColumnOrderPreserved() {
        throw new RuntimeException("Stub!");
    }

    public void setColumnOrderPreserved(boolean columnOrderPreserved) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        throw new RuntimeException("Stub!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onMeasure(int widthSpec, int heightSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
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

    public static Spec spec(int start, int size, Alignment alignment) {
        throw new RuntimeException("Stub!");
    }

    public static Spec spec(int start, Alignment alignment) {
        throw new RuntimeException("Stub!");
    }

    public static Spec spec(int start, int size) {
        throw new RuntimeException("Stub!");
    }

    public static Spec spec(int start) {
        throw new RuntimeException("Stub!");
    }
}
