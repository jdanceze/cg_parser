package javax.xml.rpc.handler;

import java.util.List;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/handler/HandlerChain.class */
public interface HandlerChain extends List {
    boolean handleRequest(MessageContext messageContext);

    boolean handleResponse(MessageContext messageContext);

    boolean handleFault(MessageContext messageContext);

    void init(Map map);

    void destroy();

    void setRoles(String[] strArr);

    String[] getRoles();
}
