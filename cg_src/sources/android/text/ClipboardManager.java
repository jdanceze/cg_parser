package android.text;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/ClipboardManager.class */
public abstract class ClipboardManager {
    public abstract CharSequence getText();

    public abstract void setText(CharSequence charSequence);

    public abstract boolean hasText();

    public ClipboardManager() {
        throw new RuntimeException("Stub!");
    }
}
