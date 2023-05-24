package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/annotation/XmlType.class */
public @interface XmlType {

    /* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/annotation/XmlType$DEFAULT.class */
    public static final class DEFAULT {
    }

    String name() default "##default";

    String[] propOrder() default {""};

    String namespace() default "##default";

    Class factoryClass() default DEFAULT.class;

    String factoryMethod() default "";
}
