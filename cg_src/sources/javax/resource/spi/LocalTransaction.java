package javax.resource.spi;

import javax.resource.ResourceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/LocalTransaction.class */
public interface LocalTransaction {
    void begin() throws ResourceException;

    void commit() throws ResourceException;

    void rollback() throws ResourceException;
}
