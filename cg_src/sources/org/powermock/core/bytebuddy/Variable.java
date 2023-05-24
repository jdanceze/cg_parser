package org.powermock.core.bytebuddy;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/bytebuddy/Variable.class */
public class Variable {
    private final TypeDescription typeDefinitions;
    private final int offset;

    public static Variable of(TypeDescription.Generic variableType, int offset) {
        return new Variable(variableType.asErasure(), offset);
    }

    private Variable(TypeDescription typeDefinitions, int offset) {
        this.typeDefinitions = typeDefinitions;
        this.offset = offset;
    }

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/bytebuddy/Variable$VariableAccess.class */
    public static class VariableAccess {
        public static StackManipulation load(Variable variable) {
            return load(variable, false);
        }

        public static StackManipulation load(Variable variable, boolean boxing) {
            TypeDescription typeDefinitions = variable.typeDefinitions;
            if (!typeDefinitions.isPrimitive() || !boxing) {
                return MethodVariableAccess.of(typeDefinitions).loadFrom(variable.offset);
            }
            return new StackManipulation.Compound(MethodVariableAccess.of(typeDefinitions).loadFrom(variable.offset), PrimitiveBoxing.forPrimitive(typeDefinitions));
        }

        public static StackManipulation store(Variable variable) {
            return MethodVariableAccess.of(variable.typeDefinitions).storeAt(variable.offset);
        }
    }
}
