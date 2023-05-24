package javax.xml.registry.infomodel;

import java.util.Collection;
import javax.xml.registry.JAXRException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/infomodel/Slot.class */
public interface Slot {
    public static final String SORT_CODE_SLOT = "sortCode";
    public static final String ADDRESS_LINES_SLOT = "addressLines";
    public static final String AUTHORIZED_NAME_SLOT = "authorizedName";
    public static final String OPERATOR_SLOT = "operator";

    String getName() throws JAXRException;

    void setName(String str) throws JAXRException;

    String getSlotType() throws JAXRException;

    void setSlotType(String str) throws JAXRException;

    Collection getValues() throws JAXRException;

    void setValues(Collection collection) throws JAXRException;
}
