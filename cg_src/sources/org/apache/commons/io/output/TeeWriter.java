package org.apache.commons.io.output;

import java.io.Writer;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/output/TeeWriter.class */
public class TeeWriter extends ProxyCollectionWriter {
    public TeeWriter(Collection<Writer> writers) {
        super(writers);
    }

    public TeeWriter(Writer... writers) {
        super(writers);
    }
}
