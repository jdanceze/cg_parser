package net.bytebuddy.dynamic.loading;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/NoOpClassFileTransformer.class */
public enum NoOpClassFileTransformer implements ClassFileTransformer {
    INSTANCE;
    
    private static final byte[] NO_TRANSFORMATION = null;

    @SuppressFBWarnings(value = {"EI_EXPOSE_REP"}, justification = "Array is guaranteed to be null")
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        return NO_TRANSFORMATION;
    }
}
