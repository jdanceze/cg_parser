package android.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.RemoteViews;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Notification.class */
public class Notification implements Parcelable {
    public static final int DEFAULT_ALL = -1;
    public static final int DEFAULT_SOUND = 1;
    public static final int DEFAULT_VIBRATE = 2;
    public static final int DEFAULT_LIGHTS = 4;
    public long when;
    public int icon;
    public int iconLevel;
    public int number;
    public PendingIntent contentIntent;
    public PendingIntent deleteIntent;
    public PendingIntent fullScreenIntent;
    public CharSequence tickerText;
    public RemoteViews tickerView;
    public RemoteViews contentView;
    public RemoteViews bigContentView;
    public Bitmap largeIcon;
    public Uri sound;
    public static final int STREAM_DEFAULT = -1;
    public int audioStreamType;
    public long[] vibrate = null;
    public int ledARGB;
    public int ledOnMS;
    public int ledOffMS;
    public int defaults;
    public static final int FLAG_SHOW_LIGHTS = 1;
    public static final int FLAG_ONGOING_EVENT = 2;
    public static final int FLAG_INSISTENT = 4;
    public static final int FLAG_ONLY_ALERT_ONCE = 8;
    public static final int FLAG_AUTO_CANCEL = 16;
    public static final int FLAG_NO_CLEAR = 32;
    public static final int FLAG_FOREGROUND_SERVICE = 64;
    @Deprecated
    public static final int FLAG_HIGH_PRIORITY = 128;
    public int flags;
    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_MIN = -2;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MAX = 2;
    public int priority;
    public static final Parcelable.Creator<Notification> CREATOR = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Notification$Builder.class */
    public static class Builder {
        public Builder(Context context) {
            throw new RuntimeException("Stub!");
        }

        public Builder setWhen(long when) {
            throw new RuntimeException("Stub!");
        }

        public Builder setUsesChronometer(boolean b) {
            throw new RuntimeException("Stub!");
        }

        public Builder setSmallIcon(int icon) {
            throw new RuntimeException("Stub!");
        }

        public Builder setSmallIcon(int icon, int level) {
            throw new RuntimeException("Stub!");
        }

        public Builder setContentTitle(CharSequence title) {
            throw new RuntimeException("Stub!");
        }

        public Builder setContentText(CharSequence text) {
            throw new RuntimeException("Stub!");
        }

        public Builder setSubText(CharSequence text) {
            throw new RuntimeException("Stub!");
        }

        public Builder setNumber(int number) {
            throw new RuntimeException("Stub!");
        }

        public Builder setContentInfo(CharSequence info) {
            throw new RuntimeException("Stub!");
        }

        public Builder setProgress(int max, int progress, boolean indeterminate) {
            throw new RuntimeException("Stub!");
        }

        public Builder setContent(RemoteViews views) {
            throw new RuntimeException("Stub!");
        }

        public Builder setContentIntent(PendingIntent intent) {
            throw new RuntimeException("Stub!");
        }

        public Builder setDeleteIntent(PendingIntent intent) {
            throw new RuntimeException("Stub!");
        }

        public Builder setFullScreenIntent(PendingIntent intent, boolean highPriority) {
            throw new RuntimeException("Stub!");
        }

        public Builder setTicker(CharSequence tickerText) {
            throw new RuntimeException("Stub!");
        }

        public Builder setTicker(CharSequence tickerText, RemoteViews views) {
            throw new RuntimeException("Stub!");
        }

        public Builder setLargeIcon(Bitmap icon) {
            throw new RuntimeException("Stub!");
        }

        public Builder setSound(Uri sound) {
            throw new RuntimeException("Stub!");
        }

        public Builder setSound(Uri sound, int streamType) {
            throw new RuntimeException("Stub!");
        }

        public Builder setVibrate(long[] pattern) {
            throw new RuntimeException("Stub!");
        }

        public Builder setLights(int argb, int onMs, int offMs) {
            throw new RuntimeException("Stub!");
        }

        public Builder setOngoing(boolean ongoing) {
            throw new RuntimeException("Stub!");
        }

        public Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
            throw new RuntimeException("Stub!");
        }

        public Builder setAutoCancel(boolean autoCancel) {
            throw new RuntimeException("Stub!");
        }

        public Builder setDefaults(int defaults) {
            throw new RuntimeException("Stub!");
        }

        public Builder setPriority(int pri) {
            throw new RuntimeException("Stub!");
        }

        public Builder addAction(int icon, CharSequence title, PendingIntent intent) {
            throw new RuntimeException("Stub!");
        }

        public Builder setStyle(Style style) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Notification getNotification() {
            throw new RuntimeException("Stub!");
        }

        public Notification build() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Notification$Style.class */
    public static abstract class Style {
        protected Builder mBuilder;

        public abstract Notification build();

        public Style() {
            throw new RuntimeException("Stub!");
        }

        protected void internalSetBigContentTitle(CharSequence title) {
            throw new RuntimeException("Stub!");
        }

        protected void internalSetSummaryText(CharSequence cs) {
            throw new RuntimeException("Stub!");
        }

        public void setBuilder(Builder builder) {
            throw new RuntimeException("Stub!");
        }

        protected void checkBuilder() {
            throw new RuntimeException("Stub!");
        }

        protected RemoteViews getStandardView(int layoutId) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Notification$BigPictureStyle.class */
    public static class BigPictureStyle extends Style {
        public BigPictureStyle() {
            throw new RuntimeException("Stub!");
        }

        public BigPictureStyle(Builder builder) {
            throw new RuntimeException("Stub!");
        }

        public BigPictureStyle setBigContentTitle(CharSequence title) {
            throw new RuntimeException("Stub!");
        }

        public BigPictureStyle setSummaryText(CharSequence cs) {
            throw new RuntimeException("Stub!");
        }

        public BigPictureStyle bigPicture(Bitmap b) {
            throw new RuntimeException("Stub!");
        }

        public BigPictureStyle bigLargeIcon(Bitmap b) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.app.Notification.Style
        public Notification build() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Notification$BigTextStyle.class */
    public static class BigTextStyle extends Style {
        public BigTextStyle() {
            throw new RuntimeException("Stub!");
        }

        public BigTextStyle(Builder builder) {
            throw new RuntimeException("Stub!");
        }

        public BigTextStyle setBigContentTitle(CharSequence title) {
            throw new RuntimeException("Stub!");
        }

        public BigTextStyle setSummaryText(CharSequence cs) {
            throw new RuntimeException("Stub!");
        }

        public BigTextStyle bigText(CharSequence cs) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.app.Notification.Style
        public Notification build() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/Notification$InboxStyle.class */
    public static class InboxStyle extends Style {
        public InboxStyle() {
            throw new RuntimeException("Stub!");
        }

        public InboxStyle(Builder builder) {
            throw new RuntimeException("Stub!");
        }

        public InboxStyle setBigContentTitle(CharSequence title) {
            throw new RuntimeException("Stub!");
        }

        public InboxStyle setSummaryText(CharSequence cs) {
            throw new RuntimeException("Stub!");
        }

        public InboxStyle addLine(CharSequence cs) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.app.Notification.Style
        public Notification build() {
            throw new RuntimeException("Stub!");
        }
    }

    public Notification() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public Notification(int icon, CharSequence tickerText, long when) {
        throw new RuntimeException("Stub!");
    }

    public Notification(Parcel parcel) {
        throw new RuntimeException("Stub!");
    }

    /* renamed from: clone */
    public Notification m17clone() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setLatestEventInfo(Context context, CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
