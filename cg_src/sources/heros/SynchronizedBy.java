package heros;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
@Target({ElementType.FIELD})
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/SynchronizedBy.class */
public @interface SynchronizedBy {
    String value() default "";
}
