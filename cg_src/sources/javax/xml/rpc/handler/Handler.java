package javax.xml.rpc.handler;

import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/handler/Handler.class */
public interface Handler {
    boolean handleRequest(MessageContext messageContext);

    boolean handleResponse(MessageContext messageContext);

    boolean handleFault(MessageContext messageContext);

    void init(HandlerInfo handlerInfo);

    void destroy();

    QName[] getHeaders();
}
