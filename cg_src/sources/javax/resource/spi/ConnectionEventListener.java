package javax.resource.spi;

import java.util.EventListener;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/ConnectionEventListener.class */
public interface ConnectionEventListener extends EventListener {
    void connectionClosed(ConnectionEvent connectionEvent);

    void localTransactionStarted(ConnectionEvent connectionEvent);

    void localTransactionCommitted(ConnectionEvent connectionEvent);

    void localTransactionRolledback(ConnectionEvent connectionEvent);

    void connectionErrorOccurred(ConnectionEvent connectionEvent);
}
