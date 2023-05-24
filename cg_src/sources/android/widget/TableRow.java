package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/TableRow.class */
public class TableRow extends LinearLayout {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/TableRow$LayoutParams.class */
    public static class LayoutParams extends LinearLayout.LayoutParams {
        @ViewDebug.ExportedProperty(category = "layout")
        public int column;
        @ViewDebug.ExportedProperty(category = "layout")
        public int span;

        public LayoutParams(Context c, AttributeSet attrs) {
            super((ViewGroup.MarginLayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(int w, int h) {
            super((ViewGroup.MarginLayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(int w, int h, float initWeight) {
            super((ViewGroup.MarginLayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams() {
            super((ViewGroup.MarginLayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(int column) {
            super((ViewGroup.MarginLayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super((ViewGroup.MarginLayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super((ViewGroup.MarginLayoutParams) null);
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.ViewGroup.LayoutParams
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            throw new RuntimeException("Stub!");
        }
    }

    public TableRow(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public TableRow(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener listener) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        throw new RuntimeException("Stub!");
    }

    public View getVirtualChildAt(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getVirtualChildCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public LinearLayout.LayoutParams generateDefaultLayoutParams() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        throw new RuntimeException("Stub!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public LinearLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
