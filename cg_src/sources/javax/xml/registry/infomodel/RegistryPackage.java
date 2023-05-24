package javax.xml.registry.infomodel;

import java.util.Collection;
import java.util.Set;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/RegistryPackage.class */
public interface RegistryPackage extends RegistryEntry {
    void addRegistryObject(RegistryObject registryObject) throws JAXRException;

    void addRegistryObjects(Collection collection) throws JAXRException;

    void removeRegistryObject(RegistryObject registryObject) throws JAXRException;

    void removeRegistryObjects(Collection collection) throws JAXRException;

    Set getRegistryObjects() throws JAXRException;
}
