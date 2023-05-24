package org.powermock.core.transformers.javassist;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.compiler.JvstCodeGen;
import org.powermock.core.MockGateway;
import org.powermock.core.transformers.TransformStrategy;
import org.powermock.core.transformers.javassist.support.TransformerHelper;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/MethodMockTransformer.class */
abstract class MethodMockTransformer extends AbstractJavaAssistMockTransformer {
    private Class<?> mockGetawayClass;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MethodMockTransformer(TransformStrategy strategy) {
        super(strategy);
        this.mockGetawayClass = MockGateway.class;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void modifyMethod(CtMethod method) throws NotFoundException, CannotCompileException {
        if (!TransformerHelper.shouldSkipMethod(method)) {
            CtClass returnTypeAsCtClass = method.getReturnType();
            String returnTypeAsString = TransformerHelper.getReturnTypeAsString(method);
            if (Modifier.isNative(method.getModifiers())) {
                modifyNativeMethod(method, returnTypeAsCtClass, returnTypeAsString);
            } else {
                modifyMethod(method, returnTypeAsCtClass, returnTypeAsString);
            }
        }
    }

    private void modifyNativeMethod(CtMethod method, CtClass returnTypeAsCtClass, String returnTypeAsString) throws CannotCompileException {
        String methodName = method.getName();
        String returnValue = "($r)value";
        if (returnTypeAsCtClass.equals(CtClass.voidType)) {
            returnValue = "";
        }
        String classOrInstance = classOrInstance(method);
        method.setModifiers(method.getModifiers() - 256);
        String code = "Object value = " + this.mockGetawayClass.getName() + ".methodCall(" + classOrInstance + ", \"" + method.getName() + "\", $args, $sig, \"" + returnTypeAsString + "\");if (value != " + MockGateway.class.getName() + ".PROCEED) return " + returnValue + "; throw new java.lang.UnsupportedOperationException(\"" + methodName + " is native\");";
        method.setBody("{" + code + "}");
    }

    private String classOrInstance(CtMethod method) {
        String classOrInstance = "this";
        if (Modifier.isStatic(method.getModifiers())) {
            classOrInstance = JvstCodeGen.clazzName;
        }
        return classOrInstance;
    }

    private void modifyMethod(CtMethod method, CtClass returnTypeAsCtClass, String returnTypeAsString) throws CannotCompileException {
        String returnValue = TransformerHelper.getCorrectReturnValueType(returnTypeAsCtClass);
        String classOrInstance = classOrInstance(method);
        String code = "Object value = " + this.mockGetawayClass.getName() + ".methodCall(" + classOrInstance + ", \"" + method.getName() + "\", $args, $sig, \"" + returnTypeAsString + "\");if (value != " + MockGateway.class.getName() + ".PROCEED) return " + returnValue + "; ";
        method.insertBefore("{ " + code + "}");
    }
}
