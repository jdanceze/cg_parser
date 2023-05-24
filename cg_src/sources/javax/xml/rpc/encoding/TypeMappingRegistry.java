package javax.xml.rpc.encoding;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/encoding/TypeMappingRegistry.class */
public interface TypeMappingRegistry extends Serializable {
    TypeMapping register(String str, TypeMapping typeMapping);

    void registerDefault(TypeMapping typeMapping);

    TypeMapping getDefaultTypeMapping();

    String[] getRegisteredEncodingStyleURIs();

    TypeMapping getTypeMapping(String str);

    TypeMapping createTypeMapping();

    TypeMapping unregisterTypeMapping(String str);

    boolean removeTypeMapping(TypeMapping typeMapping);

    void clear();
}
