package android.app;

import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Activity.class */
public class Activity extends ContextThemeWrapper implements LayoutInflater.Factory2, Window.Callback, KeyEvent.Callback, View.OnCreateContextMenuListener, ComponentCallbacks2 {
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;
    public static final int RESULT_FIRST_USER = 1;
    protected static final int[] FOCUSED_STATE_SET = null;
    public static final int DEFAULT_KEYS_DISABLE = 0;
    public static final int DEFAULT_KEYS_DIALER = 1;
    public static final int DEFAULT_KEYS_SHORTCUT = 2;
    public static final int DEFAULT_KEYS_SEARCH_LOCAL = 3;
    public static final int DEFAULT_KEYS_SEARCH_GLOBAL = 4;

    public Activity() {
        throw new RuntimeException("Stub!");
    }

    public Intent getIntent() {
        throw new RuntimeException("Stub!");
    }

    public void setIntent(Intent newIntent) {
        throw new RuntimeException("Stub!");
    }

    public final Application getApplication() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isChild() {
        throw new RuntimeException("Stub!");
    }

    public final Activity getParent() {
        throw new RuntimeException("Stub!");
    }

    public WindowManager getWindowManager() {
        throw new RuntimeException("Stub!");
    }

    public Window getWindow() {
        throw new RuntimeException("Stub!");
    }

    public LoaderManager getLoaderManager() {
        throw new RuntimeException("Stub!");
    }

    public View getCurrentFocus() {
        throw new RuntimeException("Stub!");
    }

