package org.apache.tools.tar;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/tar/TarArchiveSparseEntry.class */
public class TarArchiveSparseEntry implements TarConstants {
    private boolean isExtended;

    public TarArchiveSparseEntry(byte[] headerBuf) throws IOException {
        int offset = 0 + 504;
        this.isExtended = TarUtils.parseBoolean(headerBuf, offset);
    }

    public boolean isExtended() {
        return this.isExtended;
    }
}
