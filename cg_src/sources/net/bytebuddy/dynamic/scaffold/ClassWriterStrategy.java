package net.bytebuddy.dynamic.scaffold;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.jar.asm.ClassReader;
import net.bytebuddy.jar.asm.ClassWriter;
import net.bytebuddy.pool.TypePool;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/ClassWriterStrategy.class */
public interface ClassWriterStrategy {
    ClassWriter resolve(int i, TypePool typePool);

    ClassWriter resolve(int i, TypePool typePool, ClassReader classReader);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/ClassWriterStrategy$Default.class */
    public enum Default implements ClassWriterStrategy {
        CONSTANT_POOL_RETAINING { // from class: net.bytebuddy.dynamic.scaffold.ClassWriterStrategy.Default.1
            @Override // net.bytebuddy.dynamic.scaffold.ClassWriterStrategy
            public ClassWriter resolve(int flags, TypePool typePool, ClassReader classReader) {
                return new FrameComputingClassWriter(classReader, flags, typePool);
            }
        },
        CONSTANT_POOL_DISCARDING { // from class: net.bytebuddy.dynamic.scaffold.ClassWriterStrategy.Default.2
            @Override // net.bytebuddy.dynamic.scaffold.ClassWriterStrategy
            public ClassWriter resolve(int flags, TypePool typePool, ClassReader classReader) {
                return resolve(flags, typePool);
            }
        };

        @Override // net.bytebuddy.dynamic.scaffold.ClassWriterStrategy
        public ClassWriter resolve(int flags, TypePool typePool) {
            return new FrameComputingClassWriter(flags, typePool);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/ClassWriterStrategy$FrameComputingClassWriter.class */
    public static class FrameComputingClassWriter extends ClassWriter {
        private final TypePool typePool;

        public FrameComputingClassWriter(int flags, TypePool typePool) {
            super(flags);
            this.typePool = typePool;
        }

        public FrameComputingClassWriter(ClassReader classReader, int flags, TypePool typePool) {
            super(classReader, flags);
            this.typePool = typePool;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // net.bytebuddy.jar.asm.ClassWriter
        public String getCommonSuperClass(String leftTypeName, String rightTypeName) {
            TypeDescription leftType = this.typePool.describe(leftTypeName.replace('/', '.')).resolve();
            TypeDescription rightType = this.typePool.describe(rightTypeName.replace('/', '.')).resolve();
            if (leftType.isAssignableFrom(rightType)) {
                return leftType.getInternalName();
            }
            if (leftType.isAssignableTo(rightType)) {
                return rightType.getInternalName();
            }
            if (leftType.isInterface() || rightType.isInterface()) {
                return TypeDescription.OBJECT.getInternalName();
            }
            do {
                leftType = leftType.getSuperClass().asErasure();
            } while (!leftType.isAssignableFrom(rightType));
            return leftType.getInternalName();
        }
    }
}
