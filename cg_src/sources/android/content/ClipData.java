package android.content;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ClipData.class */
public class ClipData implements Parcelable {
    public static final Parcelable.Creator<ClipData> CREATOR = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ClipData$Item.class */
    public static class Item {
        public Item(CharSequence text) {
            throw new RuntimeException("Stub!");
        }

        public Item(CharSequence text, String htmlText) {
            throw new RuntimeException("Stub!");
        }

        public Item(Intent intent) {
            throw new RuntimeException("Stub!");
        }

        public Item(Uri uri) {
            throw new RuntimeException("Stub!");
        }

        public Item(CharSequence text, Intent intent, Uri uri) {
            throw new RuntimeException("Stub!");
        }

        public Item(CharSequence text, String htmlText, Intent intent, Uri uri) {
            throw new RuntimeException("Stub!");
        }

        public CharSequence getText() {
            throw new RuntimeException("Stub!");
        }

        public String getHtmlText() {
            throw new RuntimeException("Stub!");
        }

        public Intent getIntent() {
            throw new RuntimeException("Stub!");
        }

        public Uri getUri() {
            throw new RuntimeException("Stub!");
        }

        public CharSequence coerceToText(Context context) {
            throw new RuntimeException("Stub!");
        }

        public CharSequence coerceToStyledText(Context context) {
            throw new RuntimeException("Stub!");
        }

        public String coerceToHtmlText(Context context) {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }

    public ClipData(CharSequence label, String[] mimeTypes, Item item) {
        throw new RuntimeException("Stub!");
    }

    public ClipData(ClipDescription description, Item item) {
        throw new RuntimeException("Stub!");
    }

    public ClipData(ClipData other) {
        throw new RuntimeException("Stub!");
    }

    public static ClipData newPlainText(CharSequence label, CharSequence text) {
        throw new RuntimeException("Stub!");
    }

    public static ClipData newHtmlText(CharSequence label, CharSequence text, String htmlText) {
        throw new RuntimeException("Stub!");
    }

    public static ClipData newIntent(CharSequence label, Intent intent) {
        throw new RuntimeException("Stub!");
    }

    public static ClipData newUri(ContentResolver resolver, CharSequence label, Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public static ClipData newRawUri(CharSequence label, Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public ClipDescription getDescription() {
        throw new RuntimeException("Stub!");
    }

    public void addItem(Item item) {
        throw new RuntimeException("Stub!");
    }

    public int getItemCount() {
        throw new RuntimeException("Stub!");
    }

    public Item getItemAt(int index) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
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
