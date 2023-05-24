package javax.xml.bind.annotation.adapters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.PACKAGE, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/annotation/adapters/XmlJavaTypeAdapter.class */
public @interface XmlJavaTypeAdapter {

    /* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/annotation/adapters/XmlJavaTypeAdapter$DEFAULT.class */
    public static final class DEFAULT {
    }

    Class<? extends XmlAdapter> value();

    Class type() default DEFAULT.class;
}
