package android.appwidget;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.widget.RemoteViews;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/appwidget/AppWidgetManager.class */
public class AppWidgetManager {
    public static final String ACTION_APPWIDGET_PICK = "android.appwidget.action.APPWIDGET_PICK";
    public static final String ACTION_APPWIDGET_BIND = "android.appwidget.action.APPWIDGET_BIND";
    public static final String ACTION_APPWIDGET_CONFIGURE = "android.appwidget.action.APPWIDGET_CONFIGURE";
    public static final String EXTRA_APPWIDGET_ID = "appWidgetId";
    public static final String OPTION_APPWIDGET_MIN_WIDTH = "appWidgetMinWidth";
    public static final String OPTION_APPWIDGET_MIN_HEIGHT = "appWidgetMinHeight";
    public static final String OPTION_APPWIDGET_MAX_WIDTH = "appWidgetMaxWidth";
    public static final String OPTION_APPWIDGET_MAX_HEIGHT = "appWidgetMaxHeight";
    public static final String EXTRA_APPWIDGET_OPTIONS = "appWidgetOptions";
    public static final String EXTRA_APPWIDGET_IDS = "appWidgetIds";
    public static final String EXTRA_APPWIDGET_PROVIDER = "appWidgetProvider";
    public static final String EXTRA_CUSTOM_INFO = "customInfo";
    public static final String EXTRA_CUSTOM_EXTRAS = "customExtras";
    public static final int INVALID_APPWIDGET_ID = 0;
    public static final String ACTION_APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final String ACTION_APPWIDGET_OPTIONS_CHANGED = "android.appwidget.action.APPWIDGET_UPDATE_OPTIONS";
    public static final String ACTION_APPWIDGET_DELETED = "android.appwidget.action.APPWIDGET_DELETED";
    public static final String ACTION_APPWIDGET_DISABLED = "android.appwidget.action.APPWIDGET_DISABLED";
    public static final String ACTION_APPWIDGET_ENABLED = "android.appwidget.action.APPWIDGET_ENABLED";
    public static final String META_DATA_APPWIDGET_PROVIDER = "android.appwidget.provider";

    AppWidgetManager() {
        throw new RuntimeException("Stub!");
    }

    public static AppWidgetManager getInstance(Context context) {
        throw new RuntimeException("Stub!");
    }

    public void updateAppWidget(int[] appWidgetIds, RemoteViews views) {
        throw new RuntimeException("Stub!");
    }

    public void updateAppWidgetOptions(int appWidgetId, Bundle options) {
        throw new RuntimeException("Stub!");
    }

    public Bundle getAppWidgetOptions(int appWidgetId) {
        throw new RuntimeException("Stub!");
    }

    public void updateAppWidget(int appWidgetId, RemoteViews views) {
        throw new RuntimeException("Stub!");
    }

    public void partiallyUpdateAppWidget(int[] appWidgetIds, RemoteViews views) {
        throw new RuntimeException("Stub!");
    }

    public void partiallyUpdateAppWidget(int appWidgetId, RemoteViews views) {
        throw new RuntimeException("Stub!");
    }

    public void updateAppWidget(ComponentName provider, RemoteViews views) {
        throw new RuntimeException("Stub!");
    }

    public void notifyAppWidgetViewDataChanged(int[] appWidgetIds, int viewId) {
        throw new RuntimeException("Stub!");
    }

    public void notifyAppWidgetViewDataChanged(int appWidgetId, int viewId) {
        throw new RuntimeException("Stub!");
    }

    public List<AppWidgetProviderInfo> getInstalledProviders() {
        throw new RuntimeException("Stub!");
    }

    public AppWidgetProviderInfo getAppWidgetInfo(int appWidgetId) {
        throw new RuntimeException("Stub!");
    }

    public boolean bindAppWidgetIdIfAllowed(int appWidgetId, ComponentName provider) {
        throw new RuntimeException("Stub!");
    }

    public int[] getAppWidgetIds(ComponentName provider) {
        throw new RuntimeException("Stub!");
    }
}
