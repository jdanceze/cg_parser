package android.view;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ActionMode.class */
public abstract class ActionMode {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ActionMode$Callback.class */
    public interface Callback {
        boolean onCreateActionMode(ActionMode actionMode, Menu menu);

        boolean onPrepareActionMode(ActionMode actionMode, Menu menu);

        boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem);

        void onDestroyActionMode(ActionMode actionMode);
    }

    public abstract void setTitle(CharSequence charSequence);

    public abstract void setTitle(int i);

    public abstract void setSubtitle(CharSequence charSequence);

    public abstract void setSubtitle(int i);

    public abstract void setCustomView(View view);

    public abstract void invalidate();

    public abstract void finish();

    public abstract Menu getMenu();

    public abstract CharSequence getTitle();

    public abstract CharSequence getSubtitle();

    public abstract View getCustomView();

    public abstract MenuInflater getMenuInflater();

    public ActionMode() {
        throw new RuntimeException("Stub!");
    }

    public void setTag(Object tag) {
        throw new RuntimeException("Stub!");
    }

    public Object getTag() {
        throw new RuntimeException("Stub!");
    }

    public void setTitleOptionalHint(boolean titleOptional) {
        throw new RuntimeException("Stub!");
    }

    public boolean getTitleOptionalHint() {
        throw new RuntimeException("Stub!");
    }

    public boolean isTitleOptional() {
        throw new RuntimeException("Stub!");
    }
}
