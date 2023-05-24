package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/annotation/XmlSchemaTypes.class */
public @interface XmlSchemaTypes {
    XmlSchemaType[] value();
}
