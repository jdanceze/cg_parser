package org.powermock.configuration;

import org.powermock.core.classloader.ByteCodeFramework;
import org.powermock.utils.ArrayUtil;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/PowerMockConfiguration.class */
public class PowerMockConfiguration implements Configuration<PowerMockConfiguration> {
    private String[] globalIgnore;
    private ByteCodeFramework byteCodeFramework;

    public String[] getGlobalIgnore() {
        return this.globalIgnore;
    }

    public void setGlobalIgnore(String[] globalIgnore) {
        this.globalIgnore = globalIgnore;
    }

    public ByteCodeFramework getByteCodeFramework() {
        return this.byteCodeFramework;
    }

    public void setByteCodeFramework(ByteCodeFramework byteCodeFramework) {
        this.byteCodeFramework = byteCodeFramework;
    }

    @Override // org.powermock.configuration.Configuration
    public PowerMockConfiguration merge(PowerMockConfiguration configuration) {
        if (configuration == null) {
            return this;
        }
        PowerMockConfiguration powerMockConfiguration = new PowerMockConfiguration();
        String[] globalIgnore = ArrayUtil.mergeArrays(this.globalIgnore, configuration.globalIgnore);
        powerMockConfiguration.setGlobalIgnore(globalIgnore);
        if (configuration.byteCodeFramework == null) {
            powerMockConfiguration.setByteCodeFramework(this.byteCodeFramework);
        } else {
            powerMockConfiguration.setByteCodeFramework(configuration.byteCodeFramework);
        }
        return powerMockConfiguration;
    }
}
