package javax.xml.rpc.handler;

import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/handler/GenericHandler.class */
public abstract class GenericHandler implements Handler {
    @Override // javax.xml.rpc.handler.Handler
    public abstract QName[] getHeaders();

    protected GenericHandler() {
    }

    @Override // javax.xml.rpc.handler.Handler
    public boolean handleRequest(MessageContext context) {
        return true;
    }

    @Override // javax.xml.rpc.handler.Handler
    public boolean handleResponse(MessageContext context) {
        return true;
    }

    @Override // javax.xml.rpc.handler.Handler
    public boolean handleFault(MessageContext context) {
        return true;
    }

    @Override // javax.xml.rpc.handler.Handler
    public void init(HandlerInfo config) {
    }

    @Override // javax.xml.rpc.handler.Handler
    public void destroy() {
    }
}
