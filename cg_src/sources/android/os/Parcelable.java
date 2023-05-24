package android.os;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Parcelable.class */
public interface Parcelable {
    public static final int PARCELABLE_WRITE_RETURN_VALUE = 1;
    public static final int CONTENTS_FILE_DESCRIPTOR = 1;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Parcelable$ClassLoaderCreator.class */
    public interface ClassLoaderCreator<T> extends Creator<T> {
        T createFromParcel(Parcel parcel, ClassLoader classLoader);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/Parcelable$Creator.class */
    public interface Creator<T> {
        T createFromParcel(Parcel parcel);

        T[] newArray(int i);
    }

    int describeContents();

    void writeToParcel(Parcel parcel, int i);
}
