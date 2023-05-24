package javax.xml.registry.infomodel;

import java.util.Collection;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/ClassificationScheme.class */
public interface ClassificationScheme extends RegistryEntry {
    public static final int VALUE_TYPE_UNIQUE = 0;
    public static final int VALUE_TYPE_EMBEDDED_PATH = 1;
    public static final int VALUE_TYPE_NON_UNIQUE = 2;

    void addChildConcept(Concept concept) throws JAXRException;

    void addChildConcepts(Collection collection) throws JAXRException;

    void removeChildConcept(Concept concept) throws JAXRException;

    void removeChildConcepts(Collection collection) throws JAXRException;

    int getChildConceptCount() throws JAXRException;

    Collection getChildrenConcepts() throws JAXRException;

    Collection getDescendantConcepts() throws JAXRException;

    boolean isExternal() throws JAXRException;

    int getValueType() throws JAXRException;

    void setValueType(int i) throws JAXRException;
}
