package javax.xml.registry.infomodel;

import java.util.Collection;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/Concept.class */
public interface Concept extends RegistryObject {
    String getValue() throws JAXRException;

    void setValue(String str) throws JAXRException;

    void addChildConcept(Concept concept) throws JAXRException;

    void addChildConcepts(Collection collection) throws JAXRException;

    void removeChildConcept(Concept concept) throws JAXRException;

    void removeChildConcepts(Collection collection) throws JAXRException;

    int getChildConceptCount() throws JAXRException;

    Collection getChildrenConcepts() throws JAXRException;

    Collection getDescendantConcepts() throws JAXRException;

    Concept getParentConcept() throws JAXRException;

    ClassificationScheme getClassificationScheme() throws JAXRException;

    String getPath() throws JAXRException;

    RegistryObject getParent() throws JAXRException;
}
