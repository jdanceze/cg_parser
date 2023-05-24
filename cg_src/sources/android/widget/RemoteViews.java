package android.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/RemoteViews.class */
public class RemoteViews implements Parcelable, LayoutInflater.Filter {
    public static final Parcelable.Creator<RemoteViews> CREATOR = null;

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/RemoteViews$RemoteView.class */
    public @interface RemoteView {
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/RemoteViews$ActionException.class */
    public static class ActionException extends RuntimeException {
        public ActionException(Exception ex) {
            throw new RuntimeException("Stub!");
        }

        public ActionException(String message) {
            throw new RuntimeException("Stub!");
        }
    }

    public RemoteViews(String packageName, int layoutId) {
        throw new RuntimeException("Stub!");
    }

    public RemoteViews(RemoteViews landscape, RemoteViews portrait) {
        throw new RuntimeException("Stub!");
    }

    public RemoteViews(Parcel parcel) {
        throw new RuntimeException("Stub!");
    }

    /* renamed from: clone */
    public RemoteViews m283clone() {
        throw new RuntimeException("Stub!");
    }

    public String getPackage() {
        throw new RuntimeException("Stub!");
    }

    public int getLayoutId() {
        throw new RuntimeException("Stub!");
    }

    public void addView(int viewId, RemoteViews nestedView) {
        throw new RuntimeException("Stub!");
    }

    public void removeAllViews(int viewId) {
        throw new RuntimeException("Stub!");
    }

    public void showNext(int viewId) {
        throw new RuntimeException("Stub!");
    }

    public void showPrevious(int viewId) {
        throw new RuntimeException("Stub!");
    }

    public void setDisplayedChild(int viewId, int childIndex) {
        throw new RuntimeException("Stub!");
    }

    public void setViewVisibility(int viewId, int visibility) {
        throw new RuntimeException("Stub!");
    }

    public void setTextViewText(int viewId, CharSequence text) {
        throw new RuntimeException("Stub!");
    }

    public void setTextViewTextSize(int viewId, int units, float size) {
        throw new RuntimeException("Stub!");
    }

    public void setTextViewCompoundDrawables(int viewId, int left, int top, int right, int bottom) {
        throw new RuntimeException("Stub!");
    }

    public void setTextViewCompoundDrawablesRelative(int viewId, int start, int top, int end, int bottom) {
        throw new RuntimeException("Stub!");
    }

    public void setImageViewResource(int viewId, int srcId) {
        throw new RuntimeException("Stub!");
    }

    public void setImageViewUri(int viewId, Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public void setImageViewBitmap(int viewId, Bitmap bitmap) {
        throw new RuntimeException("Stub!");
    }

    public void setEmptyView(int viewId, int emptyViewId) {
        throw new RuntimeException("Stub!");
    }

    public void setChronometer(int viewId, long base, String format, boolean started) {
        throw new RuntimeException("Stub!");
    }

    public void setProgressBar(int viewId, int max, int progress, boolean indeterminate) {
        throw new RuntimeException("Stub!");
    }

    public void setOnClickPendingIntent(int viewId, PendingIntent pendingIntent) {
        throw new RuntimeException("Stub!");
    }

    public void setPendingIntentTemplate(int viewId, PendingIntent pendingIntentTemplate) {
        throw new RuntimeException("Stub!");
    }

    public void setOnClickFillInIntent(int viewId, Intent fillInIntent) {
        throw new RuntimeException("Stub!");
    }

    public void setTextColor(int viewId, int color) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setRemoteAdapter(int appWidgetId, int viewId, Intent intent) {
        throw new RuntimeException("Stub!");
    }

    public void setRemoteAdapter(int viewId, Intent intent) {
        throw new RuntimeException("Stub!");
    }

    public void setScrollPosition(int viewId, int position) {
        throw new RuntimeException("Stub!");
    }

    public void setRelativeScrollPosition(int viewId, int offset) {
        throw new RuntimeException("Stub!");
    }

    public void setViewPadding(int viewId, int left, int top, int right, int bottom) {
        throw new RuntimeException("Stub!");
    }

    public void setBoolean(int viewId, String methodName, boolean value) {
        throw new RuntimeException("Stub!");
    }

    public void setByte(int viewId, String methodName, byte value) {
        throw new RuntimeException("Stub!");
    }

    public void setShort(int viewId, String methodName, short value) {
        throw new RuntimeException("Stub!");
    }

    public void setInt(int viewId, String methodName, int value) {
        throw new RuntimeException("Stub!");
    }

    public void setLong(int viewId, String methodName, long value) {
        throw new RuntimeException("Stub!");
    }

    public void setFloat(int viewId, String methodName, float value) {
        throw new RuntimeException("Stub!");
    }

    public void setDouble(int viewId, String methodName, double value) {
        throw new RuntimeException("Stub!");
    }

    public void setChar(int viewId, String methodName, char value) {
        throw new RuntimeException("Stub!");
    }

    public void setString(int viewId, String methodName, String value) {
        throw new RuntimeException("Stub!");
    }

    public void setCharSequence(int viewId, String methodName, CharSequence value) {
        throw new RuntimeException("Stub!");
    }

    public void setUri(int viewId, String methodName, Uri value) {
        throw new RuntimeException("Stub!");
    }

    public void setBitmap(int viewId, String methodName, Bitmap value) {
        throw new RuntimeException("Stub!");
    }

    public void setBundle(int viewId, String methodName, Bundle value) {
        throw new RuntimeException("Stub!");
    }

    public void setIntent(int viewId, String methodName, Intent value) {
        throw new RuntimeException("Stub!");
    }

    public void setContentDescription(int viewId, CharSequence contentDescription) {
        throw new RuntimeException("Stub!");
    }

    public View apply(Context context, ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }

    public void reapply(Context context, View v) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.LayoutInflater.Filter
    public boolean onLoadClass(Class clazz) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }
}
