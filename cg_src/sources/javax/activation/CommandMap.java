package javax.activation;

import java.util.Map;
import java.util.WeakHashMap;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:j2ee.jar:javax/activation/CommandMap.class
 */
/* loaded from: gencallgraphv3.jar:javax.activation-api-1.2.0.jar:javax/activation/CommandMap.class */
public abstract class CommandMap {
    private static CommandMap defaultCommandMap = null;
    private static Map<ClassLoader, CommandMap> map = new WeakHashMap();

    public abstract CommandInfo[] getPreferredCommands(String str);

    public abstract CommandInfo[] getAllCommands(String str);

    public abstract CommandInfo getCommand(String str, String str2);

    public abstract DataContentHandler createDataContentHandler(String str);

    public static synchronized CommandMap getDefaultCommandMap() {
        if (defaultCommandMap != null) {
            return defaultCommandMap;
        }
        ClassLoader tccl = SecuritySupport.getContextClassLoader();
        CommandMap def = map.get(tccl);
        if (def == null) {
            def = new MailcapCommandMap();
            map.put(tccl, def);
        }
        return def;
    }

    public static synchronized void setDefaultCommandMap(CommandMap commandMap) {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            try {
                security.checkSetFactory();
            } catch (SecurityException ex) {
                ClassLoader cl = CommandMap.class.getClassLoader();
                if (cl == null || cl.getParent() == null || cl != commandMap.getClass().getClassLoader()) {
                    throw ex;
                }
            }
        }
        map.remove(SecuritySupport.getContextClassLoader());
        defaultCommandMap = commandMap;
    }

    public CommandInfo[] getPreferredCommands(String mimeType, DataSource ds) {
        return getPreferredCommands(mimeType);
    }

    public CommandInfo[] getAllCommands(String mimeType, DataSource ds) {
        return getAllCommands(mimeType);
    }

    public CommandInfo getCommand(String mimeType, String cmdName, DataSource ds) {
        return getCommand(mimeType, cmdName);
    }

    public DataContentHandler createDataContentHandler(String mimeType, DataSource ds) {
        return createDataContentHandler(mimeType);
    }

    public String[] getMimeTypes() {
        return null;
    }
}
