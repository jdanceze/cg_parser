package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/annotation/XmlSchema.class */
public @interface XmlSchema {
    public static final String NO_LOCATION = "##generate";

    XmlNs[] xmlns() default {};

    String namespace() default "";

    XmlNsForm elementFormDefault() default XmlNsForm.UNSET;

    XmlNsForm attributeFormDefault() default XmlNsForm.UNSET;

    String location() default "##generate";
}
