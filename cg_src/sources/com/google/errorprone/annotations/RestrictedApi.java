package com.google.errorprone.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
/* loaded from: gencallgraphv3.jar:error_prone_annotations-2.5.1.jar:com/google/errorprone/annotations/RestrictedApi.class */
public @interface RestrictedApi {
    String explanation();

    String link();

    String allowedOnPath() default "";

    Class<? extends Annotation>[] whitelistAnnotations() default {};

    Class<? extends Annotation>[] whitelistWithWarningAnnotations() default {};
}
