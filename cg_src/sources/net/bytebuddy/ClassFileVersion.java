package net.bytebuddy;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import net.bytebuddy.build.CachedReturnPlugin;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.utility.OpenedClassReader;
import org.apache.tools.ant.util.JavaEnvUtils;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/ClassFileVersion.class */
public class ClassFileVersion implements Comparable<ClassFileVersion> {
    protected static final int BASE_VERSION = 44;
    public static final ClassFileVersion JAVA_V1 = new ClassFileVersion(196653);
    public static final ClassFileVersion JAVA_V2 = new ClassFileVersion(46);
    public static final ClassFileVersion JAVA_V3 = new ClassFileVersion(47);
    public static final ClassFileVersion JAVA_V4 = new ClassFileVersion(48);
    public static final ClassFileVersion JAVA_V5 = new ClassFileVersion(49);
    public static final ClassFileVersion JAVA_V6 = new ClassFileVersion(50);
    public static final ClassFileVersion JAVA_V7 = new ClassFileVersion(51);
    public static final ClassFileVersion JAVA_V8 = new ClassFileVersion(52);
    public static final ClassFileVersion JAVA_V9 = new ClassFileVersion(53);
    public static final ClassFileVersion JAVA_V10 = new ClassFileVersion(54);
    public static final ClassFileVersion JAVA_V11 = new ClassFileVersion(55);
    public static final ClassFileVersion JAVA_V12 = new ClassFileVersion(56);
    public static final ClassFileVersion JAVA_V13 = new ClassFileVersion(57);
    public static final ClassFileVersion JAVA_V14 = new ClassFileVersion(58);
    public static final ClassFileVersion JAVA_V15 = new ClassFileVersion(59);
    public static final ClassFileVersion JAVA_V16 = new ClassFileVersion(60);
    private static final VersionLocator VERSION_LOCATOR = (VersionLocator) AccessController.doPrivileged(VersionLocator.CreationAction.INSTANCE);
    private final int versionNumber;
    private static transient /* synthetic */ ClassFileVersion ofThisVm_Gd7kLQ8C;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.versionNumber == ((ClassFileVersion) obj).versionNumber;
    }

    public int hashCode() {
        return (17 * 31) + this.versionNumber;
    }

    protected ClassFileVersion(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public static ClassFileVersion ofMinorMajor(int versionNumber) {
        ClassFileVersion classFileVersion = new ClassFileVersion(versionNumber);
        if (classFileVersion.getMajorVersion() <= 44) {
            throw new IllegalArgumentException("Class version " + versionNumber + " is not valid");
        }
        return classFileVersion;
    }

    public static ClassFileVersion ofJavaVersionString(String javaVersionString) {
        if (javaVersionString.equals("1.1")) {
            return JAVA_V1;
        }
        if (javaVersionString.equals("1.2")) {
            return JAVA_V2;
        }
        if (javaVersionString.equals("1.3")) {
            return JAVA_V3;
        }
        if (javaVersionString.equals("1.4")) {
            return JAVA_V4;
        }
        if (javaVersionString.equals(JavaEnvUtils.JAVA_1_5) || javaVersionString.equals("5")) {
            return JAVA_V5;
        }
        if (javaVersionString.equals(JavaEnvUtils.JAVA_1_6) || javaVersionString.equals("6")) {
            return JAVA_V6;
        }
        if (javaVersionString.equals(JavaEnvUtils.JAVA_1_7) || javaVersionString.equals("7")) {
            return JAVA_V7;
        }
        if (javaVersionString.equals(JavaEnvUtils.JAVA_1_8) || javaVersionString.equals("8")) {
            return JAVA_V8;
        }
        if (javaVersionString.equals(JavaEnvUtils.JAVA_1_9) || javaVersionString.equals(JavaEnvUtils.JAVA_9)) {
            return JAVA_V9;
        }
        if (javaVersionString.equals("1.10") || javaVersionString.equals(JavaEnvUtils.JAVA_10)) {
            return JAVA_V10;
        }
        if (javaVersionString.equals("1.11") || javaVersionString.equals(JavaEnvUtils.JAVA_11)) {
            return JAVA_V11;
        }
        if (javaVersionString.equals("1.12") || javaVersionString.equals(JavaEnvUtils.JAVA_12)) {
            return JAVA_V12;
        }
        if (javaVersionString.equals("1.13") || javaVersionString.equals("13")) {
            return JAVA_V13;
        }
        if (javaVersionString.equals("1.14") || javaVersionString.equals("14")) {
            return JAVA_V14;
        }
        if (javaVersionString.equals("1.15") || javaVersionString.equals("15")) {
            return JAVA_V15;
        }
        if (javaVersionString.equals("1.16") || javaVersionString.equals("16")) {
            return JAVA_V16;
        }
        if (OpenedClassReader.EXPERIMENTAL) {
            try {
                int version = Integer.parseInt(javaVersionString.startsWith("1.") ? javaVersionString.substring(2) : javaVersionString);
                if (version > 0) {
                    return new ClassFileVersion(44 + version);
                }
            } catch (NumberFormatException e) {
            }
        }
        throw new IllegalArgumentException("Unknown Java version string: " + javaVersionString);
    }

    public static ClassFileVersion ofJavaVersion(int javaVersion) {
        switch (javaVersion) {
            case 1:
                return JAVA_V1;
            case 2:
                return JAVA_V2;
            case 3:
                return JAVA_V3;
            case 4:
                return JAVA_V4;
            case 5:
                return JAVA_V5;
            case 6:
                return JAVA_V6;
            case 7:
                return JAVA_V7;
            case 8:
                return JAVA_V8;
            case 9:
                return JAVA_V9;
            case 10:
                return JAVA_V10;
            case 11:
                return JAVA_V11;
            case 12:
                return JAVA_V12;
            case 13:
                return JAVA_V13;
            case 14:
                return JAVA_V14;
            case 15:
                return JAVA_V15;
            case 16:
                return JAVA_V16;
            default:
                if (OpenedClassReader.EXPERIMENTAL && javaVersion > 0) {
                    return new ClassFileVersion(44 + javaVersion);
                }
                throw new IllegalArgumentException("Unknown Java version: " + javaVersion);
        }
    }

    @CachedReturnPlugin.Enhance
    public static ClassFileVersion ofThisVm() {
        ClassFileVersion locate = ofThisVm_Gd7kLQ8C != null ? null : VERSION_LOCATOR.locate();
        if (locate == null) {
            locate = ofThisVm_Gd7kLQ8C;
        } else {
            ofThisVm_Gd7kLQ8C = locate;
        }
        return locate;
    }

    @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
    public static ClassFileVersion ofThisVm(ClassFileVersion fallback) {
        try {
            return ofThisVm();
        } catch (Exception e) {
            return fallback;
        }
    }

    public static ClassFileVersion of(Class<?> type) throws IOException {
        return of(type, ClassFileLocator.ForClassLoader.of(type.getClassLoader()));
    }

    public static ClassFileVersion of(Class<?> type, ClassFileLocator classFileLocator) throws IOException {
        return of(TypeDescription.ForLoadedType.of(type), classFileLocator);
    }

    public static ClassFileVersion of(TypeDescription typeDescription, ClassFileLocator classFileLocator) throws IOException {
        return ofClassFile(classFileLocator.locate(typeDescription.getName()).resolve());
    }

    public static ClassFileVersion ofClassFile(byte[] binaryRepresentation) {
        if (binaryRepresentation.length < 7) {
            throw new IllegalArgumentException("Supplied byte array is too short to be a class file with " + binaryRepresentation.length + " byte");
        }
        return ofMinorMajor((binaryRepresentation[6] << 8) | (binaryRepresentation[7] & 255));
    }

    public int getMinorMajorVersion() {
        return this.versionNumber;
    }

    public short getMajorVersion() {
        return (short) (this.versionNumber & 255);
    }

    public short getMinorVersion() {
        return (short) (this.versionNumber >> 16);
    }

    public int getJavaVersion() {
        return getMajorVersion() - 44;
    }

    public boolean isAtLeast(ClassFileVersion classFileVersion) {
        return compareTo(classFileVersion) > -1;
    }

    public boolean isGreaterThan(ClassFileVersion classFileVersion) {
        return compareTo(classFileVersion) > 0;
    }

    public boolean isAtMost(ClassFileVersion classFileVersion) {
        return compareTo(classFileVersion) < 1;
    }

    public boolean isLessThan(ClassFileVersion classFileVersion) {
        return compareTo(classFileVersion) < 0;
    }

    public ClassFileVersion asPreviewVersion() {
        return new ClassFileVersion(this.versionNumber | (-65536));
    }

    public boolean isPreviewVersion() {
        return (this.versionNumber & (-65536)) == -65536;
    }

    @Override // java.lang.Comparable
    public int compareTo(ClassFileVersion other) {
        int majorVersion;
        if (getMajorVersion() == other.getMajorVersion()) {
            majorVersion = getMinorVersion() - other.getMinorVersion();
        } else {
            majorVersion = getMajorVersion() - other.getMajorVersion();
        }
        return Integer.signum(majorVersion);
    }

    public String toString() {
        return "Java " + getJavaVersion() + " (" + getMinorMajorVersion() + ")";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/ClassFileVersion$VersionLocator.class */
    public interface VersionLocator {
        ClassFileVersion locate();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/ClassFileVersion$VersionLocator$CreationAction.class */
        public enum CreationAction implements PrivilegedAction<VersionLocator> {
            INSTANCE;

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
            public VersionLocator run() {
                try {
                    return new ForJava9CapableVm(Runtime.class.getMethod("version", new Class[0]), Class.forName("java.lang.Runtime$Version").getMethod("major", new Class[0]));
                } catch (Exception e) {
                    return ForLegacyVm.INSTANCE;
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/ClassFileVersion$VersionLocator$ForJava9CapableVm.class */
        public static class ForJava9CapableVm implements VersionLocator {
            private static final Object STATIC_METHOD = null;
            private final Method current;
            private final Method major;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.current.equals(((ForJava9CapableVm) obj).current) && this.major.equals(((ForJava9CapableVm) obj).major);
            }

            public int hashCode() {
                return (((17 * 31) + this.current.hashCode()) * 31) + this.major.hashCode();
            }

            protected ForJava9CapableVm(Method current, Method major) {
                this.current = current;
                this.major = major;
            }

            @Override // net.bytebuddy.ClassFileVersion.VersionLocator
            public ClassFileVersion locate() {
                try {
                    return ClassFileVersion.ofJavaVersion(((Integer) this.major.invoke(this.current.invoke(STATIC_METHOD, new Object[0]), new Object[0])).intValue());
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Could not access VM version lookup", exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Could not look up VM version", exception2.getCause());
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/ClassFileVersion$VersionLocator$ForLegacyVm.class */
        public enum ForLegacyVm implements VersionLocator, PrivilegedAction<String> {
            INSTANCE;
            
            private static final String JAVA_VERSION_PROPERTY = "java.version";

            @Override // net.bytebuddy.ClassFileVersion.VersionLocator
            public ClassFileVersion locate() {
                String versionString = (String) AccessController.doPrivileged(this);
                int[] versionIndex = new int[3];
                versionIndex[0] = -1;
                versionIndex[1] = 0;
                versionIndex[2] = 0;
                for (int i = 1; i < 3; i++) {
                    versionIndex[i] = versionString.indexOf(46, versionIndex[i - 1] + 1);
                    if (versionIndex[i] == -1) {
                        throw new IllegalStateException("This JVM's version string does not seem to be valid: " + versionString);
                    }
                }
                return ClassFileVersion.ofJavaVersion(Integer.parseInt(versionString.substring(versionIndex[1] + 1, versionIndex[2])));
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                return System.getProperty(JAVA_VERSION_PROPERTY);
            }
        }
    }
}
