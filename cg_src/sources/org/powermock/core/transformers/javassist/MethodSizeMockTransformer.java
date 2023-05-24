package org.powermock.core.transformers.javassist;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import org.powermock.core.transformers.TransformStrategy;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/MethodSizeMockTransformer.class */
public class MethodSizeMockTransformer extends MethodMockTransformer {
    private static final int MAX_METHOD_CODE_LENGTH_LIMIT = 65536;

    public MethodSizeMockTransformer(TransformStrategy strategy) {
        super(strategy);
    }

    @Override // org.powermock.core.transformers.javassist.AbstractJavaAssistMockTransformer
    public CtClass transform(CtClass clazz) throws CannotCompileException, NotFoundException {
        CtMethod[] declaredMethods;
        for (CtMethod method : clazz.getDeclaredMethods()) {
            if (isMethodSizeExceeded(method)) {
                method.setBody("{throw new IllegalAccessException(\"Method was too large and after instrumentation exceeded JVM limit. PowerMock modified the method to allow JVM to load the class. You can use PowerMock API to suppress or mock this method behaviour.\");}");
                modifyMethod(method);
            }
        }
        return clazz;
    }

    private boolean isMethodSizeExceeded(CtMethod method) {
        CodeAttribute codeAttribute = method.getMethodInfo().getCodeAttribute();
        return codeAttribute != null && codeAttribute.getCodeLength() >= 65536;
    }
}
