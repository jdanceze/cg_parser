package javax.activation;

import com.sun.activation.registries.LogSupport;
import com.sun.activation.registries.MailcapFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.tools.ant.launch.Launcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:j2ee.jar:javax/activation/MailcapCommandMap.class
 */
/* loaded from: gencallgraphv3.jar:javax.activation-api-1.2.0.jar:javax/activation/MailcapCommandMap.class */
public class MailcapCommandMap extends CommandMap {
    private MailcapFile[] DB;
    private static final int PROG = 0;
    private static final String confDir;

    static {
        String dir = null;
        try {
            dir = (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.activation.MailcapCommandMap.1
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

    public MailcapCommandMap() {
        MailcapFile mf;
        List dbv = new ArrayList(5);
        dbv.add(null);
        LogSupport.log("MailcapCommandMap: load HOME");
        try {
            String user_home = System.getProperty(Launcher.USER_HOMEDIR);
            if (user_home != null) {
                String path = user_home + File.separator + ".mailcap";
                MailcapFile mf2 = loadFile(path);
                if (mf2 != null) {
                    dbv.add(mf2);
                }
            }
        } catch (SecurityException e) {
        }
        LogSupport.log("MailcapCommandMap: load SYS");
        try {
            if (confDir != null && (mf = loadFile(confDir + "mailcap")) != null) {
                dbv.add(mf);
            }
        } catch (SecurityException e2) {
        }
        LogSupport.log("MailcapCommandMap: load JAR");
        loadAllResources(dbv, "META-INF/mailcap");
        LogSupport.log("MailcapCommandMap: load DEF");
        MailcapFile mf3 = loadResource("/META-INF/mailcap.default");
        if (mf3 != null) {
            dbv.add(mf3);
        }
        this.DB = new MailcapFile[dbv.size()];
        this.DB = (MailcapFile[]) dbv.toArray(this.DB);
    }

    private MailcapFile loadResource(String name) {
        InputStream clis = null;
        try {
            try {
                InputStream clis2 = SecuritySupport.getResourceAsStream(getClass(), name);
                if (clis2 != null) {
                    MailcapFile mf = new MailcapFile(clis2);
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("MailcapCommandMap: successfully loaded mailcap file: " + name);
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
                    LogSupport.log("MailcapCommandMap: not loading mailcap file: " + name);
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
                LogSupport.log("MailcapCommandMap: can't load " + name, e4);
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
                LogSupport.log("MailcapCommandMap: can't load " + name, sex);
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

    private void loadAllResources(List v, String name) {
        boolean anyLoaded = false;
        try {
            ClassLoader cld = SecuritySupport.getContextClassLoader();
            if (cld == null) {
                cld = getClass().getClassLoader();
            }
            URL[] urls = cld != null ? SecuritySupport.getResources(cld, name) : SecuritySupport.getSystemResources(name);
            if (urls != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MailcapCommandMap: getResources");
                }
                for (URL url : urls) {
                    InputStream clis = null;
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("MailcapCommandMap: URL " + url);
                    }
                    try {
                        clis = SecuritySupport.openStream(url);
                        if (clis != null) {
                            v.add(new MailcapFile(clis));
                            anyLoaded = true;
                            if (LogSupport.isLoggable()) {
                                LogSupport.log("MailcapCommandMap: successfully loaded mailcap file from URL: " + url);
                            }
                        } else if (LogSupport.isLoggable()) {
                            LogSupport.log("MailcapCommandMap: not loading mailcap file from URL: " + url);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e) {
                            }
                        }
                    } catch (IOException ioex) {
                        if (LogSupport.isLoggable()) {
                            LogSupport.log("MailcapCommandMap: can't load " + url, ioex);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e2) {
                            }
                        }
                    } catch (SecurityException sex) {
                        if (LogSupport.isLoggable()) {
                            LogSupport.log("MailcapCommandMap: can't load " + url, sex);
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
                LogSupport.log("MailcapCommandMap: can't load " + name, ex);
            }
        }
        if (anyLoaded) {
            return;
        }
        if (LogSupport.isLoggable()) {
            LogSupport.log("MailcapCommandMap: !anyLoaded");
        }
        MailcapFile mf = loadResource("/" + name);
        if (mf != null) {
            v.add(mf);
        }
    }

    private MailcapFile loadFile(String name) {
        MailcapFile mtf = null;
        try {
            mtf = new MailcapFile(name);
        } catch (IOException e) {
        }
        return mtf;
    }

    public MailcapCommandMap(String fileName) throws IOException {
        this();
        if (LogSupport.isLoggable()) {
            LogSupport.log("MailcapCommandMap: load PROG from " + fileName);
        }
        if (this.DB[0] == null) {
            this.DB[0] = new MailcapFile(fileName);
        }
    }

    public MailcapCommandMap(InputStream is) {
        this();
        LogSupport.log("MailcapCommandMap: load PROG");
        if (this.DB[0] == null) {
            try {
                this.DB[0] = new MailcapFile(is);
            } catch (IOException e) {
            }
        }
    }

    @Override // javax.activation.CommandMap
    public synchronized CommandInfo[] getPreferredCommands(String mimeType) {
        Map cmdMap;
        Map cmdMap2;
        List cmdList = new ArrayList();
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null && (cmdMap2 = this.DB[i].getMailcapList(mimeType)) != null) {
                appendPrefCmdsToList(cmdMap2, cmdList);
            }
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null && (cmdMap = this.DB[i2].getMailcapFallbackList(mimeType)) != null) {
                appendPrefCmdsToList(cmdMap, cmdList);
            }
        }
        CommandInfo[] cmdInfos = new CommandInfo[cmdList.size()];
        return (CommandInfo[]) cmdList.toArray(cmdInfos);
    }

    private void appendPrefCmdsToList(Map cmdHash, List cmdList) {
        for (String verb : cmdHash.keySet()) {
            if (!checkForVerb(cmdList, verb)) {
                List cmdList2 = (List) cmdHash.get(verb);
                String className = (String) cmdList2.get(0);
                cmdList.add(new CommandInfo(verb, className));
            }
        }
    }

    private boolean checkForVerb(List cmdList, String verb) {
        Iterator ee = cmdList.iterator();
        while (ee.hasNext()) {
            String enum_verb = ((CommandInfo) ee.next()).getCommandName();
            if (enum_verb.equals(verb)) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.activation.CommandMap
    public synchronized CommandInfo[] getAllCommands(String mimeType) {
        Map cmdMap;
        Map cmdMap2;
        List cmdList = new ArrayList();
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null && (cmdMap2 = this.DB[i].getMailcapList(mimeType)) != null) {
                appendCmdsToList(cmdMap2, cmdList);
            }
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null && (cmdMap = this.DB[i2].getMailcapFallbackList(mimeType)) != null) {
                appendCmdsToList(cmdMap, cmdList);
            }
        }
        CommandInfo[] cmdInfos = new CommandInfo[cmdList.size()];
        return (CommandInfo[]) cmdList.toArray(cmdInfos);
    }

    private void appendCmdsToList(Map typeHash, List cmdList) {
        for (String verb : typeHash.keySet()) {
            List<String> cmdList2 = (List) typeHash.get(verb);
            for (String cmd : cmdList2) {
                cmdList.add(new CommandInfo(verb, cmd));
            }
        }
    }

    @Override // javax.activation.CommandMap
    public synchronized CommandInfo getCommand(String mimeType, String cmdName) {
        Map cmdMap;
        List v;
        String cmdClassName;
        Map cmdMap2;
        List v2;
        String cmdClassName2;
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null && (cmdMap2 = this.DB[i].getMailcapList(mimeType)) != null && (v2 = (List) cmdMap2.get(cmdName)) != null && (cmdClassName2 = (String) v2.get(0)) != null) {
                return new CommandInfo(cmdName, cmdClassName2);
            }
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null && (cmdMap = this.DB[i2].getMailcapFallbackList(mimeType)) != null && (v = (List) cmdMap.get(cmdName)) != null && (cmdClassName = (String) v.get(0)) != null) {
                return new CommandInfo(cmdName, cmdClassName);
            }
        }
        return null;
    }

    public synchronized void addMailcap(String mail_cap) {
        LogSupport.log("MailcapCommandMap: add to PROG");
        if (this.DB[0] == null) {
            this.DB[0] = new MailcapFile();
        }
        this.DB[0].appendToMailcap(mail_cap);
    }

    @Override // javax.activation.CommandMap
    public synchronized DataContentHandler createDataContentHandler(String mimeType) {
        List v;
        List v2;
        if (LogSupport.isLoggable()) {
            LogSupport.log("MailcapCommandMap: createDataContentHandler for " + mimeType);
        }
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("  search DB #" + i);
                }
                Map cmdMap = this.DB[i].getMailcapList(mimeType);
                if (cmdMap != null && (v2 = (List) cmdMap.get("content-handler")) != null) {
                    String name = (String) v2.get(0);
                    DataContentHandler dch = getDataContentHandler(name);
                    if (dch != null) {
                        return dch;
                    }
                }
            }
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("  search fallback DB #" + i2);
                }
                Map cmdMap2 = this.DB[i2].getMailcapFallbackList(mimeType);
                if (cmdMap2 != null && (v = (List) cmdMap2.get("content-handler")) != null) {
                    String name2 = (String) v.get(0);
                    DataContentHandler dch2 = getDataContentHandler(name2);
                    if (dch2 != null) {
                        return dch2;
                    }
                }
            }
        }
        return null;
    }

    private DataContentHandler getDataContentHandler(String name) {
        Class cl;
        if (LogSupport.isLoggable()) {
            LogSupport.log("    got content-handler");
        }
        if (LogSupport.isLoggable()) {
            LogSupport.log("      class " + name);
        }
        try {
            ClassLoader cld = SecuritySupport.getContextClassLoader();
            if (cld == null) {
                cld = getClass().getClassLoader();
            }
            try {
                cl = cld.loadClass(name);
            } catch (Exception e) {
                cl = Class.forName(name);
            }
            if (cl != null) {
                return (DataContentHandler) cl.newInstance();
            }
            return null;
        } catch (ClassNotFoundException e2) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("Can't load DCH " + name, e2);
                return null;
            }
            return null;
        } catch (IllegalAccessException e3) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("Can't load DCH " + name, e3);
                return null;
            }
            return null;
        } catch (InstantiationException e4) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("Can't load DCH " + name, e4);
                return null;
            }
            return null;
        }
    }

    @Override // javax.activation.CommandMap
    public synchronized String[] getMimeTypes() {
        String[] ts;
        List mtList = new ArrayList();
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null && (ts = this.DB[i].getMimeTypes()) != null) {
                for (int j = 0; j < ts.length; j++) {
                    if (!mtList.contains(ts[j])) {
                        mtList.add(ts[j]);
                    }
                }
            }
        }
        String[] mts = new String[mtList.size()];
        return (String[]) mtList.toArray(mts);
    }

    public synchronized String[] getNativeCommands(String mimeType) {
        String[] cmds;
        List cmdList = new ArrayList();
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (this.DB[i] != null && (cmds = this.DB[i].getNativeCommands(mimeType)) != null) {
                for (int j = 0; j < cmds.length; j++) {
                    if (!cmdList.contains(cmds[j])) {
                        cmdList.add(cmds[j]);
                    }
                }
            }
        }
        return (String[]) cmdList.toArray(new String[cmdList.size()]);
    }
}
