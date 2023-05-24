package org.powermock.tests.utils;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/MockPolicyInitializer.class */
public interface MockPolicyInitializer {
    void initialize(ClassLoader classLoader);

    boolean needsInitialization();

    boolean isPrepared(String str);

    void refreshPolicies(ClassLoader classLoader);
}
