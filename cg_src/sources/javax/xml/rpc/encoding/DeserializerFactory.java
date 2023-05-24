package javax.xml.rpc.encoding;

import java.io.Serializable;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/encoding/DeserializerFactory.class */
public interface DeserializerFactory extends Serializable {
    Deserializer getDeserializerAs(String str);

    Iterator getSupportedMechanismTypes();
}
