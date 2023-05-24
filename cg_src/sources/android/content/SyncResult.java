package android.content;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/SyncResult.class */
public final class SyncResult implements Parcelable {
    public final boolean syncAlreadyInProgress;
    public boolean tooManyDeletions;
    public boolean tooManyRetries;
    public boolean databaseError;
    public boolean fullSyncRequested;
    public boolean partialSyncUnavailable;
    public boolean moreRecordsToGet;
    public long delayUntil;
    public final SyncStats stats;
    public static final SyncResult ALREADY_IN_PROGRESS = null;
    public static final Parcelable.Creator<SyncResult> CREATOR = null;

    public SyncResult() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasHardError() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasSoftError() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasError() {
        throw new RuntimeException("Stub!");
    }

    public boolean madeSomeProgress() {
        throw new RuntimeException("Stub!");
    }

    public void clear() {
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

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public String toDebugString() {
        throw new RuntimeException("Stub!");
    }
}
