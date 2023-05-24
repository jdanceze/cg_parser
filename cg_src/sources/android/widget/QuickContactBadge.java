package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/QuickContactBadge.class */
public class QuickContactBadge extends ImageView implements View.OnClickListener {
    protected String[] mExcludeMimes;

    public QuickContactBadge(Context context) {
        super(null, null, 0);
        this.mExcludeMimes = null;
        throw new RuntimeException("Stub!");
    }

    public QuickContactBadge(Context context, AttributeSet attrs) {
        super(null, null, 0);
        this.mExcludeMimes = null;
        throw new RuntimeException("Stub!");
    }

    public QuickContactBadge(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        this.mExcludeMimes = null;
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ImageView, android.view.View
    protected void drawableStateChanged() {
        throw new RuntimeException("Stub!");
    }

    public void setMode(int size) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    public void setImageToDefault() {
        throw new RuntimeException("Stub!");
    }

    public void assignContactUri(Uri contactUri) {
        throw new RuntimeException("Stub!");
    }

    public void assignContactFromEmail(String emailAddress, boolean lazyLookup) {
        throw new RuntimeException("Stub!");
    }

    public void assignContactFromPhone(String phoneNumber, boolean lazyLookup) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ImageView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ImageView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }

    public void setExcludeMimes(String[] excludeMimes) {
        throw new RuntimeException("Stub!");
    }
}
