package android.app;

import android.animation.Animator;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Fragment.class */
public class Fragment implements ComponentCallbacks2, View.OnCreateContextMenuListener {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Fragment$SavedState.class */
    public static class SavedState implements Parcelable {
        public static final Parcelable.ClassLoaderCreator<SavedState> CREATOR = null;

        SavedState() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Fragment$InstantiationException.class */
    public static class InstantiationException extends AndroidRuntimeException {
        public InstantiationException(String msg, Exception cause) {
            throw new RuntimeException("Stub!");
        }
    }

    public Fragment() {
        throw new RuntimeException("Stub!");
    }

    public static Fragment instantiate(Context context, String fname) {
        throw new RuntimeException("Stub!");
    }

    public static Fragment instantiate(Context context, String fname, Bundle args) {
        throw new RuntimeException("Stub!");
    }

    public final boolean equals(Object o) {
        throw new RuntimeException("Stub!");
    }

    public final int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public final int getId() {
        throw new RuntimeException("Stub!");
    }

    public final String getTag() {
        throw new RuntimeException("Stub!");
    }

    public void setArguments(Bundle args) {
        throw new RuntimeException("Stub!");
    }

    public final Bundle getArguments() {
        throw new RuntimeException("Stub!");
    }

    public void setInitialSavedState(SavedState state) {
        throw new RuntimeException("Stub!");
    }

    public void setTargetFragment(Fragment fragment, int requestCode) {
        throw new RuntimeException("Stub!");
    }

    public final Fragment getTargetFragment() {
        throw new RuntimeException("Stub!");
    }

    public final int getTargetRequestCode() {
        throw new RuntimeException("Stub!");
    }

    public final Activity getActivity() {
        throw new RuntimeException("Stub!");
    }

    public final Resources getResources() {
        throw new RuntimeException("Stub!");
    }

    public final CharSequence getText(int resId) {
        throw new RuntimeException("Stub!");
    }

    public final String getString(int resId) {
        throw new RuntimeException("Stub!");
    }

    public final String getString(int resId, Object... formatArgs) {
        throw new RuntimeException("Stub!");
    }

    public final FragmentManager getFragmentManager() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isAdded() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isDetached() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isRemoving() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isInLayout() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isResumed() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isVisible() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isHidden() {
        throw new RuntimeException("Stub!");
    }

    public void onHiddenChanged(boolean hidden) {
        throw new RuntimeException("Stub!");
    }

    public void setRetainInstance(boolean retain) {
        throw new RuntimeException("Stub!");
    }

    public final boolean getRetainInstance() {
        throw new RuntimeException("Stub!");
    }

    public void setHasOptionsMenu(boolean hasMenu) {
        throw new RuntimeException("Stub!");
    }

    public void setMenuVisibility(boolean menuVisible) {
        throw new RuntimeException("Stub!");
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        throw new RuntimeException("Stub!");
    }

    public boolean getUserVisibleHint() {
        throw new RuntimeException("Stub!");
    }

    public LoaderManager getLoaderManager() {
        throw new RuntimeException("Stub!");
    }

    public void startActivity(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    public void startActivity(Intent intent, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        throw new RuntimeException("Stub!");
    }

    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void onInflate(AttributeSet attrs, Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    public void onAttach(Activity activity) {
        throw new RuntimeException("Stub!");
    }

    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        throw new RuntimeException("Stub!");
    }

    public void onCreate(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    public View getView() {
        throw new RuntimeException("Stub!");
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    public void onStart() {
        throw new RuntimeException("Stub!");
    }

    public void onResume() {
        throw new RuntimeException("Stub!");
    }

    public void onSaveInstanceState(Bundle outState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        throw new RuntimeException("Stub!");
    }

    public void onPause() {
        throw new RuntimeException("Stub!");
    }

    public void onStop() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ComponentCallbacks2
    public void onTrimMemory(int level) {
        throw new RuntimeException("Stub!");
    }

    public void onDestroyView() {
        throw new RuntimeException("Stub!");
    }

    public void onDestroy() {
        throw new RuntimeException("Stub!");
    }

    public void onDetach() {
        throw new RuntimeException("Stub!");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        throw new RuntimeException("Stub!");
    }

    public void onPrepareOptionsMenu(Menu menu) {
        throw new RuntimeException("Stub!");
    }

    public void onDestroyOptionsMenu() {
        throw new RuntimeException("Stub!");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        throw new RuntimeException("Stub!");
    }

    public void onOptionsMenuClosed(Menu menu) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        throw new RuntimeException("Stub!");
    }

    public void registerForContextMenu(View view) {
        throw new RuntimeException("Stub!");
    }

    public void unregisterForContextMenu(View view) {
        throw new RuntimeException("Stub!");
    }

    public boolean onContextItemSelected(MenuItem item) {
        throw new RuntimeException("Stub!");
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        throw new RuntimeException("Stub!");
    }
}
