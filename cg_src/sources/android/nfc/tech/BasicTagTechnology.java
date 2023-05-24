package android.nfc.tech;

import android.nfc.Tag;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/nfc/tech/BasicTagTechnology.class */
abstract class BasicTagTechnology implements TagTechnology {
    /* JADX INFO: Access modifiers changed from: package-private */
    public BasicTagTechnology() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.nfc.tech.TagTechnology
    public Tag getTag() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.nfc.tech.TagTechnology
    public boolean isConnected() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.nfc.tech.TagTechnology
    public void connect() throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.nfc.tech.TagTechnology, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }
}
