package org.powermock.modules.junit4.common.internal.impl;

import junit.runner.Version;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-common-2.0.9.jar:org/powermock/modules/junit4/common/internal/impl/JUnitVersion.class */
public class JUnitVersion {
    public static boolean isGreaterThanOrEqualTo(String version) {
        String currentVersion = getJUnitVersion();
        return new VersionComparator().compare(currentVersion, version) >= 0;
    }

    public static String getJUnitVersion() {
        return Version.id();
    }
}
