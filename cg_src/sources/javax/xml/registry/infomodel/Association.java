package javax.xml.registry.infomodel;

import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/Association.class */
public interface Association extends RegistryObject {
    RegistryObject getSourceObject() throws JAXRException;

    void setSourceObject(RegistryObject registryObject) throws JAXRException;

    RegistryObject getTargetObject() throws JAXRException;

    void setTargetObject(RegistryObject registryObject) throws JAXRException;

    Concept getAssociationType() throws JAXRException;

    void setAssociationType(Concept concept) throws JAXRException;

    boolean isExtramural() throws JAXRException;

    boolean isConfirmedBySourceOwner() throws JAXRException;

    boolean isConfirmedByTargetOwner() throws JAXRException;

    boolean isConfirmed() throws JAXRException;
}
