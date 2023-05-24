package android.view.inputmethod;

import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/inputmethod/BaseInputConnection.class */
public class BaseInputConnection implements InputConnection {
    public BaseInputConnection(View targetView, boolean fullEditor) {
        throw new RuntimeException("Stub!");
    }

    public static final void removeComposingSpans(Spannable text) {
        throw new RuntimeException("Stub!");
    }

    public static void setComposingSpans(Spannable text) {
        throw new RuntimeException("Stub!");
    }

    public static int getComposingSpanStart(Spannable text) {
        throw new RuntimeException("Stub!");
    }

    public static int getComposingSpanEnd(Spannable text) {
        throw new RuntimeException("Stub!");
    }

    public Editable getEditable() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean beginBatchEdit() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean endBatchEdit() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean clearMetaKeyStates(int states) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean commitCompletion(CompletionInfo text) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean commitCorrection(CorrectionInfo correctionInfo) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean commitText(CharSequence text, int newCursorPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean finishComposingText() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public int getCursorCapsMode(int reqModes) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public ExtractedText getExtractedText(ExtractedTextRequest request, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public CharSequence getTextBeforeCursor(int length, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public CharSequence getSelectedText(int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public CharSequence getTextAfterCursor(int length, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean performEditorAction(int actionCode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean performContextMenuAction(int id) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean performPrivateCommand(String action, Bundle data) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean setComposingText(CharSequence text, int newCursorPosition) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean setComposingRegion(int start, int end) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean setSelection(int start, int end) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean sendKeyEvent(KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.inputmethod.InputConnection
    public boolean reportFullscreenMode(boolean enabled) {
        throw new RuntimeException("Stub!");
    }
}
