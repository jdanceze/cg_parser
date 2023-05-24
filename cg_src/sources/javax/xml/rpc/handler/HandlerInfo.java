package javax.xml.rpc.handler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/handler/HandlerInfo.class */
public class HandlerInfo implements Serializable {
    private Class handlerClass;
    private Map config;
    private Vector headers;

    public HandlerInfo() {
        this.handlerClass = null;
        this.config = new HashMap();
        this.headers = new Vector();
    }

    public HandlerInfo(Class handlerClass, Map config, QName[] headers) {
        this.handlerClass = handlerClass;
        this.config = config;
        this.headers = new Vector();
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                this.headers.add(i, headers[i]);
            }
        }
    }

    public void setHandlerClass(Class handlerClass) {
        this.handlerClass = handlerClass;
    }

    public Class getHandlerClass() {
        return this.handlerClass;
    }

    public void setHandlerConfig(Map config) {
        this.config = config;
    }

    public Map getHandlerConfig() {
        return this.config;
    }

    public void setHeaders(QName[] headers) {
        this.headers.clear();
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                this.headers.add(i, headers[i]);
            }
        }
    }

    public QName[] getHeaders() {
        if (this.headers.size() == 0) {
            return null;
        }
        QName[] qns = new QName[this.headers.size()];
        this.headers.copyInto(qns);
        return qns;
    }
}
