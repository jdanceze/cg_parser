package android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RemoteViews;
@RemoteViews.RemoteView
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewStub.class */
public final class ViewStub extends View {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewStub$OnInflateListener.class */
    public interface OnInflateListener {
        void onInflate(ViewStub viewStub, View view);
    }

    public ViewStub(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public ViewStub(Context context, int layoutResource) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public ViewStub(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public ViewStub(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public int getInflatedId() {
        throw new RuntimeException("Stub!");
    }

    public void setInflatedId(int inflatedId) {
        throw new RuntimeException("Stub!");
    }

    public int getLayoutResource() {
        throw new RuntimeException("Stub!");
    }

    public void setLayoutResource(int layoutResource) {
        throw new RuntimeException("Stub!");
    }

    public void setLayoutInflater(LayoutInflater inflater) {
        throw new RuntimeException("Stub!");
    }

    public LayoutInflater getLayoutInflater() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void dispatchDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        throw new RuntimeException("Stub!");
    }

    public View inflate() {
        throw new RuntimeException("Stub!");
    }

    public void setOnInflateListener(OnInflateListener inflateListener) {
        throw new RuntimeException("Stub!");
    }
}
