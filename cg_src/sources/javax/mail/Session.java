package javax.mail;

import com.sun.mail.util.LineInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.mail.Provider;
import org.apache.tools.ant.launch.Launcher;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/Session.class */
public final class Session {
    private final Properties props;
    private final Authenticator authenticator;
    private boolean debug;
    private PrintStream out;
    private static Session defaultSession = null;
    private static final String version = "1.3.1ea";
    static Class class$javax$mail$Session;
    static Class class$javax$mail$URLName;
    private final Hashtable authTable = new Hashtable();
    private final Vector providers = new Vector();
    private final Hashtable providersByProtocol = new Hashtable();
    private final Hashtable providersByClassName = new Hashtable();
    private final Properties addressMap = new Properties();

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: javax.mail.Session.loadFile(java.lang.String, javax.mail.StreamLoader):void, file: gencallgraphv3.jar:j2ee.jar:javax/mail/Session.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:107)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0xa8
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    private void loadFile(java.lang.String r1, javax.mail.StreamLoader r2) {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: javax.mail.Session.loadFile(java.lang.String, javax.mail.StreamLoader):void, file: gencallgraphv3.jar:j2ee.jar:javax/mail/Session.class
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.Session.loadFile(java.lang.String, javax.mail.StreamLoader):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: javax.mail.Session.loadResource(java.lang.String, java.lang.Class, javax.mail.StreamLoader):void, file: gencallgraphv3.jar:j2ee.jar:javax/mail/Session.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:107)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0xa8
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    private void loadResource(java.lang.String r1, java.lang.Class r2, javax.mail.StreamLoader r3) {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: javax.mail.Session.loadResource(java.lang.String, java.lang.Class, javax.mail.StreamLoader):void, file: gencallgraphv3.jar:j2ee.jar:javax/mail/Session.class
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.Session.loadResource(java.lang.String, java.lang.Class, javax.mail.StreamLoader):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: javax.mail.Session.loadAllResources(java.lang.String, java.lang.Class, javax.mail.StreamLoader):void, file: gencallgraphv3.jar:j2ee.jar:javax/mail/Session.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:107)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0xa8
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    private void loadAllResources(java.lang.String r1, java.lang.Class r2, javax.mail.StreamLoader r3) {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: javax.mail.Session.loadAllResources(java.lang.String, java.lang.Class, javax.mail.StreamLoader):void, file: gencallgraphv3.jar:j2ee.jar:javax/mail/Session.class
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.Session.loadAllResources(java.lang.String, java.lang.Class, javax.mail.StreamLoader):void");
    }

    private Session(Properties props, Authenticator authenticator) {
        Class cl;
        this.debug = false;
        this.props = props;
        this.authenticator = authenticator;
        if (Boolean.valueOf(props.getProperty("mail.debug")).booleanValue()) {
            this.debug = true;
        }
        if (this.debug) {
            pr("DEBUG: JavaMail version 1.3.1ea");
        }
        if (authenticator != null) {
            cl = authenticator.getClass();
        } else {
            cl = getClass();
        }
        loadProviders(cl);
        loadAddressMap(cl);
    }

    public static Session getInstance(Properties props, Authenticator authenticator) {
        return new Session(props, authenticator);
    }

    public static Session getInstance(Properties props) {
        return new Session(props, null);
    }

    public static synchronized Session getDefaultInstance(Properties props, Authenticator authenticator) {
        if (defaultSession == null) {
            defaultSession = new Session(props, authenticator);
        } else if (defaultSession.authenticator != authenticator && (defaultSession.authenticator == null || authenticator == null || defaultSession.authenticator.getClass().getClassLoader() != authenticator.getClass().getClassLoader())) {
            throw new SecurityException("Access to default session denied");
        }
        return defaultSession;
    }

    public static Session getDefaultInstance(Properties props) {
        return getDefaultInstance(props, null);
    }

    public synchronized void setDebug(boolean debug) {
        this.debug = debug;
        if (debug) {
            pr("DEBUG: setDebug: JavaMail version 1.3.1ea");
        }
    }

    public synchronized boolean getDebug() {
        return this.debug;
    }

    public synchronized void setDebugOut(PrintStream out) {
        this.out = out;
    }

    public synchronized PrintStream getDebugOut() {
        if (this.out == null) {
            return System.out;
        }
        return this.out;
    }

    public synchronized Provider[] getProviders() {
        Provider[] _providers = new Provider[this.providers.size()];
        this.providers.copyInto(_providers);
        return _providers;
    }

    public synchronized Provider getProvider(String protocol) throws NoSuchProviderException {
        if (protocol == null || protocol.length() <= 0) {
            throw new NoSuchProviderException("Invalid protocol: null");
        }
        Provider _provider = null;
        String _className = this.props.getProperty(new StringBuffer().append("mail.").append(protocol).append(".class").toString());
        if (_className != null) {
            if (this.debug) {
                pr(new StringBuffer().append("DEBUG: mail.").append(protocol).append(".class property exists and points to ").append(_className).toString());
            }
            _provider = (Provider) this.providersByClassName.get(_className);
        }
        if (_provider != null) {
            return _provider;
        }
        Provider _provider2 = (Provider) this.providersByProtocol.get(protocol);
        if (_provider2 == null) {
            throw new NoSuchProviderException(new StringBuffer().append("No provider for ").append(protocol).toString());
        }
        if (this.debug) {
            pr(new StringBuffer().append("DEBUG: getProvider() returning ").append(_provider2.toString()).toString());
        }
        return _provider2;
    }

    public synchronized void setProvider(Provider provider) throws NoSuchProviderException {
        if (provider == null) {
            throw new NoSuchProviderException("Can't set null provider");
        }
        this.providersByProtocol.put(provider.getProtocol(), provider);
        this.props.put(new StringBuffer().append("mail.").append(provider.getProtocol()).append(".class").toString(), provider.getClassName());
    }

    public Store getStore() throws NoSuchProviderException {
        return getStore(getProperty("mail.store.protocol"));
    }

    public Store getStore(String protocol) throws NoSuchProviderException {
        return getStore(new URLName(protocol, null, -1, null, null, null));
    }

    public Store getStore(URLName url) throws NoSuchProviderException {
        String protocol = url.getProtocol();
        Provider p = getProvider(protocol);
        return getStore(p, url);
    }

    public Store getStore(Provider provider) throws NoSuchProviderException {
        return getStore(provider, null);
    }

    private Store getStore(Provider provider, URLName url) throws NoSuchProviderException {
        if (provider == null || provider.getType() != Provider.Type.STORE) {
            throw new NoSuchProviderException("invalid provider");
        }
        try {
            return (Store) getService(provider, url);
        } catch (ClassCastException e) {
            throw new NoSuchProviderException("incorrect class");
        }
    }

    public Folder getFolder(URLName url) throws MessagingException {
        Store store = getStore(url);
        store.connect();
        return store.getFolder(url);
    }

    public Transport getTransport() throws NoSuchProviderException {
        return getTransport(getProperty("mail.transport.protocol"));
    }

    public Transport getTransport(String protocol) throws NoSuchProviderException {
        return getTransport(new URLName(protocol, null, -1, null, null, null));
    }

    public Transport getTransport(URLName url) throws NoSuchProviderException {
        String protocol = url.getProtocol();
        Provider p = getProvider(protocol);
        return getTransport(p, url);
    }

    public Transport getTransport(Provider provider) throws NoSuchProviderException {
        return getTransport(provider, null);
    }

    public Transport getTransport(Address address) throws NoSuchProviderException {
        String transportProtocol = (String) this.addressMap.get(address.getType());
        if (transportProtocol == null) {
            throw new NoSuchProviderException(new StringBuffer().append("No provider for Address type: ").append(address.getType()).toString());
        }
        return getTransport(transportProtocol);
    }

    private Transport getTransport(Provider provider, URLName url) throws NoSuchProviderException {
        if (provider == null || provider.getType() != Provider.Type.TRANSPORT) {
            throw new NoSuchProviderException("invalid provider");
        }
        try {
            return (Transport) getService(provider, url);
        } catch (ClassCastException e) {
            throw new NoSuchProviderException("incorrect class");
        }
    }

    private Object getService(Provider provider, URLName url) throws NoSuchProviderException {
        ClassLoader cl;
        Class cls;
        Class cls2;
        if (provider == null) {
            throw new NoSuchProviderException(Jimple.NULL);
        }
        if (url == null) {
            url = new URLName(provider.getProtocol(), null, -1, null, null, null);
        }
        if (this.authenticator != null) {
            cl = this.authenticator.getClass().getClassLoader();
        } else {
            cl = getClass().getClassLoader();
        }
        Class serviceClass = null;
        try {
            ClassLoader ccl = SecuritySupport.getInstance().getContextClassLoader();
            if (ccl != null) {
                try {
                    serviceClass = ccl.loadClass(provider.getClassName());
                } catch (ClassNotFoundException e) {
                }
            }
            if (serviceClass == null) {
                serviceClass = cl.loadClass(provider.getClassName());
            }
        } catch (Exception e2) {
            try {
                serviceClass = Class.forName(provider.getClassName());
            } catch (Exception ex) {
                if (this.debug) {
                    ex.printStackTrace(getDebugOut());
                }
                throw new NoSuchProviderException(provider.getProtocol());
            }
        }
        try {
            Class[] c = new Class[2];
            if (class$javax$mail$Session == null) {
                cls = class$("javax.mail.Session");
                class$javax$mail$Session = cls;
            } else {
                cls = class$javax$mail$Session;
            }
            c[0] = cls;
            if (class$javax$mail$URLName == null) {
                cls2 = class$("javax.mail.URLName");
                class$javax$mail$URLName = cls2;
            } else {
                cls2 = class$javax$mail$URLName;
            }
            c[1] = cls2;
            Constructor cons = serviceClass.getConstructor(c);
            Object[] o = {this, url};
            Object service = cons.newInstance(o);
            return service;
        } catch (Exception ex2) {
            if (this.debug) {
                ex2.printStackTrace(getDebugOut());
            }
            throw new NoSuchProviderException(provider.getProtocol());
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public void setPasswordAuthentication(URLName url, PasswordAuthentication pw) {
        if (pw == null) {
            this.authTable.remove(url);
        } else {
            this.authTable.put(url, pw);
        }
    }

    public PasswordAuthentication getPasswordAuthentication(URLName url) {
        return (PasswordAuthentication) this.authTable.get(url);
    }

    public PasswordAuthentication requestPasswordAuthentication(InetAddress addr, int port, String protocol, String prompt, String defaultUserName) {
        if (this.authenticator != null) {
            return this.authenticator.requestPasswordAuthentication(addr, port, protocol, prompt, defaultUserName);
        }
        return null;
    }

    public Properties getProperties() {
        return this.props;
    }

    public String getProperty(String name) {
        return this.props.getProperty(name);
    }

    private void loadProviders(Class cl) {
        StreamLoader loader = new StreamLoader(this) { // from class: javax.mail.Session.1
            private final Session this$0;

            {
                this.this$0 = this;
            }

            @Override // javax.mail.StreamLoader
            public void load(InputStream is) throws IOException {
                this.this$0.loadProvidersFromStream(is);
            }
        };
        try {
            String res = new StringBuffer().append(System.getProperty("java.home")).append(File.separator).append(Launcher.ANT_PRIVATELIB).append(File.separator).append("javamail.providers").toString();
            loadFile(res, loader);
        } catch (SecurityException sex) {
            if (this.debug) {
                pr(new StringBuffer().append("DEBUG: can't get java.home: ").append(sex).toString());
            }
        }
        loadAllResources("META-INF/javamail.providers", cl, loader);
        loadResource("/META-INF/javamail.default.providers", cl, loader);
        if (this.providers.size() == 0) {
            if (this.debug) {
                pr("DEBUG: failed to load any providers, using defaults");
            }
            addProvider(new Provider(Provider.Type.STORE, "imap", "com.sun.mail.imap.IMAPStore", "Sun Microsystems, Inc.", version));
            addProvider(new Provider(Provider.Type.STORE, "pop3", "com.sun.mail.pop3.POP3Store", "Sun Microsystems, Inc.", version));
            addProvider(new Provider(Provider.Type.TRANSPORT, "smtp", "com.sun.mail.smtp.SMTPTransport", "Sun Microsystems, Inc.", version));
        }
        if (this.debug) {
            pr("DEBUG: Tables of loaded providers");
            pr(new StringBuffer().append("DEBUG: Providers Listed By Class Name: ").append(this.providersByClassName.toString()).toString());
            pr(new StringBuffer().append("DEBUG: Providers Listed By Protocol: ").append(this.providersByProtocol.toString()).toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadProvidersFromStream(InputStream is) throws IOException {
        if (is != null) {
            LineInputStream lis = new LineInputStream(is);
            while (true) {
                String readLine = lis.readLine();
                if (readLine != null) {
                    if (!readLine.startsWith("#")) {
                        Provider.Type type = null;
                        String protocol = null;
                        String className = null;
                        String vendor = null;
                        String version2 = null;
                        StringTokenizer tuples = new StringTokenizer(readLine, ";");
                        while (tuples.hasMoreTokens()) {
                            String currTuple = tuples.nextToken().trim();
                            int sep = currTuple.indexOf("=");
                            if (currTuple.startsWith("protocol=")) {
                                protocol = currTuple.substring(sep + 1);
                            } else if (currTuple.startsWith("type=")) {
                                String strType = currTuple.substring(sep + 1);
                                if (strType.equalsIgnoreCase("store")) {
                                    type = Provider.Type.STORE;
                                } else if (strType.equalsIgnoreCase("transport")) {
                                    type = Provider.Type.TRANSPORT;
                                }
                            } else if (currTuple.startsWith("class=")) {
                                className = currTuple.substring(sep + 1);
                            } else if (currTuple.startsWith("vendor=")) {
                                vendor = currTuple.substring(sep + 1);
                            } else if (currTuple.startsWith("version=")) {
                                version2 = currTuple.substring(sep + 1);
                            }
                        }
                        if (type == null || protocol == null || className == null || protocol.length() <= 0 || className.length() <= 0) {
                            if (this.debug) {
                                pr(new StringBuffer().append("DEBUG: Bad provider entry: ").append(readLine).toString());
                            }
                        } else {
                            Provider provider = new Provider(type, protocol, className, vendor, version2);
                            addProvider(provider);
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    private void addProvider(Provider provider) {
        this.providers.addElement(provider);
        this.providersByClassName.put(provider.getClassName(), provider);
        if (!this.providersByProtocol.containsKey(provider.getProtocol())) {
            this.providersByProtocol.put(provider.getProtocol(), provider);
        }
    }

    private void loadAddressMap(Class cl) {
        StreamLoader loader = new StreamLoader(this) { // from class: javax.mail.Session.2
            private final Session this$0;

            {
                this.this$0 = this;
            }

            @Override // javax.mail.StreamLoader
            public void load(InputStream is) throws IOException {
                this.this$0.addressMap.load(is);
            }
        };
        loadResource("/META-INF/javamail.default.address.map", cl, loader);
        loadAllResources("META-INF/javamail.address.map", cl, loader);
        try {
            String res = new StringBuffer().append(System.getProperty("java.home")).append(File.separator).append(Launcher.ANT_PRIVATELIB).append(File.separator).append("javamail.address.map").toString();
            loadFile(res, loader);
        } catch (SecurityException sex) {
            if (this.debug) {
                pr(new StringBuffer().append("DEBUG: can't get java.home: ").append(sex).toString());
            }
        }
        if (this.addressMap.isEmpty()) {
            if (this.debug) {
                pr("DEBUG: failed to load address map, using defaults");
            }
            this.addressMap.put("rfc822", "smtp");
        }
    }

    private void pr(String str) {
        getDebugOut().println(str);
    }
}
