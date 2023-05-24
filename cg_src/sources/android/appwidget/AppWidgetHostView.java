package android.appwidget;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/appwidget/AppWidgetHostView.class */
public class AppWidgetHostView extends FrameLayout {
    public AppWidgetHostView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AppWidgetHostView(Context context, int animationIn, int animationOut) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setAppWidget(int appWidgetId, AppWidgetProviderInfo info) {
        throw new RuntimeException("Stub!");
    }

    public static Rect getDefaultPaddingForWidget(Context context, ComponentName component, Rect padding) {
        throw new RuntimeException("Stub!");
    }

    public int getAppWidgetId() {
        throw new RuntimeException("Stub!");
    }

    public AppWidgetProviderInfo getAppWidgetInfo() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        throw new RuntimeException("Stub!");
    }

    public void updateAppWidgetSize(Bundle options, int minWidth, int minHeight, int maxWidth, int maxHeight) {
        throw new RuntimeException("Stub!");
    }

    public void updateAppWidgetOptions(Bundle options) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    public void updateAppWidget(RemoteViews remoteViews) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        throw new RuntimeException("Stub!");
    }

    protected void prepareView(View view) {
        throw new RuntimeException("Stub!");
    }

    protected View getDefaultView() {
        throw new RuntimeException("Stub!");
    }

    protected View getErrorView() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
