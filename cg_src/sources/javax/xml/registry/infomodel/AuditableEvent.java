package javax.xml.registry.infomodel;

import java.sql.Timestamp;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/AuditableEvent.class */
public interface AuditableEvent extends RegistryObject {
    public static final int EVENT_TYPE_CREATED = 0;
    public static final int EVENT_TYPE_DELETED = 1;
    public static final int EVENT_TYPE_DEPRECATED = 2;
    public static final int EVENT_TYPE_UPDATED = 3;
    public static final int EVENT_TYPE_VERSIONED = 4;
    public static final int EVENT_TYPE_UNDEPRECATED = 5;

    User getUser() throws JAXRException;

    Timestamp getTimestamp() throws JAXRException;

    int getEventType() throws JAXRException;

    RegistryObject getRegistryObject() throws JAXRException;
}
