package org.powermock.core.bytebuddy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.method.ParameterList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.constant.ClassConstant;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.jar.asm.MethodVisitor;
import org.powermock.core.bytebuddy.Variable;
import org.powermock.reflect.internal.WhiteboxImpl;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/bytebuddy/MockGetawayCall.class */
public class MockGetawayCall {
    private final Method getawayMethod;

    public MockGetawayCall(Class mockGetawayClass) {
        this.getawayMethod = WhiteboxImpl.getMethod(mockGetawayClass, "suppressConstructorCall", Class.class, Object[].class, Class[].class);
    }

    public ForType forType(TypeDescription targetType) {
        return new ForType(targetType);
    }

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/bytebuddy/MockGetawayCall$ForType.class */
    public static class ForType {
        private final TypeDescription targetType;
        private final MockGetawayCall mockGetawayCall;

        private ForType(MockGetawayCall mockGetawayCall, TypeDescription targetType) {
            this.mockGetawayCall = mockGetawayCall;
            this.targetType = targetType;
        }

        public WithArguments withArguments(List<Variable> parameters) {
            return new WithArguments(this, parameters);
        }
    }

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/bytebuddy/MockGetawayCall$WithArguments.class */
    public static class WithArguments {
        private final ForType forType;
        private final List<Variable> arguments;

        private WithArguments(ForType forType, List<Variable> arguments) {
            this.forType = forType;
            this.arguments = arguments;
        }

        public ConstructorMockGetawayCall withParameterTypes(ParameterList<ParameterDescription.InDefinedShape> targetParameters) {
            return new ConstructorMockGetawayCall(this.forType.mockGetawayCall.getawayMethod, this.forType.targetType, this.arguments, targetParameters);
        }
    }

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/bytebuddy/MockGetawayCall$ConstructorMockGetawayCall.class */
    private static class ConstructorMockGetawayCall implements StackManipulation {
        private final Method getawayMethod;
        private final TypeDescription targetType;
        private final List<Variable> arguments;
        private final ParameterList<ParameterDescription.InDefinedShape> targetParameters;

        private ConstructorMockGetawayCall(Method getawayMethod, TypeDescription targetType, List<Variable> arguments, ParameterList<ParameterDescription.InDefinedShape> targetParameters) {
            this.getawayMethod = getawayMethod;
            this.targetType = targetType;
            this.arguments = arguments;
            this.targetParameters = targetParameters;
        }

        private List<StackManipulation> loadSignatureParametersClasess() {
            List<StackManipulation> constructorSignature = new ArrayList<>();
            for (ParameterDescription.InDefinedShape targetParameter : this.targetParameters) {
                constructorSignature.add(ClassConstant.of(targetParameter.getType().asErasure()));
            }
            return constructorSignature;
        }

        private List<StackManipulation> loadArgumentsFromVariable() {
            List<StackManipulation> loadTargetParameters = new ArrayList<>();
            for (Variable argument : this.arguments) {
                loadTargetParameters.add(Variable.VariableAccess.load(argument, true));
            }
            return loadTargetParameters;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public boolean isValid() {
            return true;
        }

        @Override // net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor mv, Implementation.Context implementationContext) {
            List<StackManipulation> loadTargetParameters = loadArgumentsFromVariable();
            List<StackManipulation> constructorSignature = loadSignatureParametersClasess();
            return new StackManipulation.Compound(ClassConstant.of(this.targetType), ArrayFactory.forType(TypeDescription.OBJECT.asGenericType()).withValues(loadTargetParameters), ArrayFactory.forType(TypeDescription.CLASS.asGenericType()).withValues(constructorSignature), MethodInvocation.invoke((MethodDescription.InDefinedShape) new MethodDescription.ForLoadedMethod(this.getawayMethod))).apply(mv, implementationContext);
        }
    }
}
