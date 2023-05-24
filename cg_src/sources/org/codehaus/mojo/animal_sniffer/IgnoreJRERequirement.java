package org.codehaus.mojo.animal_sniffer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Documented
/* loaded from: gencallgraphv3.jar:animal-sniffer-annotations-1.17.jar:org/codehaus/mojo/animal_sniffer/IgnoreJRERequirement.class */
public @interface IgnoreJRERequirement {
}