    protected void onCreate(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    protected void onStart() {
        throw new RuntimeException("Stub!");
    }

    protected void onRestart() {
        throw new RuntimeException("Stub!");
    }

    protected void onResume() {
        throw new RuntimeException("Stub!");
    }

    protected void onPostResume() {
        throw new RuntimeException("Stub!");
    }

    protected void onNewIntent(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    protected void onSaveInstanceState(Bundle outState) {
        throw new RuntimeException("Stub!");
    }

    protected void onPause() {
        throw new RuntimeException("Stub!");
    }

    protected void onUserLeaveHint() {
        throw new RuntimeException("Stub!");
    }

    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence onCreateDescription() {
        throw new RuntimeException("Stub!");
    }

    protected void onStop() {
        throw new RuntimeException("Stub!");
    }

    protected void onDestroy() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        throw new RuntimeException("Stub!");
    }

    public int getChangingConfigurations() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public Object getLastNonConfigurationInstance() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public Object onRetainNonConfigurationInstance() {
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

    public FragmentManager getFragmentManager() {
        throw new RuntimeException("Stub!");
    }

    public void onAttachFragment(Fragment fragment) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public final Cursor managedQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void startManagingCursor(Cursor c) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void stopManagingCursor(Cursor c) {
        throw new RuntimeException("Stub!");
    }

    public View findViewById(int id) {
        throw new RuntimeException("Stub!");
    }

    public ActionBar getActionBar() {
        throw new RuntimeException("Stub!");
    }

    public void setContentView(int layoutResID) {
        throw new RuntimeException("Stub!");
    }

    public void setContentView(View view) {
        throw new RuntimeException("Stub!");
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    public void setFinishOnTouchOutside(boolean finish) {
        throw new RuntimeException("Stub!");
    }

    public final void setDefaultKeyMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.KeyEvent.Callback
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.KeyEvent.Callback
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public void onBackPressed() {
        throw new RuntimeException("Stub!");
    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean onTouchEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean onTrackballEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    public void onUserInteraction() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onContentChanged() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onAttachedToWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasWindowFocus() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public View onCreatePanelView(int featureId) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean onMenuOpened(int featureId, Menu menu) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onPanelClosed(int featureId, Menu menu) {
        throw new RuntimeException("Stub!");
    }

    public void invalidateOptionsMenu() {
        throw new RuntimeException("Stub!");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        throw new RuntimeException("Stub!");
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        throw new RuntimeException("Stub!");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        throw new RuntimeException("Stub!");
    }

    public boolean onNavigateUp() {
        throw new RuntimeException("Stub!");
    }

    public boolean onNavigateUpFromChild(Activity child) {
        throw new RuntimeException("Stub!");
    }

    public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
        throw new RuntimeException("Stub!");
    }

    public void onPrepareNavigateUpTaskStack(TaskStackBuilder builder) {
        throw new RuntimeException("Stub!");
    }

    public void onOptionsMenuClosed(Menu menu) {
        throw new RuntimeException("Stub!");
    }

    public void openOptionsMenu() {
        throw new RuntimeException("Stub!");
    }

    public void closeOptionsMenu() {
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

    public void openContextMenu(View view) {
        throw new RuntimeException("Stub!");
    }

    public void closeContextMenu() {
        throw new RuntimeException("Stub!");
    }

    public boolean onContextItemSelected(MenuItem item) {
        throw new RuntimeException("Stub!");
    }

    public void onContextMenuClosed(Menu menu) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    protected Dialog onCreateDialog(int id) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    protected Dialog onCreateDialog(int id, Bundle args) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    protected void onPrepareDialog(int id, Dialog dialog) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public final void showDialog(int id) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public final boolean showDialog(int id, Bundle args) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public final void dismissDialog(int id) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public final void removeDialog(int id) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public boolean onSearchRequested() {
        throw new RuntimeException("Stub!");
    }

    public void startSearch(String initialQuery, boolean selectInitialQuery, Bundle appSearchData, boolean globalSearch) {
        throw new RuntimeException("Stub!");
    }

    public void triggerSearch(String query, Bundle appSearchData) {
        throw new RuntimeException("Stub!");
    }

    public void takeKeyEvents(boolean get) {
        throw new RuntimeException("Stub!");
    }

    public final boolean requestWindowFeature(int featureId) {
        throw new RuntimeException("Stub!");
    }

    public final void setFeatureDrawableResource(int featureId, int resId) {
        throw new RuntimeException("Stub!");
    }

    public final void setFeatureDrawableUri(int featureId, Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public final void setFeatureDrawable(int featureId, Drawable drawable) {
        throw new RuntimeException("Stub!");
    }

    public final void setFeatureDrawableAlpha(int featureId, int alpha) {
        throw new RuntimeException("Stub!");
    }

    public LayoutInflater getLayoutInflater() {
        throw new RuntimeException("Stub!");
    }

    public MenuInflater getMenuInflater() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ContextThemeWrapper
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        throw new RuntimeException("Stub!");
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        throw new RuntimeException("Stub!");
    }

    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {
        throw new RuntimeException("Stub!");
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void startActivity(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void startActivity(Intent intent, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void startActivities(Intent[] intents) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void startActivities(Intent[] intents, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        throw new RuntimeException("Stub!");
    }

    public boolean startActivityIfNeeded(Intent intent, int requestCode) {
        throw new RuntimeException("Stub!");
    }

    public boolean startActivityIfNeeded(Intent intent, int requestCode, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    public boolean startNextMatchingActivity(Intent intent) {
        throw new RuntimeException("Stub!");
    }

    public boolean startNextMatchingActivity(Intent intent, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    public void startActivityFromChild(Activity child, Intent intent, int requestCode) {
        throw new RuntimeException("Stub!");
    }

    public void startActivityFromChild(Activity child, Intent intent, int requestCode, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        throw new RuntimeException("Stub!");
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    public void startIntentSenderFromChild(Activity child, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {
        throw new RuntimeException("Stub!");
    }

    public void startIntentSenderFromChild(Activity child, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        throw new RuntimeException("Stub!");
    }

    public void overridePendingTransition(int enterAnim, int exitAnim) {
        throw new RuntimeException("Stub!");
    }

    public final void setResult(int resultCode) {
        throw new RuntimeException("Stub!");
    }

    public final void setResult(int resultCode, Intent data) {
        throw new RuntimeException("Stub!");
    }

    public String getCallingPackage() {
        throw new RuntimeException("Stub!");
    }

    public ComponentName getCallingActivity() {
        throw new RuntimeException("Stub!");
    }

    public void setVisible(boolean visible) {
        throw new RuntimeException("Stub!");
    }

    public boolean isFinishing() {
        throw new RuntimeException("Stub!");
    }

    public boolean isChangingConfigurations() {
        throw new RuntimeException("Stub!");
    }

    public void recreate() {
        throw new RuntimeException("Stub!");
    }

    public void finish() {
        throw new RuntimeException("Stub!");
    }

    public void finishAffinity() {
        throw new RuntimeException("Stub!");
    }

    public void finishFromChild(Activity child) {
        throw new RuntimeException("Stub!");
    }

    public void finishActivity(int requestCode) {
        throw new RuntimeException("Stub!");
    }

    public void finishActivityFromChild(Activity child, int requestCode) {
        throw new RuntimeException("Stub!");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        throw new RuntimeException("Stub!");
    }

    public PendingIntent createPendingResult(int requestCode, Intent data, int flags) {
        throw new RuntimeException("Stub!");
    }

    public void setRequestedOrientation(int requestedOrientation) {
        throw new RuntimeException("Stub!");
    }

    public int getRequestedOrientation() {
        throw new RuntimeException("Stub!");
    }

    public int getTaskId() {
        throw new RuntimeException("Stub!");
    }

    public boolean isTaskRoot() {
        throw new RuntimeException("Stub!");
    }

    public boolean moveTaskToBack(boolean nonRoot) {
        throw new RuntimeException("Stub!");
    }

    public String getLocalClassName() {
        throw new RuntimeException("Stub!");
    }

    public ComponentName getComponentName() {
        throw new RuntimeException("Stub!");
    }

    public SharedPreferences getPreferences(int mode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public Object getSystemService(String name) {
        throw new RuntimeException("Stub!");
    }

    public void setTitle(CharSequence title) {
        throw new RuntimeException("Stub!");
    }

    public void setTitle(int titleId) {
        throw new RuntimeException("Stub!");
    }

    public void setTitleColor(int textColor) {
        throw new RuntimeException("Stub!");
    }

    public final CharSequence getTitle() {
        throw new RuntimeException("Stub!");
    }

    public final int getTitleColor() {
        throw new RuntimeException("Stub!");
    }

    protected void onTitleChanged(CharSequence title, int color) {
        throw new RuntimeException("Stub!");
    }

    protected void onChildTitleChanged(Activity childActivity, CharSequence title) {
        throw new RuntimeException("Stub!");
    }

    public final void setProgressBarVisibility(boolean visible) {
        throw new RuntimeException("Stub!");
    }

    public final void setProgressBarIndeterminateVisibility(boolean visible) {
        throw new RuntimeException("Stub!");
    }

    public final void setProgressBarIndeterminate(boolean indeterminate) {
        throw new RuntimeException("Stub!");
    }

    public final void setProgress(int progress) {
        throw new RuntimeException("Stub!");
    }

    public final void setSecondaryProgress(int secondaryProgress) {
        throw new RuntimeException("Stub!");
    }

    public final void setVolumeControlStream(int streamType) {
        throw new RuntimeException("Stub!");
    }

    public final int getVolumeControlStream() {
        throw new RuntimeException("Stub!");
    }

    public final void runOnUiThread(Runnable action) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.LayoutInflater.Factory
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.LayoutInflater.Factory2
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        throw new RuntimeException("Stub!");
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onActionModeStarted(ActionMode mode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.Window.Callback
    public void onActionModeFinished(ActionMode mode) {
        throw new RuntimeException("Stub!");
    }

    public boolean shouldUpRecreateTask(Intent targetIntent) {
        throw new RuntimeException("Stub!");
    }

    public boolean navigateUpTo(Intent upIntent) {
        throw new RuntimeException("Stub!");
    }

    public boolean navigateUpToFromChild(Activity child, Intent upIntent) {
        throw new RuntimeException("Stub!");
    }

    public Intent getParentActivityIntent() {
        throw new RuntimeException("Stub!");
    }
}
