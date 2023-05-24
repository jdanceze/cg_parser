package javax.xml.registry.infomodel;

import javax.activation.DataHandler;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/ExtrinsicObject.class */
public interface ExtrinsicObject extends RegistryEntry {
    String getMimeType() throws JAXRException;

    void setMimeType(String str) throws JAXRException;

    boolean isOpaque() throws JAXRException;

    void setOpaque(boolean z) throws JAXRException;

    DataHandler getRepositoryItem() throws JAXRException;

    void setRepositoryItem(DataHandler dataHandler) throws JAXRException;
}
