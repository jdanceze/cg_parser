package net.bytebuddy.implementation.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bind.ArgumentTypeResolver;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
@Target({ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/Argument.class */
public @interface Argument {

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/Argument$BindingMechanic.class */
    public enum BindingMechanic {
        UNIQUE { // from class: net.bytebuddy.implementation.bind.annotation.Argument.BindingMechanic.1
            @Override // net.bytebuddy.implementation.bind.annotation.Argument.BindingMechanic
            protected MethodDelegationBinder.ParameterBinding<?> makeBinding(TypeDescription.Generic source, TypeDescription.Generic target, int sourceParameterIndex, Assigner assigner, Assigner.Typing typing, int parameterOffset) {
                return MethodDelegationBinder.ParameterBinding.Unique.of(new StackManipulation.Compound(MethodVariableAccess.of(source).loadFrom(parameterOffset), assigner.assign(source, target, typing)), new ArgumentTypeResolver.ParameterIndexToken(sourceParameterIndex));
            }
        },
        ANONYMOUS { // from class: net.bytebuddy.implementation.bind.annotation.Argument.BindingMechanic.2
            @Override // net.bytebuddy.implementation.bind.annotation.Argument.BindingMechanic
            protected MethodDelegationBinder.ParameterBinding<?> makeBinding(TypeDescription.Generic source, TypeDescription.Generic target, int sourceParameterIndex, Assigner assigner, Assigner.Typing typing, int parameterOffset) {
                return new MethodDelegationBinder.ParameterBinding.Anonymous(new StackManipulation.Compound(MethodVariableAccess.of(source).loadFrom(parameterOffset), assigner.assign(source, target, typing)));
            }
        };

        protected abstract MethodDelegationBinder.ParameterBinding<?> makeBinding(TypeDescription.Generic generic, TypeDescription.Generic generic2, int i, Assigner assigner, Assigner.Typing typing, int i2);
    }

    int value();

    BindingMechanic bindingMechanic() default BindingMechanic.UNIQUE;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/Argument$Binder.class */
    public enum Binder implements TargetMethodAnnotationDrivenBinder.ParameterBinder<Argument> {
        INSTANCE;

        @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder
        public Class<Argument> getHandledType() {
            return Argument.class;
        }

        @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder
        public MethodDelegationBinder.ParameterBinding<?> bind(AnnotationDescription.Loadable<Argument> annotation, MethodDescription source, ParameterDescription target, Implementation.Target implementationTarget, Assigner assigner, Assigner.Typing typing) {
            Argument argument = annotation.load();
            if (argument.value() < 0) {
                throw new IllegalArgumentException("@Argument annotation on " + target + " specifies negative index");
            }
            if (source.getParameters().size() <= argument.value()) {
                return MethodDelegationBinder.ParameterBinding.Illegal.INSTANCE;
            }
            return argument.bindingMechanic().makeBinding(((ParameterDescription) source.getParameters().get(argument.value())).getType(), target.getType(), argument.value(), assigner, typing, ((ParameterDescription) source.getParameters().get(argument.value())).getOffset());
        }
    }
}
