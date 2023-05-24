package org.apache.http;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/HeaderIterator.class */
public interface HeaderIterator extends Iterator {
    @Override // java.util.Iterator
    boolean hasNext();

    Header nextHeader();
}
