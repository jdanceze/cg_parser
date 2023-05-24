package android.app;

import android.app.FragmentManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/FragmentBreadCrumbs.class */
public class FragmentBreadCrumbs extends ViewGroup implements FragmentManager.OnBackStackChangedListener {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/FragmentBreadCrumbs$OnBreadCrumbClickListener.class */
    public interface OnBreadCrumbClickListener {
        boolean onBreadCrumbClick(FragmentManager.BackStackEntry backStackEntry, int i);
    }

    public FragmentBreadCrumbs(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public FragmentBreadCrumbs(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public FragmentBreadCrumbs(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setActivity(Activity a) {
        throw new RuntimeException("Stub!");
    }

    public void setMaxVisible(int visibleCrumbs) {
        throw new RuntimeException("Stub!");
    }

    public void setParentTitle(CharSequence title, CharSequence shortTitle, View.OnClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnBreadCrumbClickListener(OnBreadCrumbClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setTitle(CharSequence title, CharSequence shortTitle) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.FragmentManager.OnBackStackChangedListener
    public void onBackStackChanged() {
        throw new RuntimeException("Stub!");
    }
}
