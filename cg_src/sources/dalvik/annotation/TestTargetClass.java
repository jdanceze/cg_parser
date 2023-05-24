package dalvik.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:dalvik/annotation/TestTargetClass.class */
public @interface TestTargetClass {
    Class<?> value();
}
