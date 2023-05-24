package javax.xml.soap;

import java.util.Locale;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/SOAPFault.class */
public interface SOAPFault extends SOAPBodyElement {
    void setFaultCode(Name name) throws SOAPException;

    void setFaultCode(String str) throws SOAPException;

    Name getFaultCodeAsName();

    String getFaultCode();

    void setFaultActor(String str) throws SOAPException;

    String getFaultActor();

    void setFaultString(String str) throws SOAPException;

    void setFaultString(String str, Locale locale) throws SOAPException;

    String getFaultString();

    Locale getFaultStringLocale();

    Detail getDetail();

    Detail addDetail() throws SOAPException;
}
