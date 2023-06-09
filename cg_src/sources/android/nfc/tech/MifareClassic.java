package android.nfc.tech;

import android.nfc.Tag;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/nfc/tech/MifareClassic.class */
public final class MifareClassic extends BasicTagTechnology {
    public static final byte[] KEY_DEFAULT = null;
    public static final byte[] KEY_MIFARE_APPLICATION_DIRECTORY = null;
    public static final byte[] KEY_NFC_FORUM = null;
    public static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_CLASSIC = 0;
    public static final int TYPE_PLUS = 1;
    public static final int TYPE_PRO = 2;
    public static final int SIZE_1K = 1024;
    public static final int SIZE_2K = 2048;
    public static final int SIZE_4K = 4096;
    public static final int SIZE_MINI = 320;
    public static final int BLOCK_SIZE = 16;

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

    MifareClassic() {
        throw new RuntimeException("Stub!");
    }

    public static MifareClassic get(Tag tag) {
        throw new RuntimeException("Stub!");
    }

    public int getType() {
        throw new RuntimeException("Stub!");
    }

    public int getSize() {
        throw new RuntimeException("Stub!");
    }

    public int getSectorCount() {
        throw new RuntimeException("Stub!");
    }

    public int getBlockCount() {
        throw new RuntimeException("Stub!");
    }

    public int getBlockCountInSector(int sectorIndex) {
        throw new RuntimeException("Stub!");
    }

    public int blockToSector(int blockIndex) {
        throw new RuntimeException("Stub!");
    }

    public int sectorToBlock(int sectorIndex) {
        throw new RuntimeException("Stub!");
    }

    public boolean authenticateSectorWithKeyA(int sectorIndex, byte[] key) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public boolean authenticateSectorWithKeyB(int sectorIndex, byte[] key) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public byte[] readBlock(int blockIndex) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void writeBlock(int blockIndex, byte[] data) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void increment(int blockIndex, int value) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void decrement(int blockIndex, int value) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void transfer(int blockIndex) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void restore(int blockIndex) throws IOException {
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
