package android.widget;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/PopupMenu.class */
public class PopupMenu {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/PopupMenu$OnDismissListener.class */
    public interface OnDismissListener {
        void onDismiss(PopupMenu popupMenu);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/PopupMenu$OnMenuItemClickListener.class */
    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public PopupMenu(Context context, View anchor) {
        throw new RuntimeException("Stub!");
    }

    public Menu getMenu() {
        throw new RuntimeException("Stub!");
    }

    public MenuInflater getMenuInflater() {
        throw new RuntimeException("Stub!");
    }

    public void inflate(int menuRes) {
        throw new RuntimeException("Stub!");
    }

    public void show() {
        throw new RuntimeException("Stub!");
    }

    public void dismiss() {
        throw new RuntimeException("Stub!");
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnDismissListener(OnDismissListener listener) {
        throw new RuntimeException("Stub!");
    }
}
