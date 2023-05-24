package org.apache.commons.io.input;

import java.io.Reader;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/input/CloseShieldReader.class */
public class CloseShieldReader extends ProxyReader {
    public CloseShieldReader(Reader in) {
        super(in);
    }

    @Override // org.apache.commons.io.input.ProxyReader, java.io.FilterReader, java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.in = ClosedReader.CLOSED_READER;
    }
}
