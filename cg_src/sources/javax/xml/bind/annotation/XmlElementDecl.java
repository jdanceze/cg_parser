package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/annotation/XmlElementDecl.class */
public @interface XmlElementDecl {

    /* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/annotation/XmlElementDecl$GLOBAL.class */
    public static final class GLOBAL {
    }

    Class scope() default GLOBAL.class;

    String namespace() default "##default";

    String name();

    String substitutionHeadNamespace() default "##default";

    String substitutionHeadName() default "";

    String defaultValue() default "��";
}
