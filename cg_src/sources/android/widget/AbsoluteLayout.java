package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RemoteViews;
@Deprecated
@RemoteViews.RemoteView
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AbsoluteLayout.class */
public class AbsoluteLayout extends ViewGroup {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AbsoluteLayout$LayoutParams.class */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int x;
        public int y;

        public LayoutParams(int width, int height, int x, int y) {
            super(null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(null);
            throw new RuntimeException("Stub!");
        }

        public String debug(String output) {
            throw new RuntimeException("Stub!");
        }
    }

    public AbsoluteLayout(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AbsoluteLayout(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AbsoluteLayout(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
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

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        throw new RuntimeException("Stub!");
    }
}
