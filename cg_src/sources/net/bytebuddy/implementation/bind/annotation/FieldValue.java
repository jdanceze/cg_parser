package net.bytebuddy.implementation.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.matcher.ElementMatchers;
@Target({ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldValue.class */
public @interface FieldValue {
    String value() default "";

    Class<?> declaringType() default void.class;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldValue$Binder.class */
    public enum Binder implements TargetMethodAnnotationDrivenBinder.ParameterBinder<FieldValue> {
        INSTANCE(new Delegate());
        
        private static final MethodDescription.InDefinedShape DECLARING_TYPE;
        private static final MethodDescription.InDefinedShape FIELD_NAME;
        private final TargetMethodAnnotationDrivenBinder.ParameterBinder<FieldValue> delegate;

        static {
            MethodList<MethodDescription.InDefinedShape> methodList = TypeDescription.ForLoadedType.of(FieldValue.class).getDeclaredMethods();
            DECLARING_TYPE = (MethodDescription.InDefinedShape) methodList.filter(ElementMatchers.named("declaringType")).getOnly();
            FIELD_NAME = (MethodDescription.InDefinedShape) methodList.filter(ElementMatchers.named("value")).getOnly();
        }

        Binder(TargetMethodAnnotationDrivenBinder.ParameterBinder parameterBinder) {
            this.delegate = parameterBinder;
        }

        @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder
        public Class<FieldValue> getHandledType() {
            return this.delegate.getHandledType();
        }

        @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder
        public MethodDelegationBinder.ParameterBinding<?> bind(AnnotationDescription.Loadable<FieldValue> annotation, MethodDescription source, ParameterDescription target, Implementation.Target implementationTarget, Assigner assigner, Assigner.Typing typing) {
            return this.delegate.bind(annotation, source, target, implementationTarget, assigner, typing);
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/FieldValue$Binder$Delegate.class */
        protected static class Delegate extends TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFieldBinding<FieldValue> {
            protected Delegate() {
            }

            @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder
            public Class<FieldValue> getHandledType() {
                return FieldValue.class;
            }

            @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFieldBinding
            protected String fieldName(AnnotationDescription.Loadable<FieldValue> annotation) {
                return (String) annotation.getValue(Binder.FIELD_NAME).resolve(String.class);
            }

            @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFieldBinding
            protected TypeDescription declaringType(AnnotationDescription.Loadable<FieldValue> annotation) {
                return (TypeDescription) annotation.getValue(Binder.DECLARING_TYPE).resolve(TypeDescription.class);
            }

            @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFieldBinding
            protected MethodDelegationBinder.ParameterBinding<?> bind(FieldDescription fieldDescription, AnnotationDescription.Loadable<FieldValue> annotation, MethodDescription source, ParameterDescription target, Implementation.Target implementationTarget, Assigner assigner) {
                StackManipulation[] stackManipulationArr = new StackManipulation[3];
                stackManipulationArr[0] = fieldDescription.isStatic() ? StackManipulation.Trivial.INSTANCE : MethodVariableAccess.loadThis();
                stackManipulationArr[1] = FieldAccess.forField(fieldDescription).read();
                stackManipulationArr[2] = assigner.assign(fieldDescription.getType(), target.getType(), RuntimeType.Verifier.check(target));
                StackManipulation stackManipulation = new StackManipulation.Compound(stackManipulationArr);
                return stackManipulation.isValid() ? new MethodDelegationBinder.ParameterBinding.Anonymous(stackManipulation) : MethodDelegationBinder.ParameterBinding.Illegal.INSTANCE;
            }
        }
    }
}
