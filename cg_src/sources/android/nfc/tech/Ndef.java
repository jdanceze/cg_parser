package android.nfc.tech;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.Tag;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/nfc/tech/Ndef.class */
public final class Ndef extends BasicTagTechnology {
    public static final String NFC_FORUM_TYPE_1 = "org.nfcforum.ndef.type1";
    public static final String NFC_FORUM_TYPE_2 = "org.nfcforum.ndef.type2";
    public static final String NFC_FORUM_TYPE_3 = "org.nfcforum.ndef.type3";
    public static final String NFC_FORUM_TYPE_4 = "org.nfcforum.ndef.type4";
    public static final String MIFARE_CLASSIC = "com.nxp.ndef.mifareclassic";

    @Override // android.nfc.tech.BasicTagTechnology, android.nfc.tech.TagTechnology, java.io.Closeable, java.lang.AutoCloseable
    public /* bridge */ /* synthetic */ void close() throws IOException {
        super.close();
    }

    @Override // android.nfc.tech.BasicTagTechnology, android.nfc.tech.TagTechnology
    public /* bridge */ /* synthetic */ void connect() throws IOException {
        super.connect();
    }

    @Override // android.nfc.tech.BasicTagTechnology, android.nfc.tech.TagTechnology
    public /* bridge */ /* synthetic */ boolean isConnected() {
        return super.isConnected();
    }

    @Override // android.nfc.tech.BasicTagTechnology, android.nfc.tech.TagTechnology
    public /* bridge */ /* synthetic */ Tag getTag() {
        return super.getTag();
    }

    Ndef() {
        throw new RuntimeException("Stub!");
    }

    public static Ndef get(Tag tag) {
        throw new RuntimeException("Stub!");
    }

    public NdefMessage getCachedNdefMessage() {
        throw new RuntimeException("Stub!");
    }

    public String getType() {
        throw new RuntimeException("Stub!");
    }

    public int getMaxSize() {
        throw new RuntimeException("Stub!");
    }

    public boolean isWritable() {
        throw new RuntimeException("Stub!");
    }

    public NdefMessage getNdefMessage() throws IOException, FormatException {
        throw new RuntimeException("Stub!");
    }

    public void writeNdefMessage(NdefMessage msg) throws IOException, FormatException {
        throw new RuntimeException("Stub!");
    }

    public boolean canMakeReadOnly() {
        throw new RuntimeException("Stub!");
    }

    public boolean makeReadOnly() throws IOException {
        throw new RuntimeException("Stub!");
    }
}
