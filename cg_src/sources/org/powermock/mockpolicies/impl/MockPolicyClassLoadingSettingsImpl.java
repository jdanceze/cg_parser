package org.powermock.mockpolicies.impl;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.powermock.mockpolicies.MockPolicyClassLoadingSettings;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/mockpolicies/impl/MockPolicyClassLoadingSettingsImpl.class */
public class MockPolicyClassLoadingSettingsImpl implements MockPolicyClassLoadingSettings {
    private Set<String> fullyQualifiedNamesOfClassesToLoadByMockClassloader = new LinkedHashSet();
    private Set<String> staticInitializersToSuppress = new LinkedHashSet();

    @Override // org.powermock.mockpolicies.MockPolicyClassLoadingSettings
    public String[] getFullyQualifiedNamesOfClassesToLoadByMockClassloader() {
        if (this.fullyQualifiedNamesOfClassesToLoadByMockClassloader == null) {
            return new String[0];
        }
        return (String[]) this.fullyQualifiedNamesOfClassesToLoadByMockClassloader.toArray(new String[this.fullyQualifiedNamesOfClassesToLoadByMockClassloader.size()]);
    }

    @Override // org.powermock.mockpolicies.MockPolicyClassLoadingSettings
    public String[] getStaticInitializersToSuppress() {
        if (this.staticInitializersToSuppress == null) {
            return new String[0];
        }
        return (String[]) this.staticInitializersToSuppress.toArray(new String[this.staticInitializersToSuppress.size()]);
    }

    @Override // org.powermock.mockpolicies.MockPolicyClassLoadingSettings
    public void addFullyQualifiedNamesOfClassesToLoadByMockClassloader(String firstClass, String... additionalClasses) {
        this.fullyQualifiedNamesOfClassesToLoadByMockClassloader.add(firstClass);
        addFullyQualifiedNamesOfClassesToLoadByMockClassloader(additionalClasses);
    }

    @Override // org.powermock.mockpolicies.MockPolicyClassLoadingSettings
    public void addFullyQualifiedNamesOfClassesToLoadByMockClassloader(String[] classes) {
        Collections.addAll(this.fullyQualifiedNamesOfClassesToLoadByMockClassloader, classes);
    }

    @Override // org.powermock.mockpolicies.MockPolicyClassLoadingSettings
    public void addStaticInitializersToSuppress(String firstStaticInitializerToSuppress, String... additionalStaticInitializersToSuppress) {
        this.staticInitializersToSuppress.add(firstStaticInitializerToSuppress);
        addStaticInitializersToSuppress(additionalStaticInitializersToSuppress);
    }

    @Override // org.powermock.mockpolicies.MockPolicyClassLoadingSettings
    public void addStaticInitializersToSuppress(String[] staticInitializersToSuppress) {
        Collections.addAll(this.staticInitializersToSuppress, staticInitializersToSuppress);
    }

    @Override // org.powermock.mockpolicies.MockPolicyClassLoadingSettings
    public void setFullyQualifiedNamesOfClassesToLoadByMockClassloader(String[] classes) {
        this.fullyQualifiedNamesOfClassesToLoadByMockClassloader.clear();
        addFullyQualifiedNamesOfClassesToLoadByMockClassloader(classes);
    }

    @Override // org.powermock.mockpolicies.MockPolicyClassLoadingSettings
    public void setStaticInitializersToSuppress(String[] staticInitializersToSuppress) {
        this.staticInitializersToSuppress.clear();
        addStaticInitializersToSuppress(staticInitializersToSuppress);
    }
}
