package org.powermock.tests.utils.impl;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.powermock.configuration.GlobalConfiguration;
import org.powermock.configuration.PowerMockConfiguration;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.tests.utils.IgnorePackagesExtractor;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/impl/PowerMockIgnorePackagesExtractorImpl.class */
public class PowerMockIgnorePackagesExtractorImpl implements IgnorePackagesExtractor {
    @Override // org.powermock.tests.utils.IgnorePackagesExtractor
    public String[] getPackagesToIgnore(AnnotatedElement element) {
        PowerMockIgnore annotation = (PowerMockIgnore) element.getAnnotation(PowerMockIgnore.class);
        boolean useGlobal = true;
        if (annotation != null) {
            useGlobal = annotation.globalIgnore();
        }
        Set<String> ignoredPackages = new HashSet<>();
        boolean useGlobal2 = useGlobal & extractPackageToIgnore(element, ignoredPackages);
        String[] packageToIgnore = (String[]) ignoredPackages.toArray(new String[ignoredPackages.size()]);
        if (useGlobal2) {
            return getPackageToIgnoreWithGlobal(packageToIgnore);
        }
        return packageToIgnore;
    }

    private String[] getPackageToIgnoreWithGlobal(String[] packageToIgnore) {
        String[] allPackageToIgnore;
        String[] globalIgnore = getGlobalIgnore();
        if (globalIgnore != null) {
            allPackageToIgnore = addGlobalIgnore(packageToIgnore, globalIgnore);
        } else {
            allPackageToIgnore = packageToIgnore;
        }
        return allPackageToIgnore;
    }

    private String[] getGlobalIgnore() {
        PowerMockConfiguration powerMockConfiguration = GlobalConfiguration.powerMockConfiguration();
        return powerMockConfiguration.getGlobalIgnore();
    }

    private boolean extractPackageToIgnore(AnnotatedElement element, Set<String> ignoredPackages) {
        boolean useGlobalFromAnnotation = addValueFromAnnotation(element, ignoredPackages);
        boolean useGlobalFromSuperclass = addValuesFromSuperclass((Class) element, ignoredPackages);
        return useGlobalFromAnnotation & useGlobalFromSuperclass;
    }

    private boolean addValuesFromSuperclass(Class<?> element, Set<String> ignoredPackages) {
        Collection<Class<?>> superclasses = new ArrayList<>();
        Collections.addAll(superclasses, element.getSuperclass());
        Collections.addAll(superclasses, element.getInterfaces());
        boolean useGlobalIgnore = true;
        for (Class<?> superclass : superclasses) {
            if (superclass != null && !superclass.equals(Object.class)) {
                useGlobalIgnore &= extractPackageToIgnore(superclass, ignoredPackages);
            }
        }
        return useGlobalIgnore;
    }

    private boolean addValueFromAnnotation(AnnotatedElement element, Set<String> ignoredPackages) {
        PowerMockIgnore annotation = (PowerMockIgnore) element.getAnnotation(PowerMockIgnore.class);
        if (annotation != null) {
            String[] ignores = annotation.value();
            Collections.addAll(ignoredPackages, ignores);
            return annotation.globalIgnore();
        }
        return true;
    }

    private String[] addGlobalIgnore(String[] packageToIgnore, String[] globalIgnore) {
        String[] allPackageToIgnore = new String[globalIgnore.length + packageToIgnore.length];
        System.arraycopy(globalIgnore, 0, allPackageToIgnore, 0, globalIgnore.length);
        System.arraycopy(packageToIgnore, 0, allPackageToIgnore, globalIgnore.length, packageToIgnore.length);
        return allPackageToIgnore;
    }
}
