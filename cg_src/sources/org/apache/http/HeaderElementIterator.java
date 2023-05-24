package org.apache.http;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/HeaderElementIterator.class */
public interface HeaderElementIterator extends Iterator {
    @Override // java.util.Iterator
    boolean hasNext();

    HeaderElement nextElement();
}
