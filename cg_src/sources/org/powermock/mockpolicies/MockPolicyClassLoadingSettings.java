package org.powermock.mockpolicies;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/mockpolicies/MockPolicyClassLoadingSettings.class */
public interface MockPolicyClassLoadingSettings {
    void setStaticInitializersToSuppress(String[] strArr);

    void addStaticInitializersToSuppress(String str, String... strArr);

    void addStaticInitializersToSuppress(String[] strArr);

    void setFullyQualifiedNamesOfClassesToLoadByMockClassloader(String[] strArr);

    void addFullyQualifiedNamesOfClassesToLoadByMockClassloader(String str, String... strArr);

    void addFullyQualifiedNamesOfClassesToLoadByMockClassloader(String[] strArr);

    String[] getStaticInitializersToSuppress();

    String[] getFullyQualifiedNamesOfClassesToLoadByMockClassloader();
}
