package android.content;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ContentProviderOperation.class */
public class ContentProviderOperation implements Parcelable {
    public static final Parcelable.Creator<ContentProviderOperation> CREATOR = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/ContentProviderOperation$Builder.class */
    public static class Builder {
        Builder() {
            throw new RuntimeException("Stub!");
        }

        public ContentProviderOperation build() {
            throw new RuntimeException("Stub!");
        }

        public Builder withValueBackReferences(ContentValues backReferences) {
            throw new RuntimeException("Stub!");
        }

        public Builder withValueBackReference(String key, int previousResult) {
            throw new RuntimeException("Stub!");
        }

        public Builder withSelectionBackReference(int selectionArgIndex, int previousResult) {
            throw new RuntimeException("Stub!");
        }

        public Builder withValues(ContentValues values) {
            throw new RuntimeException("Stub!");
        }

        public Builder withValue(String key, Object value) {
            throw new RuntimeException("Stub!");
        }

        public Builder withSelection(String selection, String[] selectionArgs) {
            throw new RuntimeException("Stub!");
        }

        public Builder withExpectedCount(int count) {
            throw new RuntimeException("Stub!");
        }

        public Builder withYieldAllowed(boolean yieldAllowed) {
            throw new RuntimeException("Stub!");
        }
    }

    ContentProviderOperation() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }

    public static Builder newInsert(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public static Builder newUpdate(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public static Builder newDelete(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public static Builder newAssertQuery(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public Uri getUri() {
        throw new RuntimeException("Stub!");
    }

    public boolean isYieldAllowed() {
        throw new RuntimeException("Stub!");
    }

    public boolean isWriteOperation() {
        throw new RuntimeException("Stub!");
    }

    public boolean isReadOperation() {
        throw new RuntimeException("Stub!");
    }

    public ContentProviderResult apply(ContentProvider provider, ContentProviderResult[] backRefs, int numBackRefs) throws OperationApplicationException {
        throw new RuntimeException("Stub!");
    }

    public ContentValues resolveValueBackReferences(ContentProviderResult[] backRefs, int numBackRefs) {
        throw new RuntimeException("Stub!");
    }

    public String[] resolveSelectionArgsBackReferences(ContentProviderResult[] backRefs, int numBackRefs) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }
}
