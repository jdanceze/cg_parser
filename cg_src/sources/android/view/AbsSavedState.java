package android.view;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/AbsSavedState.class */
public abstract class AbsSavedState implements Parcelable {
    public static final AbsSavedState EMPTY_STATE = null;
    public static final Parcelable.Creator<AbsSavedState> CREATOR = null;

    protected AbsSavedState(Parcelable superState) {
        throw new RuntimeException("Stub!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbsSavedState(Parcel source) {
        throw new RuntimeException("Stub!");
    }

    public final Parcelable getSuperState() {
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
