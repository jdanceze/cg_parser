package net.bytebuddy.utility;

import java.security.AccessController;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.jar.asm.ClassReader;
import net.bytebuddy.utility.privilege.GetSystemPropertyAction;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/OpenedClassReader.class */
public class OpenedClassReader {
    public static final String EXPERIMENTAL_PROPERTY = "net.bytebuddy.experimental";
    public static final boolean EXPERIMENTAL;
    public static final int ASM_API;

    static {
        boolean experimental;
        try {
            experimental = Boolean.parseBoolean((String) AccessController.doPrivileged(new GetSystemPropertyAction(EXPERIMENTAL_PROPERTY)));
        } catch (Exception e) {
            experimental = false;
        }
        EXPERIMENTAL = experimental;
        ASM_API = 524288;
    }

    private OpenedClassReader() {
        throw new UnsupportedOperationException("This class is a utility class and not supposed to be instantiated");
    }

    public static ClassReader of(byte[] binaryRepresentation) {
        if (EXPERIMENTAL) {
            ClassFileVersion classFileVersion = ClassFileVersion.ofClassFile(binaryRepresentation);
            if (classFileVersion.isGreaterThan(ClassFileVersion.JAVA_V14)) {
                binaryRepresentation[6] = (byte) (ClassFileVersion.JAVA_V14.getMajorVersion() >>> 8);
                binaryRepresentation[7] = (byte) ClassFileVersion.JAVA_V14.getMajorVersion();
                ClassReader classReader = new ClassReader(binaryRepresentation);
                binaryRepresentation[6] = (byte) (classFileVersion.getMajorVersion() >>> 8);
                binaryRepresentation[7] = (byte) classFileVersion.getMajorVersion();
                return classReader;
            }
            return new ClassReader(binaryRepresentation);
        }
        return new ClassReader(binaryRepresentation);
    }
}
