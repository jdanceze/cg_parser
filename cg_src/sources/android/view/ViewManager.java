package android.view;

import android.view.ViewGroup;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ViewManager.class */
public interface ViewManager {
    void addView(View view, ViewGroup.LayoutParams layoutParams);

    void updateViewLayout(View view, ViewGroup.LayoutParams layoutParams);

    void removeView(View view);
}
