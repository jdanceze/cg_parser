package android.view.inputmethod;

import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.view.View;
import java.util.List;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/inputmethod/InputMethodManager.class */
public final class InputMethodManager {
    public static final int SHOW_IMPLICIT = 1;
    public static final int SHOW_FORCED = 2;
    public static final int RESULT_UNCHANGED_SHOWN = 0;
    public static final int RESULT_UNCHANGED_HIDDEN = 1;
    public static final int RESULT_SHOWN = 2;
    public static final int RESULT_HIDDEN = 3;
    public static final int HIDE_IMPLICIT_ONLY = 1;
    public static final int HIDE_NOT_ALWAYS = 2;

    InputMethodManager() {
        throw new RuntimeException("Stub!");
    }

    public List<InputMethodInfo> getInputMethodList() {
        throw new RuntimeException("Stub!");
    }

    public List<InputMethodInfo> getEnabledInputMethodList() {
        throw new RuntimeException("Stub!");
    }

    public List<InputMethodSubtype> getEnabledInputMethodSubtypeList(InputMethodInfo imi, boolean allowsImplicitlySelectedSubtypes) {
        throw new RuntimeException("Stub!");
    }

    public void showStatusIcon(IBinder imeToken, String packageName, int iconId) {
        throw new RuntimeException("Stub!");
    }

    public void hideStatusIcon(IBinder imeToken) {
        throw new RuntimeException("Stub!");
    }

    public boolean isFullscreenMode() {
        throw new RuntimeException("Stub!");
    }

    public boolean isActive(View view) {
        throw new RuntimeException("Stub!");
    }

    public boolean isActive() {
        throw new RuntimeException("Stub!");
    }

    public boolean isAcceptingText() {
        throw new RuntimeException("Stub!");
    }

    public void displayCompletions(View view, CompletionInfo[] completions) {
        throw new RuntimeException("Stub!");
    }

    public void updateExtractedText(View view, int token, ExtractedText text) {
        throw new RuntimeException("Stub!");
    }

    public boolean showSoftInput(View view, int flags) {
        throw new RuntimeException("Stub!");
    }

    public boolean showSoftInput(View view, int flags, ResultReceiver resultReceiver) {
        throw new RuntimeException("Stub!");
    }

    public boolean hideSoftInputFromWindow(IBinder windowToken, int flags) {
        throw new RuntimeException("Stub!");
    }

    public boolean hideSoftInputFromWindow(IBinder windowToken, int flags, ResultReceiver resultReceiver) {
        throw new RuntimeException("Stub!");
    }

    public void toggleSoftInputFromWindow(IBinder windowToken, int showFlags, int hideFlags) {
        throw new RuntimeException("Stub!");
    }

    public void toggleSoftInput(int showFlags, int hideFlags) {
        throw new RuntimeException("Stub!");
    }

    public void restartInput(View view) {
        throw new RuntimeException("Stub!");
    }

    public void updateSelection(View view, int selStart, int selEnd, int candidatesStart, int candidatesEnd) {
        throw new RuntimeException("Stub!");
    }

    public void viewClicked(View view) {
        throw new RuntimeException("Stub!");
    }

    public boolean isWatchingCursor(View view) {
        throw new RuntimeException("Stub!");
    }

    public void updateCursor(View view, int left, int top, int right, int bottom) {
        throw new RuntimeException("Stub!");
    }

    public void sendAppPrivateCommand(View view, String action, Bundle data) {
        throw new RuntimeException("Stub!");
    }

    public void setInputMethod(IBinder token, String id) {
        throw new RuntimeException("Stub!");
    }

    public void setInputMethodAndSubtype(IBinder token, String id, InputMethodSubtype subtype) {
        throw new RuntimeException("Stub!");
    }

    public void hideSoftInputFromInputMethod(IBinder token, int flags) {
        throw new RuntimeException("Stub!");
    }

    public void showSoftInputFromInputMethod(IBinder token, int flags) {
        throw new RuntimeException("Stub!");
    }

    public void showInputMethodPicker() {
        throw new RuntimeException("Stub!");
    }

    public void showInputMethodAndSubtypeEnabler(String imiId) {
        throw new RuntimeException("Stub!");
    }

    public InputMethodSubtype getCurrentInputMethodSubtype() {
        throw new RuntimeException("Stub!");
    }

    public boolean setCurrentInputMethodSubtype(InputMethodSubtype subtype) {
        throw new RuntimeException("Stub!");
    }

    public Map<InputMethodInfo, List<InputMethodSubtype>> getShortcutInputMethodsAndSubtypes() {
        throw new RuntimeException("Stub!");
    }

    public boolean switchToLastInputMethod(IBinder imeToken) {
        throw new RuntimeException("Stub!");
    }

    public boolean switchToNextInputMethod(IBinder imeToken, boolean onlyCurrentIme) {
        throw new RuntimeException("Stub!");
    }

    public void setAdditionalInputMethodSubtypes(String imiId, InputMethodSubtype[] subtypes) {
        throw new RuntimeException("Stub!");
    }

    public InputMethodSubtype getLastInputMethodSubtype() {
        throw new RuntimeException("Stub!");
    }
}
