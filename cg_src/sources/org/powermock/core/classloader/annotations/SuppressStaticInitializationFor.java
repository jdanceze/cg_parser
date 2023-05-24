package org.powermock.core.classloader.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.powermock.core.classloader.ByteCodeFramework;
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/classloader/annotations/SuppressStaticInitializationFor.class */
public @interface SuppressStaticInitializationFor {
    String[] value() default {""};

    ByteCodeFramework byteCodeFramework() default ByteCodeFramework.Javassist;
}
