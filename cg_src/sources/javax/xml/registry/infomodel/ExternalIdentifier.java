package javax.xml.registry.infomodel;

import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/ExternalIdentifier.class */
public interface ExternalIdentifier extends RegistryObject {
    RegistryObject getRegistryObject() throws JAXRException;

    String getValue() throws JAXRException;

    void setValue(String str) throws JAXRException;

    ClassificationScheme getIdentificationScheme() throws JAXRException;

    void setIdentificationScheme(ClassificationScheme classificationScheme) throws JAXRException;
}
