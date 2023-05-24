package javax.ejb;

import javax.xml.rpc.handler.MessageContext;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/SessionContext.class */
public interface SessionContext extends EJBContext {
    EJBLocalObject getEJBLocalObject() throws IllegalStateException;

    EJBObject getEJBObject() throws IllegalStateException;

    MessageContext getMessageContext() throws IllegalStateException;
}
