package android.view;

import android.content.Context;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ScaleGestureDetector.class */
public class ScaleGestureDetector {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ScaleGestureDetector$OnScaleGestureListener.class */
    public interface OnScaleGestureListener {
        boolean onScale(ScaleGestureDetector scaleGestureDetector);

        boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector);

        void onScaleEnd(ScaleGestureDetector scaleGestureDetector);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ScaleGestureDetector$SimpleOnScaleGestureListener.class */
    public static class SimpleOnScaleGestureListener implements OnScaleGestureListener {
        public SimpleOnScaleGestureListener() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector detector) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector detector) {
            throw new RuntimeException("Stub!");
        }
    }

    public ScaleGestureDetector(Context context, OnScaleGestureListener listener) {
        throw new RuntimeException("Stub!");
    }

    public boolean onTouchEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean isInProgress() {
        throw new RuntimeException("Stub!");
    }

    public float getFocusX() {
        throw new RuntimeException("Stub!");
    }

    public float getFocusY() {
        throw new RuntimeException("Stub!");
    }

    public float getCurrentSpan() {
        throw new RuntimeException("Stub!");
    }

    public float getCurrentSpanX() {
        throw new RuntimeException("Stub!");
    }

    public float getCurrentSpanY() {
        throw new RuntimeException("Stub!");
    }

    public float getPreviousSpan() {
        throw new RuntimeException("Stub!");
    }

    public float getPreviousSpanX() {
        throw new RuntimeException("Stub!");
    }

    public float getPreviousSpanY() {
        throw new RuntimeException("Stub!");
    }

    public float getScaleFactor() {
        throw new RuntimeException("Stub!");
    }

    public long getTimeDelta() {
        throw new RuntimeException("Stub!");
    }

    public long getEventTime() {
        throw new RuntimeException("Stub!");
    }
}
