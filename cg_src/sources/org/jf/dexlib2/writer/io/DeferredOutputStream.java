package org.jf.dexlib2.writer.io;

import java.io.IOException;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/io/DeferredOutputStream.class */
public abstract class DeferredOutputStream extends OutputStream {
    public abstract void writeTo(OutputStream outputStream) throws IOException;
}
