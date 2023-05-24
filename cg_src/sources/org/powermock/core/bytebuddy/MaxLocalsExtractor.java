package org.powermock.core.bytebuddy;

import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/bytebuddy/MaxLocalsExtractor.class */
public class MaxLocalsExtractor extends ClassVisitor {
    private MethodMaxLocals methodMaxLocals;

    public MaxLocalsExtractor() {
        super(327680);
    }

    @Override // net.bytebuddy.jar.asm.ClassVisitor
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if ("<init>".equals(name)) {
            this.methodMaxLocals = new MethodMaxLocals();
            return new MaxLocalsMethodVisitor(name, desc, this.methodMaxLocals);
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    public MethodMaxLocals getMethods() {
        return this.methodMaxLocals;
    }

    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/bytebuddy/MaxLocalsExtractor$MaxLocalsMethodVisitor.class */
    private static class MaxLocalsMethodVisitor extends MethodVisitor {
        private final String name;
        private final String signature;
        private final MethodMaxLocals methodMaxLocals;
        private int maxLocals;

        private MaxLocalsMethodVisitor(String name, String signature, MethodMaxLocals methodMaxLocals) {
            super(327680);
            this.name = name;
            this.signature = signature;
            this.methodMaxLocals = methodMaxLocals;
        }

        @Override // net.bytebuddy.jar.asm.MethodVisitor
        public void visitMaxs(int maxStack, int maxLocals) {
            this.maxLocals = maxLocals;
            super.visitMaxs(maxStack, maxLocals);
        }

        @Override // net.bytebuddy.jar.asm.MethodVisitor
        public void visitEnd() {
            this.methodMaxLocals.add(this.name, this.signature, this.maxLocals);
            super.visitEnd();
        }
    }
}
