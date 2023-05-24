package android.nfc.tech;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.Tag;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/nfc/tech/NdefFormatable.class */
public final class NdefFormatable extends BasicTagTechnology {
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

    NdefFormatable() {
        throw new RuntimeException("Stub!");
    }

    public static NdefFormatable get(Tag tag) {
        throw new RuntimeException("Stub!");
    }

    public void format(NdefMessage firstMessage) throws IOException, FormatException {
        throw new RuntimeException("Stub!");
    }

    public void formatReadOnly(NdefMessage firstMessage) throws IOException, FormatException {
        throw new RuntimeException("Stub!");
    }
}
