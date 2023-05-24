package javax.xml.rpc.handler;

import java.io.Serializable;
import java.util.List;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/handler/HandlerRegistry.class */
public interface HandlerRegistry extends Serializable {
    List getHandlerChain(QName qName);

    void setHandlerChain(QName qName, List list);
}
