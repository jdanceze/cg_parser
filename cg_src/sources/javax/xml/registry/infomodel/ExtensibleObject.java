package javax.xml.registry.infomodel;

import java.util.Collection;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/ExtensibleObject.class */
public interface ExtensibleObject {
    void addSlot(Slot slot) throws JAXRException;

    void addSlots(Collection collection) throws JAXRException;

    void removeSlot(String str) throws JAXRException;

    void removeSlots(Collection collection) throws JAXRException;

    Slot getSlot(String str) throws JAXRException;

    Collection getSlots() throws JAXRException;
}
