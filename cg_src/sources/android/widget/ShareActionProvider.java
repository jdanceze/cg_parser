package android.widget;

import android.content.Context;
import android.content.Intent;
import android.view.ActionProvider;
import android.view.SubMenu;
import android.view.View;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ShareActionProvider.class */
public class ShareActionProvider extends ActionProvider {
    public static final String DEFAULT_SHARE_HISTORY_FILE_NAME = "share_history.xml";

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ShareActionProvider$OnShareTargetSelectedListener.class */
    public interface OnShareTargetSelectedListener {
        boolean onShareTargetSelected(ShareActionProvider shareActionProvider, Intent intent);
    }

    public ShareActionProvider(Context context) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public void setOnShareTargetSelectedListener(OnShareTargetSelectedListener listener) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ActionProvider
    public View onCreateActionView() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ActionProvider
    public boolean hasSubMenu() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ActionProvider
    public void onPrepareSubMenu(SubMenu subMenu) {
        throw new RuntimeException("Stub!");
    }

    public void setShareHistoryFileName(String shareHistoryFile) {
        throw new RuntimeException("Stub!");
    }

    public void setShareIntent(Intent shareIntent) {
        throw new RuntimeException("Stub!");
    }
}
