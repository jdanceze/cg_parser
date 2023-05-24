package org.apache.commons.io.input;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/input/AbstractCharacterFilterReader.class */
public abstract class AbstractCharacterFilterReader extends FilterReader {
    protected abstract boolean filter(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractCharacterFilterReader(Reader reader) {
        super(reader);
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        int ch;
        do {
            ch = this.in.read();
        } while (filter(ch));
        return ch;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read(char[] cbuf, int off, int len) throws IOException {
        int read = super.read(cbuf, off, len);
        if (read == -1) {
            return -1;
        }
        int pos = off - 1;
        for (int readPos = off; readPos < off + read; readPos++) {
            if (!filter(read)) {
                pos++;
                if (pos < readPos) {
                    cbuf[pos] = cbuf[readPos];
                }
            }
        }
        return (pos - off) + 1;
    }
}
