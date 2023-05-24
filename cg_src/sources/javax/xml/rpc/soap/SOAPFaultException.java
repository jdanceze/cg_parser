package javax.xml.rpc.soap;

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/soap/SOAPFaultException.class */
public class SOAPFaultException extends RuntimeException {
    private QName faultcode;
    private String faultstring;
    private String faultactor;
    private Detail detail;

    public SOAPFaultException(QName faultcode, String faultstring, String faultactor, Detail faultdetail) {
        super(faultstring);
        this.faultcode = faultcode;
        this.faultstring = faultstring;
        this.faultactor = faultactor;
        this.detail = faultdetail;
    }

    public QName getFaultCode() {
        return this.faultcode;
    }

    public String getFaultString() {
        return this.faultstring;
    }

    public String getFaultActor() {
        return this.faultactor;
    }

    public Detail getDetail() {
        return this.detail;
    }
}
