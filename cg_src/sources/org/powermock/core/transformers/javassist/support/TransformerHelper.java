package org.powermock.core.transformers.javassist.support;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.FieldInfo;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/support/TransformerHelper.class */
public class TransformerHelper {
    public static final String VOID = "";

    private static boolean isAccessFlagSynthetic(CtMethod method) {
        int accessFlags = method.getMethodInfo2().getAccessFlags();
        return ((accessFlags & 4096) == 0 || isBridgeMethod(method)) ? false : true;
    }

    private static boolean isBridgeMethod(CtMethod method) {
        return (method.getMethodInfo2().getAccessFlags() & 64) != 0;
    }

    public static String getCorrectReturnValueType(CtClass returnTypeAsCtClass) {
        String returnValue;
        String returnTypeAsString = returnTypeAsCtClass.getName();
        if (returnTypeAsCtClass.equals(CtClass.voidType)) {
            returnValue = "";
        } else if (returnTypeAsCtClass.isPrimitive()) {
            if (returnTypeAsString.equals("char")) {
                returnValue = "((java.lang.Character)value).charValue()";
            } else if (returnTypeAsString.equals("boolean")) {
                returnValue = "((java.lang.Boolean)value).booleanValue()";
            } else {
                returnValue = "((java.lang.Number)value)." + returnTypeAsString + "Value()";
            }
        } else {
            returnValue = "(" + returnTypeAsString + ")value";
        }
        return returnValue;
    }

    public static boolean isNotSyntheticField(FieldInfo fieldInfo) {
        return (fieldInfo.getAccessFlags() & 4096) == 0;
    }

    public static boolean shouldSkipMethod(CtMethod method) {
        return isAccessFlagSynthetic(method) || Modifier.isAbstract(method.getModifiers());
    }

    public static String getReturnTypeAsString(CtMethod method) throws NotFoundException {
        CtClass returnType = method.getReturnType();
        String returnTypeAsString = "";
        if (!returnType.equals(CtClass.voidType)) {
            returnTypeAsString = returnType.getName();
        }
        return returnTypeAsString;
    }

    public static boolean shouldTreatAsSystemClassCall(CtClass declaringClass) {
        String className = declaringClass.getName();
        return className.startsWith("java.");
    }
}
