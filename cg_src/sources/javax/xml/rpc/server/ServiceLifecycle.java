package javax.xml.rpc.server;

import javax.xml.rpc.ServiceException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/server/ServiceLifecycle.class */
public interface ServiceLifecycle {
    void init(Object obj) throws ServiceException;

    void destroy();
}
