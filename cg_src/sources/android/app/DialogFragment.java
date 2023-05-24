package android.app;

import android.content.DialogInterface;
import android.os.Bundle;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/DialogFragment.class */
public class DialogFragment extends Fragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {
    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_NO_TITLE = 1;
    public static final int STYLE_NO_FRAME = 2;
    public static final int STYLE_NO_INPUT = 3;

    public DialogFragment() {
        throw new RuntimeException("Stub!");
    }

    public void setStyle(int style, int theme) {
        throw new RuntimeException("Stub!");
    }

    public void show(FragmentManager manager, String tag) {
        throw new RuntimeException("Stub!");
    }

    public int show(FragmentTransaction transaction, String tag) {
        throw new RuntimeException("Stub!");
    }

    public void dismiss() {
        throw new RuntimeException("Stub!");
    }

    public void dismissAllowingStateLoss() {
        throw new RuntimeException("Stub!");
    }

    public Dialog getDialog() {
        throw new RuntimeException("Stub!");
    }

    public int getTheme() {
        throw new RuntimeException("Stub!");
    }

    public void setCancelable(boolean cancelable) {
        throw new RuntimeException("Stub!");
    }

    public boolean isCancelable() {
        throw new RuntimeException("Stub!");
    }

    public void setShowsDialog(boolean showsDialog) {
        throw new RuntimeException("Stub!");
    }

    public boolean getShowsDialog() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onDetach() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialog) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onStart() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onStop() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Fragment
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        throw new RuntimeException("Stub!");
    }
}
