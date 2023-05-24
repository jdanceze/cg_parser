package com.google.errorprone.annotations.concurrent;

import com.google.errorprone.annotations.IncompatibleModifiers;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.lang.model.element.Modifier;
@Target({ElementType.FIELD})
@IncompatibleModifiers({Modifier.FINAL})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:error_prone_annotations-2.5.1.jar:com/google/errorprone/annotations/concurrent/LazyInit.class */
public @interface LazyInit {
}
