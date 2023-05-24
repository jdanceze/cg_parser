package android.inputmethodservice;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.Region;
import android.inputmethodservice.AbstractInputMethodService;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodSubtype;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/inputmethodservice/InputMethodService.class */
public class InputMethodService extends AbstractInputMethodService {
    public static final int BACK_DISPOSITION_DEFAULT = 0;
    public static final int BACK_DISPOSITION_WILL_NOT_DISMISS = 1;
    public static final int BACK_DISPOSITION_WILL_DISMISS = 2;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/inputmethodservice/InputMethodService$InputMethodImpl.class */
    public class InputMethodImpl extends AbstractInputMethodService.AbstractInputMethodImpl {
        public InputMethodImpl() {
            super();
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethod
        public void attachToken(IBinder token) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethod
        public void bindInput(InputBinding binding) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethod
        public void unbindInput() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethod
        public void startInput(InputConnection ic, EditorInfo attribute) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethod
        public void restartInput(InputConnection ic, EditorInfo attribute) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethod
        public void hideSoftInput(int flags, ResultReceiver resultReceiver) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethod
        public void showSoftInput(int flags, ResultReceiver resultReceiver) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethod
        public void changeInputMethodSubtype(InputMethodSubtype subtype) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/inputmethodservice/InputMethodService$InputMethodSessionImpl.class */
    public class InputMethodSessionImpl extends AbstractInputMethodService.AbstractInputMethodSessionImpl {
        public InputMethodSessionImpl() {
            super();
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void finishInput() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void displayCompletions(CompletionInfo[] completions) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void updateExtractedText(int token, ExtractedText text) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void updateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void viewClicked(boolean focusChanged) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void updateCursor(Rect newCursor) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void appPrivateCommand(String action, Bundle data) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void toggleSoftInput(int showFlags, int hideFlags) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/inputmethodservice/InputMethodService$Insets.class */
    public static final class Insets {
        public int contentTopInsets;
        public int visibleTopInsets;
        public final Region touchableRegion;
        public static final int TOUCHABLE_INSETS_FRAME = 0;
        public static final int TOUCHABLE_INSETS_CONTENT = 1;
        public static final int TOUCHABLE_INSETS_VISIBLE = 2;
        public static final int TOUCHABLE_INSETS_REGION = 3;
        public int touchableInsets;

        public Insets() {
            throw new RuntimeException("Stub!");
        }
    }

    public InputMethodService() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void setTheme(int theme) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public void onCreate() {
        throw new RuntimeException("Stub!");
    }

    public void onInitializeInterface() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public void onDestroy() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.inputmethodservice.AbstractInputMethodService
    public AbstractInputMethodService.AbstractInputMethodImpl onCreateInputMethodInterface() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.inputmethodservice.AbstractInputMethodService
    public AbstractInputMethodService.AbstractInputMethodSessionImpl onCreateInputMethodSessionInterface() {
        throw new RuntimeException("Stub!");
    }

    public LayoutInflater getLayoutInflater() {
        throw new RuntimeException("Stub!");
    }

    public Dialog getWindow() {
        throw new RuntimeException("Stub!");
    }

    public void setBackDisposition(int disposition) {
        throw new RuntimeException("Stub!");
    }

    public int getBackDisposition() {
        throw new RuntimeException("Stub!");
    }

    public int getMaxWidth() {
        throw new RuntimeException("Stub!");
    }

    public InputBinding getCurrentInputBinding() {
        throw new RuntimeException("Stub!");
    }

    public InputConnection getCurrentInputConnection() {
        throw new RuntimeException("Stub!");
    }

    public boolean getCurrentInputStarted() {
        throw new RuntimeException("Stub!");
    }

    public EditorInfo getCurrentInputEditorInfo() {
        throw new RuntimeException("Stub!");
    }

    public void updateFullscreenMode() {
        throw new RuntimeException("Stub!");
    }

    public void onConfigureWindow(Window win, boolean isFullscreen, boolean isCandidatesOnly) {
        throw new RuntimeException("Stub!");
    }

    public boolean isFullscreenMode() {
        throw new RuntimeException("Stub!");
    }

    public boolean onEvaluateFullscreenMode() {
        throw new RuntimeException("Stub!");
    }

    public void setExtractViewShown(boolean shown) {
        throw new RuntimeException("Stub!");
    }

    public boolean isExtractViewShown() {
        throw new RuntimeException("Stub!");
    }

    public void onComputeInsets(Insets outInsets) {
        throw new RuntimeException("Stub!");
    }

    public void updateInputViewShown() {
        throw new RuntimeException("Stub!");
    }

    public boolean isShowInputRequested() {
        throw new RuntimeException("Stub!");
    }

    public boolean isInputViewShown() {
        throw new RuntimeException("Stub!");
    }

    public boolean onEvaluateInputViewShown() {
        throw new RuntimeException("Stub!");
    }

    public void setCandidatesViewShown(boolean shown) {
        throw new RuntimeException("Stub!");
    }

    public int getCandidatesHiddenVisibility() {
        throw new RuntimeException("Stub!");
    }

    public void showStatusIcon(int iconResId) {
        throw new RuntimeException("Stub!");
    }

    public void hideStatusIcon() {
        throw new RuntimeException("Stub!");
    }

    public void switchInputMethod(String id) {
        throw new RuntimeException("Stub!");
    }

    public void setExtractView(View view) {
        throw new RuntimeException("Stub!");
    }

    public void setCandidatesView(View view) {
        throw new RuntimeException("Stub!");
    }

    public void setInputView(View view) {
        throw new RuntimeException("Stub!");
    }

    public View onCreateExtractTextView() {
        throw new RuntimeException("Stub!");
    }

    public View onCreateCandidatesView() {
        throw new RuntimeException("Stub!");
    }

    public View onCreateInputView() {
        throw new RuntimeException("Stub!");
    }

    public void onStartInputView(EditorInfo info, boolean restarting) {
        throw new RuntimeException("Stub!");
    }

    public void onFinishInputView(boolean finishingInput) {
        throw new RuntimeException("Stub!");
    }

    public void onStartCandidatesView(EditorInfo info, boolean restarting) {
        throw new RuntimeException("Stub!");
    }

    public void onFinishCandidatesView(boolean finishingInput) {
        throw new RuntimeException("Stub!");
    }

    public boolean onShowInputRequested(int flags, boolean configChange) {
        throw new RuntimeException("Stub!");
    }

    public void showWindow(boolean showInput) {
        throw new RuntimeException("Stub!");
    }

    public void hideWindow() {
        throw new RuntimeException("Stub!");
    }

    public void onWindowShown() {
        throw new RuntimeException("Stub!");
    }

    public void onWindowHidden() {
        throw new RuntimeException("Stub!");
    }

    public void onBindInput() {
        throw new RuntimeException("Stub!");
    }

    public void onUnbindInput() {
        throw new RuntimeException("Stub!");
    }

    public void onStartInput(EditorInfo attribute, boolean restarting) {
        throw new RuntimeException("Stub!");
    }

    public void onFinishInput() {
        throw new RuntimeException("Stub!");
    }

    public void onDisplayCompletions(CompletionInfo[] completions) {
        throw new RuntimeException("Stub!");
    }

    public void onUpdateExtractedText(int token, ExtractedText text) {
        throw new RuntimeException("Stub!");
    }

    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
        throw new RuntimeException("Stub!");
    }

    public void onViewClicked(boolean focusChanged) {
        throw new RuntimeException("Stub!");
    }

    public void onUpdateCursor(Rect newCursor) {
        throw new RuntimeException("Stub!");
    }

    public void requestHideSelf(int flags) {
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
    public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.inputmethodservice.AbstractInputMethodService
    public boolean onTrackballEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }

    public void onAppPrivateCommand(String action, Bundle data) {
        throw new RuntimeException("Stub!");
    }

    public void sendDownUpKeyEvents(int keyEventCode) {
        throw new RuntimeException("Stub!");
    }

    public boolean sendDefaultEditorAction(boolean fromEnterKey) {
        throw new RuntimeException("Stub!");
    }

    public void sendKeyChar(char charCode) {
        throw new RuntimeException("Stub!");
    }

    public void onExtractedSelectionChanged(int start, int end) {
        throw new RuntimeException("Stub!");
    }

    public void onExtractedTextClicked() {
        throw new RuntimeException("Stub!");
    }

    public void onExtractedCursorMovement(int dx, int dy) {
        throw new RuntimeException("Stub!");
    }

    public boolean onExtractTextContextMenuItem(int id) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getTextForImeAction(int imeOptions) {
        throw new RuntimeException("Stub!");
    }

    public void onUpdateExtractingVisibility(EditorInfo ei) {
        throw new RuntimeException("Stub!");
    }

    public void onUpdateExtractingViews(EditorInfo ei) {
        throw new RuntimeException("Stub!");
    }

    public void onExtractingInputChanged(EditorInfo ei) {
        throw new RuntimeException("Stub!");
    }

    protected void onCurrentInputMethodSubtypeChanged(InputMethodSubtype newSubtype) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.inputmethodservice.AbstractInputMethodService, android.app.Service
    protected void dump(FileDescriptor fd, PrintWriter fout, String[] args) {
        throw new RuntimeException("Stub!");
    }
}
