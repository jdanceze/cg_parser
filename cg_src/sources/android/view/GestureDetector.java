package android.view;

import android.content.Context;
import android.os.Handler;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/GestureDetector.class */
public class GestureDetector {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/GestureDetector$OnDoubleTapListener.class */
    public interface OnDoubleTapListener {
        boolean onSingleTapConfirmed(MotionEvent motionEvent);

        boolean onDoubleTap(MotionEvent motionEvent);

        boolean onDoubleTapEvent(MotionEvent motionEvent);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/GestureDetector$OnGestureListener.class */
    public interface OnGestureListener {
        boolean onDown(MotionEvent motionEvent);

        void onShowPress(MotionEvent motionEvent);

        boolean onSingleTapUp(MotionEvent motionEvent);

        boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onLongPress(MotionEvent motionEvent);

        boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/GestureDetector$SimpleOnGestureListener.class */
    public static class SimpleOnGestureListener implements OnGestureListener, OnDoubleTapListener {
        public SimpleOnGestureListener() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTapEvent(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }
    }

    @Deprecated
    public GestureDetector(OnGestureListener listener, Handler handler) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public GestureDetector(OnGestureListener listener) {
        throw new RuntimeException("Stub!");
    }

    public GestureDetector(Context context, OnGestureListener listener) {
        throw new RuntimeException("Stub!");
    }

    public GestureDetector(Context context, OnGestureListener listener, Handler handler) {
        throw new RuntimeException("Stub!");
    }

    public GestureDetector(Context context, OnGestureListener listener, Handler handler, boolean ignoreMultitouch) {
        throw new RuntimeException("Stub!");
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        throw new RuntimeException("Stub!");
    }

    public void setIsLongpressEnabled(boolean isLongpressEnabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean isLongpressEnabled() {
        throw new RuntimeException("Stub!");
    }

    public boolean onTouchEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }
}
