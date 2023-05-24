package org.powermock.configuration;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/MockitoConfiguration.class */
public class MockitoConfiguration implements Configuration<MockitoConfiguration> {
    private String mockMakerClass;

    public MockitoConfiguration() {
    }

    private MockitoConfiguration(String mockMakerClass) {
        this.mockMakerClass = mockMakerClass;
    }

    public String getMockMakerClass() {
        return this.mockMakerClass;
    }

    public void setMockMakerClass(String mockMakerClass) {
        this.mockMakerClass = mockMakerClass;
    }

    @Override // org.powermock.configuration.Configuration
    public MockitoConfiguration merge(MockitoConfiguration configuration) {
        if (configuration != null && configuration.getMockMakerClass() != null) {
            return new MockitoConfiguration(configuration.getMockMakerClass());
        }
        return this;
    }
}
