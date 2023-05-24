package org.jf.dexlib2.writer.io;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/io/DeferredOutputStreamFactory.class */
public interface DeferredOutputStreamFactory {
    DeferredOutputStream makeDeferredOutputStream() throws IOException;
}
