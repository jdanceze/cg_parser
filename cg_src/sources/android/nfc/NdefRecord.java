package android.nfc;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/nfc/NdefRecord.class */
public final class NdefRecord implements Parcelable {
    public static final short TNF_EMPTY = 0;
    public static final short TNF_WELL_KNOWN = 1;
    public static final short TNF_MIME_MEDIA = 2;
    public static final short TNF_ABSOLUTE_URI = 3;
    public static final short TNF_EXTERNAL_TYPE = 4;
    public static final short TNF_UNKNOWN = 5;
    public static final short TNF_UNCHANGED = 6;
    public static final byte[] RTD_TEXT = null;
    public static final byte[] RTD_URI = null;
    public static final byte[] RTD_SMART_POSTER = null;
    public static final byte[] RTD_ALTERNATIVE_CARRIER = null;
    public static final byte[] RTD_HANDOVER_CARRIER = null;
    public static final byte[] RTD_HANDOVER_REQUEST = null;
    public static final byte[] RTD_HANDOVER_SELECT = null;
    public static final Parcelable.Creator<NdefRecord> CREATOR = null;

    public NdefRecord(short tnf, byte[] type, byte[] id, byte[] payload) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public NdefRecord(byte[] data) throws FormatException {
        throw new RuntimeException("Stub!");
    }

    public static NdefRecord createApplicationRecord(String packageName) {
        throw new RuntimeException("Stub!");
    }

    public static NdefRecord createUri(Uri uri) {
        throw new RuntimeException("Stub!");
    }

    public static NdefRecord createUri(String uriString) {
        throw new RuntimeException("Stub!");
    }

    public static NdefRecord createMime(String mimeType, byte[] mimeData) {
        throw new RuntimeException("Stub!");
    }

    public static NdefRecord createExternal(String domain, String type, byte[] data) {
        throw new RuntimeException("Stub!");
    }

    public short getTnf() {
        throw new RuntimeException("Stub!");
    }

    public byte[] getType() {
        throw new RuntimeException("Stub!");
    }

    public byte[] getId() {
        throw new RuntimeException("Stub!");
    }

    public byte[] getPayload() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public byte[] toByteArray() {
        throw new RuntimeException("Stub!");
    }

    public String toMimeType() {
        throw new RuntimeException("Stub!");
    }

    public Uri toUri() {
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

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
