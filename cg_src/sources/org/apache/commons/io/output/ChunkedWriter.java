package org.apache.commons.io.output;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/output/ChunkedWriter.class */
public class ChunkedWriter extends FilterWriter {
    private static final int DEFAULT_CHUNK_SIZE = 4096;
    private final int chunkSize;

    public ChunkedWriter(Writer writer, int chunkSize) {
        super(writer);
        if (chunkSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.chunkSize = chunkSize;
    }

    public ChunkedWriter(Writer writer) {
        this(writer, 4096);
    }

    @Override // java.io.FilterWriter, java.io.Writer
    public void write(char[] data, int srcOffset, int length) throws IOException {
        int bytes = length;
        int i = srcOffset;
        while (true) {
            int dstOffset = i;
            if (bytes > 0) {
                int chunk = Math.min(bytes, this.chunkSize);
                this.out.write(data, dstOffset, chunk);
                bytes -= chunk;
                i = dstOffset + chunk;
            } else {
                return;
            }
        }
    }
}
