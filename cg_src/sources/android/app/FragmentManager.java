package android.app;

import android.app.Fragment;
import android.os.Bundle;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/FragmentManager.class */
public abstract class FragmentManager {
    public static final int POP_BACK_STACK_INCLUSIVE = 1;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/FragmentManager$BackStackEntry.class */
    public interface BackStackEntry {
        int getId();

        String getName();

        int getBreadCrumbTitleRes();

        int getBreadCrumbShortTitleRes();

        CharSequence getBreadCrumbTitle();

        CharSequence getBreadCrumbShortTitle();
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/FragmentManager$OnBackStackChangedListener.class */
    public interface OnBackStackChangedListener {
        void onBackStackChanged();
    }

    public abstract FragmentTransaction beginTransaction();

    public abstract boolean executePendingTransactions();

    public abstract Fragment findFragmentById(int i);

    public abstract Fragment findFragmentByTag(String str);

    public abstract void popBackStack();

    public abstract boolean popBackStackImmediate();

    public abstract void popBackStack(String str, int i);

    public abstract boolean popBackStackImmediate(String str, int i);

    public abstract void popBackStack(int i, int i2);

    public abstract boolean popBackStackImmediate(int i, int i2);

    public abstract int getBackStackEntryCount();

    public abstract BackStackEntry getBackStackEntryAt(int i);

    public abstract void addOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener);

    public abstract void removeOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener);

    public abstract void putFragment(Bundle bundle, String str, Fragment fragment);

    public abstract Fragment getFragment(Bundle bundle, String str);

    public abstract Fragment.SavedState saveFragmentInstanceState(Fragment fragment);

    public abstract void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public FragmentManager() {
        throw new RuntimeException("Stub!");
    }

    public static void enableDebugLogging(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public void invalidateOptionsMenu() {
        throw new RuntimeException("Stub!");
    }
}
