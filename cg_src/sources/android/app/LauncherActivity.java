package android.app;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/LauncherActivity.class */
public abstract class LauncherActivity extends ListActivity {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/LauncherActivity$ListItem.class */
    public static class ListItem {
        public ResolveInfo resolveInfo;
        public CharSequence label;
        public Drawable icon;
        public String packageName;
        public String className;
        public Bundle extras;

        public ListItem() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/LauncherActivity$IconResizer.class */
    public class IconResizer {
        public IconResizer() {
            throw new RuntimeException("Stub!");
        }

        public Drawable createIconThumbnail(Drawable icon) {
            throw new RuntimeException("Stub!");
        }
    }

    public LauncherActivity() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle icicle) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    public void setTitle(CharSequence title) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Activity
    public void setTitle(int titleId) {
        throw new RuntimeException("Stub!");
    }

    protected void onSetContentView() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.ListActivity
    protected void onListItemClick(ListView l, View v, int position, long id) {
        throw new RuntimeException("Stub!");
    }

    protected Intent intentForPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    protected ListItem itemForPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    protected Intent getTargetIntent() {
        throw new RuntimeException("Stub!");
    }

    protected List<ResolveInfo> onQueryPackageManager(Intent queryIntent) {
        throw new RuntimeException("Stub!");
    }

    public List<ListItem> makeListItems() {
        throw new RuntimeException("Stub!");
    }
}
