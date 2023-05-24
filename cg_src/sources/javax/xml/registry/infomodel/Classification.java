package javax.xml.registry.infomodel;

import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/Classification.class */
public interface Classification extends RegistryObject {
    Concept getConcept() throws JAXRException;

    void setConcept(Concept concept) throws JAXRException;

    ClassificationScheme getClassificationScheme() throws JAXRException;

    void setClassificationScheme(ClassificationScheme classificationScheme) throws JAXRException;

    String getValue() throws JAXRException;

    void setValue(String str) throws JAXRException;

    RegistryObject getClassifiedObject() throws JAXRException;

    void setClassifiedObject(RegistryObject registryObject) throws JAXRException;

    boolean isExternal() throws JAXRException;
}
