package android.widget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/RemoteViewsService.class */
public abstract class RemoteViewsService extends Service {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/RemoteViewsService$RemoteViewsFactory.class */
    public interface RemoteViewsFactory {
        void onCreate();

        void onDataSetChanged();

        void onDestroy();

        int getCount();

        RemoteViews getViewAt(int i);

        RemoteViews getLoadingView();

        int getViewTypeCount();

        long getItemId(int i);

        boolean hasStableIds();
    }

    public abstract RemoteViewsFactory onGetViewFactory(Intent intent);

    public RemoteViewsService() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        throw new RuntimeException("Stub!");
    }
}
