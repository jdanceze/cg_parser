package net.bytebuddy.implementation.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.constant.ClassConstant;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.constant.JavaConstantValue;
import net.bytebuddy.implementation.bytecode.constant.MethodConstant;
import net.bytebuddy.implementation.bytecode.constant.TextConstant;
import net.bytebuddy.utility.JavaConstant;
import net.bytebuddy.utility.JavaType;
@Target({ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/Origin.class */
public @interface Origin {
    boolean cache() default true;

    boolean privileged() default false;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/Origin$Binder.class */
    public enum Binder implements TargetMethodAnnotationDrivenBinder.ParameterBinder<Origin> {
        INSTANCE;

        private static StackManipulation methodConstant(Origin origin, MethodDescription.InDefinedShape methodDescription) {
            MethodConstant.CanCache of;
            if (origin.privileged()) {
                of = MethodConstant.ofPrivileged(methodDescription);
            } else {
                of = MethodConstant.of(methodDescription);
            }
            MethodConstant.CanCache methodConstant = of;
            return origin.cache() ? methodConstant.cached() : methodConstant;
        }

        @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder
        public Class<Origin> getHandledType() {
            return Origin.class;
        }

        @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder
        public MethodDelegationBinder.ParameterBinding<?> bind(AnnotationDescription.Loadable<Origin> annotation, MethodDescription source, ParameterDescription target, Implementation.Target implementationTarget, Assigner assigner, Assigner.Typing typing) {
            TypeDescription parameterType = target.getType().asErasure();
            if (parameterType.represents(Class.class)) {
                return new MethodDelegationBinder.ParameterBinding.Anonymous(ClassConstant.of(implementationTarget.getOriginType().asErasure()));
            }
            if (parameterType.represents(Method.class)) {
                return source.isMethod() ? new MethodDelegationBinder.ParameterBinding.Anonymous(methodConstant(annotation.load(), source.asDefined())) : MethodDelegationBinder.ParameterBinding.Illegal.INSTANCE;
            } else if (parameterType.represents(Constructor.class)) {
                return source.isConstructor() ? new MethodDelegationBinder.ParameterBinding.Anonymous(methodConstant(annotation.load(), source.asDefined())) : MethodDelegationBinder.ParameterBinding.Illegal.INSTANCE;
            } else if (JavaType.EXECUTABLE.getTypeStub().equals(parameterType)) {
                return new MethodDelegationBinder.ParameterBinding.Anonymous(methodConstant(annotation.load(), source.asDefined()));
            } else {
                if (parameterType.represents(String.class)) {
                    return new MethodDelegationBinder.ParameterBinding.Anonymous(new TextConstant(source.toString()));
                }
                if (parameterType.represents(Integer.TYPE)) {
                    return new MethodDelegationBinder.ParameterBinding.Anonymous(IntegerConstant.forValue(source.getModifiers()));
                }
                if (parameterType.equals(JavaType.METHOD_HANDLE.getTypeStub())) {
                    return new MethodDelegationBinder.ParameterBinding.Anonymous(new JavaConstantValue(JavaConstant.MethodHandle.of(source.asDefined())));
                }
                if (parameterType.equals(JavaType.METHOD_TYPE.getTypeStub())) {
                    return new MethodDelegationBinder.ParameterBinding.Anonymous(new JavaConstantValue(JavaConstant.MethodType.of(source.asDefined())));
                }
                throw new IllegalStateException("The " + target + " method's " + target.getIndex() + " parameter is annotated with a Origin annotation with an argument not representing a Class, Method, Constructor, String, int, MethodType or MethodHandle type");
            }
        }
    }
}
