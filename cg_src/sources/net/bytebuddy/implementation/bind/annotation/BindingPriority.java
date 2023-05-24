package net.bytebuddy.implementation.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/BindingPriority.class */
public @interface BindingPriority {
    public static final int DEFAULT = 1;

    int value();

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/BindingPriority$Resolver.class */
    public enum Resolver implements MethodDelegationBinder.AmbiguityResolver {
        INSTANCE;

        private static int resolve(AnnotationDescription.Loadable<BindingPriority> bindingPriority) {
            if (bindingPriority == null) {
                return 1;
            }
            return bindingPriority.load().value();
        }

        @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.AmbiguityResolver
        public MethodDelegationBinder.AmbiguityResolver.Resolution resolve(MethodDescription source, MethodDelegationBinder.MethodBinding left, MethodDelegationBinder.MethodBinding right) {
            int leftPriority = resolve(left.getTarget().getDeclaredAnnotations().ofType(BindingPriority.class));
            int rightPriority = resolve(right.getTarget().getDeclaredAnnotations().ofType(BindingPriority.class));
            if (leftPriority == rightPriority) {
                return MethodDelegationBinder.AmbiguityResolver.Resolution.AMBIGUOUS;
            }
            if (leftPriority < rightPriority) {
                return MethodDelegationBinder.AmbiguityResolver.Resolution.RIGHT;
            }
            return MethodDelegationBinder.AmbiguityResolver.Resolution.LEFT;
        }
    }
}
