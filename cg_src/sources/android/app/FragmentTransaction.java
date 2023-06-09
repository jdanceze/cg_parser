package android.app;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/FragmentTransaction.class */
public abstract class FragmentTransaction {
    public static final int TRANSIT_ENTER_MASK = 4096;
    public static final int TRANSIT_EXIT_MASK = 8192;
    public static final int TRANSIT_UNSET = -1;
    public static final int TRANSIT_NONE = 0;
    public static final int TRANSIT_FRAGMENT_OPEN = 4097;
    public static final int TRANSIT_FRAGMENT_CLOSE = 8194;
    public static final int TRANSIT_FRAGMENT_FADE = 4099;

    public abstract FragmentTransaction add(Fragment fragment, String str);

    public abstract FragmentTransaction add(int i, Fragment fragment);

    public abstract FragmentTransaction add(int i, Fragment fragment, String str);

    public abstract FragmentTransaction replace(int i, Fragment fragment);

    public abstract FragmentTransaction replace(int i, Fragment fragment, String str);

    public abstract FragmentTransaction remove(Fragment fragment);

    public abstract FragmentTransaction hide(Fragment fragment);

    public abstract FragmentTransaction show(Fragment fragment);

    public abstract FragmentTransaction detach(Fragment fragment);

    public abstract FragmentTransaction attach(Fragment fragment);

    public abstract boolean isEmpty();

    public abstract FragmentTransaction setCustomAnimations(int i, int i2);

    public abstract FragmentTransaction setCustomAnimations(int i, int i2, int i3, int i4);

    public abstract FragmentTransaction setTransition(int i);

    public abstract FragmentTransaction setTransitionStyle(int i);

    public abstract FragmentTransaction addToBackStack(String str);

    public abstract boolean isAddToBackStackAllowed();

    public abstract FragmentTransaction disallowAddToBackStack();

    public abstract FragmentTransaction setBreadCrumbTitle(int i);

    public abstract FragmentTransaction setBreadCrumbTitle(CharSequence charSequence);

    public abstract FragmentTransaction setBreadCrumbShortTitle(int i);

    public abstract FragmentTransaction setBreadCrumbShortTitle(CharSequence charSequence);

    public abstract int commit();

    public abstract int commitAllowingStateLoss();

    public FragmentTransaction() {
        throw new RuntimeException("Stub!");
    }
}
