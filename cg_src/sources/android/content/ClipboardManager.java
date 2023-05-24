package android.content;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ClipboardManager.class */
public class ClipboardManager extends android.text.ClipboardManager {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ClipboardManager$OnPrimaryClipChangedListener.class */
    public interface OnPrimaryClipChangedListener {
        void onPrimaryClipChanged();
    }

    ClipboardManager() {
        throw new RuntimeException("Stub!");
    }

    public void setPrimaryClip(ClipData clip) {
        throw new RuntimeException("Stub!");
    }

    public ClipData getPrimaryClip() {
        throw new RuntimeException("Stub!");
    }

    public ClipDescription getPrimaryClipDescription() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasPrimaryClip() {
        throw new RuntimeException("Stub!");
    }

    public void addPrimaryClipChangedListener(OnPrimaryClipChangedListener what) {
        throw new RuntimeException("Stub!");
    }

    public void removePrimaryClipChangedListener(OnPrimaryClipChangedListener what) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.ClipboardManager
    @Deprecated
    public CharSequence getText() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.ClipboardManager
    @Deprecated
    public void setText(CharSequence text) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.text.ClipboardManager
    @Deprecated
    public boolean hasText() {
        throw new RuntimeException("Stub!");
    }
}
