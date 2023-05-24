package android.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/ContextThemeWrapper.class */
public class ContextThemeWrapper extends ContextWrapper {
    public ContextThemeWrapper() {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public ContextThemeWrapper(Context base, int themeres) {
        super(null);
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context newBase) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void setTheme(int resid) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources.Theme getTheme() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Object getSystemService(String name) {
        throw new RuntimeException("Stub!");
    }

    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        throw new RuntimeException("Stub!");
    }
}
