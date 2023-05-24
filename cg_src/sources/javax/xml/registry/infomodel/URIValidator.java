package javax.xml.registry.infomodel;

import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/URIValidator.class */
public interface URIValidator {
    void setValidateURI(boolean z) throws JAXRException;

    boolean getValidateURI() throws JAXRException;
}
