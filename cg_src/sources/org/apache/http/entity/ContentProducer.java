package org.apache.http.entity;

import java.io.IOException;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/entity/ContentProducer.class */
public interface ContentProducer {
    void writeTo(OutputStream outputStream) throws IOException;
}
