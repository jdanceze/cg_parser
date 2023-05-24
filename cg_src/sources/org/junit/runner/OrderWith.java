package org.junit.runner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.runner.manipulation.Ordering;
import org.junit.validator.ValidateWith;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/OrderWith.class
 */
@Target({ElementType.TYPE})
@ValidateWith(OrderWithValidator.class)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/OrderWith.class */
public @interface OrderWith {
    Class<? extends Ordering.Factory> value();
}
