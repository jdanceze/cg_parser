package android.view;

import android.graphics.drawable.Drawable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ContextMenu.class */
public interface ContextMenu extends Menu {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ContextMenu$ContextMenuInfo.class */
    public interface ContextMenuInfo {
    }

    ContextMenu setHeaderTitle(int i);

    ContextMenu setHeaderTitle(CharSequence charSequence);

    ContextMenu setHeaderIcon(int i);

    ContextMenu setHeaderIcon(Drawable drawable);

    ContextMenu setHeaderView(View view);

    void clearHeader();
}
