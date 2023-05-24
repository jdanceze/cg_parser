package org.mockito.internal.configuration.plugins;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.io.IOUtil;
import org.mockito.plugins.PluginSwitch;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/plugins/PluginFinder.class */
class PluginFinder {
    private final PluginSwitch pluginSwitch;

    public PluginFinder(PluginSwitch pluginSwitch) {
        this.pluginSwitch = pluginSwitch;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String findPluginClass(Iterable<URL> resources) {
        for (URL resource : resources) {
            InputStream s = null;
            try {
                try {
                    s = resource.openStream();
                    String pluginClassName = new PluginFileReader().readPluginClass(s);
                    if (pluginClassName == null) {
                        IOUtil.closeQuietly(s);
                    } else if (this.pluginSwitch.isEnabled(pluginClassName)) {
                        IOUtil.closeQuietly(s);
                        return pluginClassName;
                    } else {
                        IOUtil.closeQuietly(s);
                    }
                } catch (Exception e) {
                    throw new MockitoException("Problems reading plugin implementation from: " + resource, e);
                }
            } catch (Throwable th) {
                IOUtil.closeQuietly(s);
                throw th;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> findPluginClasses(Iterable<URL> resources) {
        List<String> pluginClassNames = new ArrayList<>();
        for (URL resource : resources) {
            InputStream s = null;
            try {
                try {
                    s = resource.openStream();
                    String pluginClassName = new PluginFileReader().readPluginClass(s);
                    if (pluginClassName == null) {
                        IOUtil.closeQuietly(s);
                    } else if (this.pluginSwitch.isEnabled(pluginClassName)) {
                        pluginClassNames.add(pluginClassName);
                        IOUtil.closeQuietly(s);
                    } else {
                        IOUtil.closeQuietly(s);
                    }
                } catch (Exception e) {
                    throw new MockitoException("Problems reading plugin implementation from: " + resource, e);
                }
            } catch (Throwable th) {
                IOUtil.closeQuietly(s);
                throw th;
            }
        }
        return pluginClassNames;
    }
}
