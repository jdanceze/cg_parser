package javax.xml.registry.infomodel;

import java.util.Collection;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/ExternalLink.class */
public interface ExternalLink extends RegistryObject, URIValidator {
    Collection getLinkedObjects() throws JAXRException;

    String getExternalURI() throws JAXRException;

    void setExternalURI(String str) throws JAXRException;
}
