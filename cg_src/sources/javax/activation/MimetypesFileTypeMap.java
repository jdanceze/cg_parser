package javax.activation;

import com.sun.activation.registries.LogSupport;
import com.sun.activation.registries.MimeTypeFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Vector;
import org.apache.tools.ant.launch.Launcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:j2ee.jar:javax/activation/MimetypesFileTypeMap.class
 */
/* loaded from: gencallgraphv3.jar:javax.activation-api-1.2.0.jar:javax/activation/MimetypesFileTypeMap.class */
public class MimetypesFileTypeMap extends FileTypeMap {
    private MimeTypeFile[] DB;
    private static final int PROG = 0;
    private static final String defaultType = "application/octet-stream";
    private static final String confDir;

    static {
        String dir = null;
        try {
            dir = (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.activation.MimetypesFileTypeMap.1
                @Override // java.security.PrivilegedAction
                public Object run() {
                    String home = System.getProperty("java.home");
                    String newdir = home + File.separator + "conf";
                    File conf = new File(newdir);
                    if (conf.exists()) {
                        return newdir + File.separator;
                    }
                    return home + File.separator + Launcher.ANT_PRIVATELIB + File.separator;
                }
            });
        } catch (Exception e) {
        }
        confDir = dir;
    }

    public MimetypesFileTypeMap() {
        MimeTypeFile mf;
        Vector dbv = new Vector(5);
        dbv.addElement(null);
        LogSupport.log("MimetypesFileTypeMap: load HOME");
        try {
            String user_home = System.getProperty(Launcher.USER_HOMEDIR);
            if (user_home != null) {
                String path = user_home + File.separator + ".mime.types";
                MimeTypeFile mf2 = loadFile(path);
                if (mf2 != null) {
                    dbv.addElement(mf2);
                }
            }
        } catch (SecurityException e) {
        }
        LogSupport.log("MimetypesFileTypeMap: load SYS");
        try {
            if (confDir != null && (mf = loadFile(confDir + "mime.types")) != null) {
                dbv.addElement(mf);
            }
        } catch (SecurityException e2) {
        }
        LogSupport.log("MimetypesFileTypeMap: load JAR");
        loadAllResources(dbv, "META-INF/mime.types");
        LogSupport.log("MimetypesFileTypeMap: load DEF");
        MimeTypeFile mf3 = loadResource("/META-INF/mimetypes.default");
        if (mf3 != null) {
            dbv.addElement(mf3);
        }
        this.DB = new MimeTypeFile[dbv.size()];
        dbv.copyInto(this.DB);
    }

    private MimeTypeFile loadResource(String name) {
        InputStream clis = null;
        try {
            try {
                InputStream clis2 = SecuritySupport.getResourceAsStream(getClass(), name);
                if (clis2 != null) {
                    MimeTypeFile mf = new MimeTypeFile(clis2);
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("MimetypesFileTypeMap: successfully loaded mime types file: " + name);
                    }
                    if (clis2 != null) {
                        try {
                            clis2.close();
                        } catch (IOException e) {
                        }
                    }
                    return mf;
                }
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MimetypesFileTypeMap: not loading mime types file: " + name);
                }
                if (clis2 != null) {
                    try {
                        clis2.close();
                    } catch (IOException e2) {
                        return null;
                    }
                }
                return null;
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        clis.close();
                    } catch (IOException e3) {
                        throw th;
                    }
                }
                throw th;
            }
        } catch (IOException e4) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MimetypesFileTypeMap: can't load " + name, e4);
            }
            if (0 != 0) {
                try {
                    clis.close();
                } catch (IOException e5) {
                    return null;
                }
            }
            return null;
        } catch (SecurityException sex) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MimetypesFileTypeMap: can't load " + name, sex);
            }
            if (0 != 0) {
                try {
                    clis.close();
                } catch (IOException e6) {
                    return null;
                }
            }
            return null;
        }
    }

    private void loadAllResources(Vector v, String name) {
        boolean anyLoaded = false;
        try {
            ClassLoader cld = SecuritySupport.getContextClassLoader();
            if (cld == null) {
                cld = getClass().getClassLoader();
            }
            URL[] urls = cld != null ? SecuritySupport.getResources(cld, name) : SecuritySupport.getSystemResources(name);
            if (urls != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MimetypesFileTypeMap: getResources");
                }
                for (URL url : urls) {
                    InputStream clis = null;
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("MimetypesFileTypeMap: URL " + url);
                    }
                    try {
                        clis = SecuritySupport.openStream(url);
                        if (clis != null) {
                            v.addElement(new MimeTypeFile(clis));
                            anyLoaded = true;
                            if (LogSupport.isLoggable()) {
                                LogSupport.log("MimetypesFileTypeMap: successfully loaded mime types from URL: " + url);
                            }
                        } else if (LogSupport.isLoggable()) {
                            LogSupport.log("MimetypesFileTypeMap: not loading mime types from URL: " + url);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e) {
                            }
                        }
                    } catch (IOException ioex) {
                        if (LogSupport.isLoggable()) {
                            LogSupport.log("MimetypesFileTypeMap: can't load " + url, ioex);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e2) {
                            }
                        }
                    } catch (SecurityException sex) {
                        if (LogSupport.isLoggable()) {
                            LogSupport.log("MimetypesFileTypeMap: can't load " + url, sex);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e3) {
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MimetypesFileTypeMap: can't load " + name, ex);
            }
        }
        if (anyLoaded) {
            return;
        }
        LogSupport.log("MimetypesFileTypeMap: !anyLoaded");
        MimeTypeFile mf = loadResource("/" + name);
        if (mf != null) {
            v.addElement(mf);
        }
    }

    private MimeTypeFile loadFile(String name) {
        MimeTypeFile mtf = null;
        try {
            mtf = new MimeTypeFile(name);
        } catch (IOException e) {
        }
        return mtf;
    }

    public MimetypesFileTypeMap(String mimeTypeFileName) throws IOException {
        this();
        this.DB[0] = new MimeTypeFile(mimeTypeFileName);
    }

    public MimetypesFileTypeMap(InputStream is) {
        this();
        try {
            this.DB[0] = new MimeTypeFile(is);
        } catch (IOException e) {
        }
    }

    public synchronized void addMimeTypes(String mime_types) {
        if (this.DB[0] == null) {
            this.DB[0] = new MimeTypeFile();
        }
        this.DB[0].appendToRegistry(mime_types);
    }

    @Override // javax.activation.FileTypeMap
    public String getContentType(File f) {
        return getContentType(f.getName());
    }

    @Override // javax.activation.FileTypeMap
    public synchronized String getContentType(String filename) {
        String result;
        int dot_pos = filename.lastIndexOf(".");
        if (dot_pos < 0) {
            return "application/octet-stream";
        }
        String file_ext = filename.substring(dot_pos + 1);
        if (file_ext.length() == 0) {
            return "application/octet-stream";
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null && (result = this.DB[i].getMIMETypeString(file_ext)) != null) {
                return result;
            }
        }
        return "application/octet-stream";
    }
}
