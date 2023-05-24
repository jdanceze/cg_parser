package org.jf.dexlib2.writer.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/io/DexDataStore.class */
public interface DexDataStore {
    @Nonnull
    OutputStream outputAt(int i);

    @Nonnull
    InputStream readAt(int i);

    void close() throws IOException;
}
