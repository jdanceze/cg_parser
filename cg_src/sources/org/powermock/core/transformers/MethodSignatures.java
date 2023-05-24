package org.powermock.core.transformers;

import java.lang.reflect.Method;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.bytebuddy.description.method.MethodDescription;
import org.powermock.PowerMockInternalException;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/MethodSignatures.class */
public enum MethodSignatures {
    ByteBuddy { // from class: org.powermock.core.transformers.MethodSignatures.1
        @Override // org.powermock.core.transformers.MethodSignatures
        public MethodSignatureWriter<MethodDescription> methodSignatureWriter() {
            return new ByteBuddyMethodSignatureWriterWriter();
        }
    },
    Javassist { // from class: org.powermock.core.transformers.MethodSignatures.2
        @Override // org.powermock.core.transformers.MethodSignatures
        public MethodSignatureWriter<CtMethod> methodSignatureWriter() {
            return new JavassistMethodSignatureWriterWriter();
        }
    };

    public abstract <T> MethodSignatureWriter<T> methodSignatureWriter();

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/MethodSignatures$ByteBuddyMethodSignatureWriterWriter.class */
    private static class ByteBuddyMethodSignatureWriterWriter implements MethodSignatureWriter<MethodDescription> {
        private ByteBuddyMethodSignatureWriterWriter() {
        }

        @Override // org.powermock.core.transformers.MethodSignatureWriter
        public String signatureFor(MethodDescription method) {
            return method.toGenericString();
        }

        @Override // org.powermock.core.transformers.MethodSignatureWriter
        public String signatureForReflection(Method method) {
            return method.toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/MethodSignatures$JavassistMethodSignatureWriterWriter.class */
    private static class JavassistMethodSignatureWriterWriter implements MethodSignatureWriter<CtMethod> {
        private JavassistMethodSignatureWriterWriter() {
        }

        @Override // org.powermock.core.transformers.MethodSignatureWriter
        public String signatureFor(CtMethod m) {
            try {
                CtClass[] paramTypes = m.getParameterTypes();
                String[] paramTypeNames = new String[paramTypes.length];
                for (int i = 0; i < paramTypeNames.length; i++) {
                    paramTypeNames[i] = paramTypes[i].getSimpleName();
                }
                return createSignature(m.getDeclaringClass().getSimpleName(), m.getReturnType().getSimpleName(), m.getName(), paramTypeNames);
            } catch (NotFoundException e) {
                throw new PowerMockInternalException(e);
            }
        }

        @Override // org.powermock.core.transformers.MethodSignatureWriter
        public String signatureForReflection(Method m) {
            Class[] paramTypes = m.getParameterTypes();
            String[] paramTypeNames = new String[paramTypes.length];
            for (int i = 0; i < paramTypeNames.length; i++) {
                paramTypeNames[i] = paramTypes[i].getSimpleName();
            }
            return createSignature(m.getDeclaringClass().getSimpleName(), m.getReturnType().getSimpleName(), m.getName(), paramTypeNames);
        }

        private String createSignature(String testClass, String returnType, String methodName, String[] paramTypes) {
            StringBuilder builder = new StringBuilder(testClass).append('\n').append(returnType).append('\n').append(methodName);
            for (String param : paramTypes) {
                builder.append('\n').append(param);
            }
            return builder.toString();
        }
    }
}
