package android.nfc.tech;

import android.nfc.Tag;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/nfc/tech/NfcA.class */
public final class NfcA extends BasicTagTechnology {
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

    NfcA() {
        throw new RuntimeException("Stub!");
    }

    public static NfcA get(Tag tag) {
        throw new RuntimeException("Stub!");
    }

    public byte[] getAtqa() {
        throw new RuntimeException("Stub!");
    }

    public short getSak() {
        throw new RuntimeException("Stub!");
    }

    public byte[] transceive(byte[] data) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public int getMaxTransceiveLength() {
        throw new RuntimeException("Stub!");
    }

    public void setTimeout(int timeout) {
        throw new RuntimeException("Stub!");
    }

    public int getTimeout() {
        throw new RuntimeException("Stub!");
    }
}
