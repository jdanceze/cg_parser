package com.google.errorprone.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.lang.model.element.Modifier;
@Target({ElementType.METHOD})
@IncompatibleModifiers({Modifier.PUBLIC, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL})
@Documented
@Retention(RetentionPolicy.CLASS)
/* loaded from: gencallgraphv3.jar:error_prone_annotations-2.5.1.jar:com/google/errorprone/annotations/ForOverride.class */
public @interface ForOverride {
}
