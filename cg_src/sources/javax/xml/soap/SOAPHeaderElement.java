package javax.xml.soap;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/SOAPHeaderElement.class */
public interface SOAPHeaderElement extends SOAPElement {
    void setActor(String str);

    String getActor();

    void setMustUnderstand(boolean z);

    boolean getMustUnderstand();
}
