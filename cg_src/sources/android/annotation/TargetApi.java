package android.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.CLASS)
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/annotation/TargetApi.class */
public @interface TargetApi {
    int value();
}
