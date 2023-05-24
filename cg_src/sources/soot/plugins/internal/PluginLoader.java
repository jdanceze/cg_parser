package soot.plugins.internal;

import java.io.File;
import java.security.InvalidParameterException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.PackManager;
import soot.Transform;
import soot.plugins.SootPhasePlugin;
import soot.plugins.model.PhasePluginDescription;
import soot.plugins.model.PluginDescription;
import soot.plugins.model.Plugins;
/* loaded from: gencallgraphv3.jar:soot/plugins/internal/PluginLoader.class */
public class PluginLoader {
    private static final Logger logger = LoggerFactory.getLogger(PluginLoader.class);
    private static ClassLoadingStrategy loadStrategy = new ReflectionClassLoadingStrategy();

    private static String[] appendEnabled(String[] options) {
        for (String option : options) {
            if ("enabled".equals(option)) {
                return options;
            }
        }
        String[] result = new String[options.length + 1];
        result[0] = "enabled";
        System.arraycopy(options, 0, result, 1, options.length);
        return result;
    }

    private static String concat(String[] options) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String option : options) {
            if (first) {
                first = false;
            } else {
                sb.append(' ');
            }
            sb.append(option);
        }
        return sb.toString();
    }

    private static String getPackName(String phaseName) {
        if (!phaseName.contains(".")) {
            throw new RuntimeException("Name of phase '" + phaseName + "'does not contain a dot.");
        }
        return phaseName.substring(0, phaseName.indexOf(46));
    }

    private static void handlePhasePlugin(PhasePluginDescription pluginDescription) {
        try {
            Object instance = loadStrategy.create(pluginDescription.getClassName());
            if (!(instance instanceof SootPhasePlugin)) {
                throw new RuntimeException("The plugin class '" + pluginDescription.getClassName() + "' does not implement SootPhasePlugin.");
            }
            SootPhasePlugin phasePlugin = (SootPhasePlugin) instance;
            phasePlugin.setDescription(pluginDescription);
            String packName = getPackName(pluginDescription.getPhaseName());
            Transform transform = new Transform(pluginDescription.getPhaseName(), phasePlugin.getTransformer());
            transform.setDeclaredOptions(concat(appendEnabled(phasePlugin.getDeclaredOptions())));
            transform.setDefaultOptions(concat(phasePlugin.getDefaultOptions()));
            PackManager.v().getPack(packName).add(transform);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load plugin class for " + pluginDescription + ".", e);
        } catch (InstantiationException e2) {
            throw new RuntimeException("Failed to instanciate plugin class for " + pluginDescription + ".", e2);
        }
    }

    public static boolean load(String file) {
        File configFile = new File(file);
        if (!configFile.exists()) {
            System.err.println("The configuration file '" + configFile + "' does not exist.");
            return false;
        } else if (!configFile.canRead()) {
            System.err.println("Cannot read the configuration file '" + configFile + "'.");
            return false;
        } else {
            try {
                JAXBContext context = JAXBContext.newInstance(Plugins.class, PluginDescription.class, PhasePluginDescription.class);
                Object root = context.createUnmarshaller().unmarshal(configFile);
                if (!(root instanceof Plugins)) {
                    System.err.println("Expected a root node of type Plugins got " + root.getClass());
                    return false;
                }
                loadPlugins((Plugins) root);
                return true;
            } catch (RuntimeException e) {
                System.err.println("Failed to load plugin correctly.");
                logger.error(e.getMessage(), (Throwable) e);
                return false;
            } catch (JAXBException e2) {
                System.err.println("An error occured while loading plugin configuration '" + file + "'.");
                logger.error(e2.getMessage(), (Throwable) e2);
                return false;
            }
        }
    }

    public static void loadPlugins(Plugins plugins) throws RuntimeException {
        for (PluginDescription plugin : plugins.getPluginDescriptions()) {
            if (plugin instanceof PhasePluginDescription) {
                handlePhasePlugin((PhasePluginDescription) plugin);
            } else {
                logger.debug("[Warning] Unhandled plugin of type '" + plugin.getClass() + "'");
            }
        }
    }

    public static void setClassLoadingStrategy(ClassLoadingStrategy strategy) {
        if (strategy == null) {
            throw new InvalidParameterException("Class loading strategy is not allowed to be null.");
        }
        loadStrategy = strategy;
    }

    private PluginLoader() {
    }
}
