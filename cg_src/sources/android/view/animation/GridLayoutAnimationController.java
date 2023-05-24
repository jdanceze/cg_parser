package android.view.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LayoutAnimationController;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/animation/GridLayoutAnimationController.class */
public class GridLayoutAnimationController extends LayoutAnimationController {
    public static final int DIRECTION_LEFT_TO_RIGHT = 0;
    public static final int DIRECTION_RIGHT_TO_LEFT = 1;
    public static final int DIRECTION_TOP_TO_BOTTOM = 0;
    public static final int DIRECTION_BOTTOM_TO_TOP = 2;
    public static final int DIRECTION_HORIZONTAL_MASK = 1;
    public static final int DIRECTION_VERTICAL_MASK = 2;
    public static final int PRIORITY_NONE = 0;
    public static final int PRIORITY_COLUMN = 1;
    public static final int PRIORITY_ROW = 2;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/animation/GridLayoutAnimationController$AnimationParameters.class */
    public static class AnimationParameters extends LayoutAnimationController.AnimationParameters {
        public int column;
        public int row;
        public int columnsCount;
        public int rowsCount;

        public AnimationParameters() {
            throw new RuntimeException("Stub!");
        }
    }

    public GridLayoutAnimationController(Context context, AttributeSet attrs) {
        super((Animation) null, 0.0f);
        throw new RuntimeException("Stub!");
    }

    public GridLayoutAnimationController(Animation animation) {
        super((Animation) null, 0.0f);
        throw new RuntimeException("Stub!");
    }

    public GridLayoutAnimationController(Animation animation, float columnDelay, float rowDelay) {
        super((Animation) null, 0.0f);
        throw new RuntimeException("Stub!");
    }

    public float getColumnDelay() {
        throw new RuntimeException("Stub!");
    }

    public void setColumnDelay(float columnDelay) {
        throw new RuntimeException("Stub!");
    }

    public float getRowDelay() {
        throw new RuntimeException("Stub!");
    }

    public void setRowDelay(float rowDelay) {
        throw new RuntimeException("Stub!");
    }

    public int getDirection() {
        throw new RuntimeException("Stub!");
    }

    public void setDirection(int direction) {
        throw new RuntimeException("Stub!");
    }

    public int getDirectionPriority() {
        throw new RuntimeException("Stub!");
    }

    public void setDirectionPriority(int directionPriority) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.animation.LayoutAnimationController
    public boolean willOverlap() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.animation.LayoutAnimationController
    protected long getDelayForView(View view) {
        throw new RuntimeException("Stub!");
    }
}
