package org.xml.sax.helpers;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/helpers/NewInstance.class */
class NewInstance {
    private static final boolean DO_FALLBACK = true;
    static Class class$org$xml$sax$helpers$NewInstance;

    NewInstance() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object newInstance(ClassLoader classLoader, String str) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class cls;
        Class<?> loadClass;
        if (classLoader == null) {
            loadClass = Class.forName(str);
        } else {
            try {
                loadClass = classLoader.loadClass(str);
            } catch (ClassNotFoundException e) {
                if (class$org$xml$sax$helpers$NewInstance == null) {
                    cls = class$("org.xml.sax.helpers.NewInstance");
                    class$org$xml$sax$helpers$NewInstance = cls;
                } else {
                    cls = class$org$xml$sax$helpers$NewInstance;
                }
                ClassLoader classLoader2 = cls.getClassLoader();
                loadClass = classLoader2 != null ? classLoader2.loadClass(str) : Class.forName(str);
            }
        }
        return loadClass.newInstance();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ClassLoader getClassLoader() {
        Class cls;
        ClassLoader contextClassLoader = SecuritySupport.getInstance().getContextClassLoader();
        if (contextClassLoader == null) {
            if (class$org$xml$sax$helpers$NewInstance == null) {
                cls = class$("org.xml.sax.helpers.NewInstance");
                class$org$xml$sax$helpers$NewInstance = cls;
            } else {
                cls = class$org$xml$sax$helpers$NewInstance;
            }
            contextClassLoader = cls.getClassLoader();
        }
        return contextClassLoader;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }
}
