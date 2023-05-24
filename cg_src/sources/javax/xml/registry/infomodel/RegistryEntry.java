package javax.xml.registry.infomodel;

import java.util.Date;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/RegistryEntry.class */
public interface RegistryEntry extends RegistryObject, Versionable {
    public static final int STATUS_SUBMITTED = 0;
    public static final int STATUS_APPROVED = 1;
    public static final int STATUS_DEPRECATED = 2;
    public static final int STATUS_WITHDRAWN = 3;
    public static final int STABILITY_DYNAMIC = 0;
    public static final int STABILITY_DYNAMIC_COMPATIBLE = 1;
    public static final int STABILITY_STATIC = 2;

    int getStatus() throws JAXRException;

    int getStability() throws JAXRException;

    void setStability(int i) throws JAXRException;

    Date getExpiration() throws JAXRException;

    void setExpiration(Date date) throws JAXRException;
}
