package org.powermock.core.spi;

import org.powermock.mockpolicies.MockPolicyClassLoadingSettings;
import org.powermock.mockpolicies.MockPolicyInterceptionSettings;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/spi/PowerMockPolicy.class */
public interface PowerMockPolicy {
    void applyClassLoadingPolicy(MockPolicyClassLoadingSettings mockPolicyClassLoadingSettings);

    void applyInterceptionPolicy(MockPolicyInterceptionSettings mockPolicyInterceptionSettings);
}
