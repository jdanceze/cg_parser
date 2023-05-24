package net.bytebuddy.implementation.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.utility.CompoundList;
@Target({ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/AllArguments.class */
public @interface AllArguments {
    Assignment value() default Assignment.STRICT;

    boolean includeSelf() default false;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/AllArguments$Assignment.class */
    public enum Assignment {
        STRICT(true),
        SLACK(false);
        
        private final boolean strict;

        Assignment(boolean strict) {
            this.strict = strict;
        }

        protected boolean isStrict() {
            return this.strict;
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/annotation/AllArguments$Binder.class */
    public enum Binder implements TargetMethodAnnotationDrivenBinder.ParameterBinder<AllArguments> {
        INSTANCE;

        @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder
        public Class<AllArguments> getHandledType() {
            return AllArguments.class;
        }

        @Override // net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder
        public MethodDelegationBinder.ParameterBinding<?> bind(AnnotationDescription.Loadable<AllArguments> annotation, MethodDescription source, ParameterDescription target, Implementation.Target implementationTarget, Assigner assigner, Assigner.Typing typing) {
            TypeDescription.Generic componentType;
            List<TypeDescription.Generic> asTypeList;
            if (target.getType().represents(Object.class)) {
                componentType = TypeDescription.Generic.OBJECT;
            } else if (target.getType().isArray()) {
                componentType = target.getType().getComponentType();
            } else {
                throw new IllegalStateException("Expected an array type for all argument annotation on " + source);
            }
            boolean includeThis = !source.isStatic() && annotation.load().includeSelf();
            List<StackManipulation> stackManipulations = new ArrayList<>(source.getParameters().size() + (includeThis ? 1 : 0));
            int offset = (source.isStatic() || includeThis) ? 0 : 1;
            if (includeThis) {
                asTypeList = CompoundList.of(implementationTarget.getInstrumentedType().asGenericType(), source.getParameters().asTypeList());
            } else {
                asTypeList = source.getParameters().asTypeList();
            }
            for (TypeDescription.Generic sourceParameter : asTypeList) {
                StackManipulation stackManipulation = new StackManipulation.Compound(MethodVariableAccess.of(sourceParameter).loadFrom(offset), assigner.assign(sourceParameter, componentType, typing));
                if (stackManipulation.isValid()) {
                    stackManipulations.add(stackManipulation);
                } else if (annotation.load().value().isStrict()) {
                    return MethodDelegationBinder.ParameterBinding.Illegal.INSTANCE;
                }
                offset += sourceParameter.getStackSize().getSize();
            }
            return new MethodDelegationBinder.ParameterBinding.Anonymous(ArrayFactory.forType(componentType).withValues(stackManipulations));
        }
    }
}
