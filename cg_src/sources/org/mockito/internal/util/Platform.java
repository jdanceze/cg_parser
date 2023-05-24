package org.mockito.internal.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/Platform.class */
public abstract class Platform {
    private static final Pattern JAVA_8_RELEASE_VERSION_SCHEME = Pattern.compile("1\\.8\\.0_(\\d+)(?:-ea)?(?:-b\\d+)?");
    private static final Pattern JAVA_8_DEV_VERSION_SCHEME = Pattern.compile("1\\.8\\.0b\\d+_u(\\d+)");
    public static final String JAVA_VERSION = System.getProperty("java.specification.version");
    public static final String JVM_VERSION = System.getProperty("java.runtime.version");
    public static final String JVM_VENDOR = System.getProperty("java.vm.vendor");
    public static final String JVM_VENDOR_VERSION = System.getProperty("java.vm.version");
    public static final String JVM_NAME = System.getProperty("java.vm.name");
    public static final String JVM_INFO = System.getProperty("java.vm.info");
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_VERSION = System.getProperty("os.version");

    private Platform() {
    }

    public static boolean isAndroid() {
        return System.getProperty("java.vendor", "").toLowerCase(Locale.US).contains("android");
    }

    public static boolean isAndroidMockMakerRequired() {
        return Boolean.getBoolean("org.mockito.mock.android");
    }

    public static String describe() {
        String description = String.format("Java               : %s\nJVM vendor name    : %s\nJVM vendor version : %s\nJVM name           : %s\nJVM version        : %s\nJVM info           : %s\nOS name            : %s\nOS version         : %s\n", JAVA_VERSION, JVM_VENDOR, JVM_VENDOR_VERSION, JVM_NAME, JVM_VERSION, JVM_INFO, OS_NAME, OS_VERSION);
        if (isAndroid()) {
            description = StringUtil.join("IMPORTANT INFORMATION FOR ANDROID USERS:", "", "The regular Byte Buddy mock makers cannot generate code on an Android VM!", "To resolve this, please use the 'mockito-android' dependency for your application:", "http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22mockito-android%22%20g%3A%22org.mockito%22", "", description);
        }
        return description;
    }

    public static boolean isJava8BelowUpdate45() {
        return isJava8BelowUpdate45(JVM_VERSION);
    }

    static boolean isJava8BelowUpdate45(String jvmVersion) {
        Matcher matcher = JAVA_8_RELEASE_VERSION_SCHEME.matcher(jvmVersion);
        if (matcher.matches()) {
            int update = Integer.parseInt(matcher.group(1));
            return update < 45;
        }
        Matcher matcher2 = JAVA_8_DEV_VERSION_SCHEME.matcher(jvmVersion);
        if (matcher2.matches()) {
            int update2 = Integer.parseInt(matcher2.group(1));
            return update2 < 45;
        }
        return Pattern.compile("1\\.8\\.0-b\\d+").matcher(jvmVersion).matches();
    }

    public static String warnForVM(String vmName1, String warnMessage1, String vmName2, String warnMessage2) {
        return warnForVM(JVM_NAME, vmName1, warnMessage1, vmName2, warnMessage2);
    }

    static String warnForVM(String current, String vmName1, String warnMessage1, String vmName2, String warnMessage2) {
        if (vmName1 != null && current.contains(vmName1)) {
            return warnMessage1;
        }
        if (vmName2 != null && current.contains(vmName2)) {
            return warnMessage2;
        }
        return "";
    }
}
