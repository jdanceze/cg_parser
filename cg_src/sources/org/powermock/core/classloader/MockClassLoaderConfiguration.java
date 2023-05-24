package org.powermock.core.classloader;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.powermock.core.ClassReplicaCreator;
import org.powermock.core.WildcardMatcher;
import org.powermock.core.spi.PowerMockPolicy;
import org.powermock.core.spi.support.InvocationSubstitute;
import org.powermock.utils.ArrayUtil;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/classloader/MockClassLoaderConfiguration.class */
public class MockClassLoaderConfiguration {
    static final String[] PACKAGES_TO_BE_DEFERRED = {"org.hamcrest.*", "jdk.*", "java.*", "javax.accessibility.*", "sun.*", "org.junit.*", "org.testng.*", "junit.*", "org.pitest.*", "org.powermock.modules.junit4.common.internal.*", "org.powermock.modules.junit3.internal.PowerMockJUnit3RunnerDelegate*", "org.powermock.core*", "org.jacoco.agent.rt.*"};
    private static final String[] PACKAGES_TO_LOAD_BUT_NOT_MODIFY = {"org.junit.", "junit.", "org.testng.", "org.easymock.", "net.sf.cglib.", "javassist.", "org.powermock.modules.junit4.internal.", "org.powermock.modules.junit4.legacy.internal.", "org.powermock.modules.junit3.internal.", "org.powermock"};
    private final String[] specificClassesToLoadButNotModify;
    private final Set<String> modify;
    private String[] deferPackages;

    public MockClassLoaderConfiguration() {
        this(new String[0], new String[0]);
    }

    public MockClassLoaderConfiguration(String[] classesToMock, String[] packagesToDefer) {
        this.specificClassesToLoadButNotModify = new String[]{InvocationSubstitute.class.getName(), PowerMockPolicy.class.getName(), ClassReplicaCreator.class.getName()};
        this.modify = Collections.synchronizedSet(new HashSet());
        this.deferPackages = getPackagesToDefer(packagesToDefer);
        addClassesToModify(classesToMock);
    }

    public void addIgnorePackage(String... packagesToIgnore) {
        if (packagesToIgnore != null && packagesToIgnore.length > 0) {
            int previousLength = this.deferPackages.length;
            String[] newDeferPackages = new String[previousLength + packagesToIgnore.length];
            System.arraycopy(this.deferPackages, 0, newDeferPackages, 0, previousLength);
            System.arraycopy(packagesToIgnore, 0, newDeferPackages, previousLength, packagesToIgnore.length);
            this.deferPackages = newDeferPackages;
        }
    }

    public final void addClassesToModify(String... classes) {
        if (classes != null) {
            for (String clazz : classes) {
                if (!shouldDefer(PACKAGES_TO_BE_DEFERRED, clazz)) {
                    this.modify.add(clazz);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldDefer(String className) {
        return shouldDefer(this.deferPackages, className);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldMockClass(String className) {
        return shouldModify(className) && !shouldLoadWithMockClassloaderWithoutModifications(className);
    }

    String[] getDeferPackages() {
        return (String[]) ArrayUtil.clone(this.deferPackages);
    }

    private boolean shouldDefer(String[] packages, String name) {
        for (String packageToCheck : packages) {
            if (deferConditionMatches(name, packageToCheck)) {
                return true;
            }
        }
        return false;
    }

    private boolean deferConditionMatches(String name, String packageName) {
        boolean wildcardMatch = WildcardMatcher.matches(name, packageName);
        return (!wildcardMatch || shouldLoadUnmodifiedClass(name) || shouldModifyClass(name)) ? false : true;
    }

    private boolean shouldIgnore(String[] packages, String name) {
        for (String ignore : packages) {
            if (WildcardMatcher.matches(name, ignore)) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldLoadUnmodifiedClass(String className) {
        String[] strArr;
        for (String classNameToLoadButNotModify : this.specificClassesToLoadButNotModify) {
            if (className.equals(classNameToLoadButNotModify)) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldLoadWithMockClassloaderWithoutModifications(String className) {
        String[] strArr;
        if (className.startsWith("org.powermock.example")) {
            return false;
        }
        for (String packageToLoadButNotModify : PACKAGES_TO_LOAD_BUT_NOT_MODIFY) {
            if (className.startsWith(packageToLoadButNotModify)) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldModifyClass(String className) {
        return this.modify.contains(className);
    }

    private boolean shouldIgnore(String className) {
        return shouldIgnore(this.deferPackages, className);
    }

    boolean shouldModify(String className) {
        boolean shouldIgnoreClass = shouldIgnore(className);
        boolean shouldModifyAll = shouldModifyAll();
        if (shouldModifyAll) {
            return !shouldIgnoreClass;
        }
        return WildcardMatcher.matchesAny((Collection<String>) this.modify, className);
    }

    private boolean shouldModifyAll() {
        return this.modify.size() == 1 && this.modify.iterator().next().equals("*");
    }

    private static String[] getPackagesToDefer(String[] additionalDeferPackages) {
        int additionalIgnorePackagesLength = additionalDeferPackages == null ? 0 : additionalDeferPackages.length;
        int defaultDeferPackagesLength = PACKAGES_TO_BE_DEFERRED.length;
        int allIgnoreLength = defaultDeferPackagesLength + additionalIgnorePackagesLength;
        String[] allPackagesToBeIgnored = new String[allIgnoreLength];
        if (allIgnoreLength > defaultDeferPackagesLength) {
            System.arraycopy(PACKAGES_TO_BE_DEFERRED, 0, allPackagesToBeIgnored, 0, defaultDeferPackagesLength);
            System.arraycopy(additionalDeferPackages != null ? additionalDeferPackages : new String[0], 0, allPackagesToBeIgnored, defaultDeferPackagesLength, additionalIgnorePackagesLength);
            return allPackagesToBeIgnored;
        }
        return PACKAGES_TO_BE_DEFERRED;
    }
}
