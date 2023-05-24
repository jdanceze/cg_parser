package android.nfc.tech;

import android.nfc.Tag;
import java.io.Closeable;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/nfc/tech/TagTechnology.class */
public interface TagTechnology extends Closeable {
    Tag getTag();

    void connect() throws IOException;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close() throws IOException;

    boolean isConnected();
}
