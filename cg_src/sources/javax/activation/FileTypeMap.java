package javax.activation;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:j2ee.jar:javax/activation/FileTypeMap.class
 */
/* loaded from: gencallgraphv3.jar:javax.activation-api-1.2.0.jar:javax/activation/FileTypeMap.class */
public abstract class FileTypeMap {
    private static FileTypeMap defaultMap = null;
    private static Map<ClassLoader, FileTypeMap> map = new WeakHashMap();

    public abstract String getContentType(File file);

    public abstract String getContentType(String str);

    public static synchronized void setDefaultFileTypeMap(FileTypeMap fileTypeMap) {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            try {
                security.checkSetFactory();
            } catch (SecurityException ex) {
                ClassLoader cl = FileTypeMap.class.getClassLoader();
                if (cl == null || cl.getParent() == null || cl != fileTypeMap.getClass().getClassLoader()) {
                    throw ex;
                }
            }
        }
        map.remove(SecuritySupport.getContextClassLoader());
        defaultMap = fileTypeMap;
    }

    public static synchronized FileTypeMap getDefaultFileTypeMap() {
        if (defaultMap != null) {
            return defaultMap;
        }
        ClassLoader tccl = SecuritySupport.getContextClassLoader();
        FileTypeMap def = map.get(tccl);
        if (def == null) {
            def = new MimetypesFileTypeMap();
            map.put(tccl, def);
        }
        return def;
    }
}
