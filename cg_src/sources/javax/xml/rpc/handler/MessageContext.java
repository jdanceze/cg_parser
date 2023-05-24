package javax.xml.rpc.handler;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/handler/MessageContext.class */
public interface MessageContext {
    void setProperty(String str, Object obj);

    Object getProperty(String str);

    void removeProperty(String str);

    boolean containsProperty(String str);

    Iterator getPropertyNames();
}
