package org.mockito.internal.configuration.plugins;

import org.mockito.plugins.PluginSwitch;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/plugins/DefaultPluginSwitch.class */
class DefaultPluginSwitch implements PluginSwitch {
    @Override // org.mockito.plugins.PluginSwitch
    public boolean isEnabled(String pluginClassName) {
        return true;
    }
}
