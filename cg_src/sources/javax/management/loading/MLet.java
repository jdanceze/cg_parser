package javax.management.loading;

import com.sun.jmx.trace.Trace;
import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanRegistration;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.ServiceNotFoundException;
import org.apache.tools.ant.launch.Launcher;
import polyglot.main.Report;
import soot.JavaBasicTypes;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/loading/MLet.class */
public class MLet extends URLClassLoader implements MLetMBean, MBeanRegistration, Externalizable {
    private static final long serialVersionUID = 3636148327800330130L;
    private MBeanServer server;
    private Vector mletList;
    private String libraryDirectory;
    private ObjectName mletObjectName;
    private URL[] myUrls;
    private static final String dbgTag = "MLet";
    private transient ClassLoaderRepository currentClr;
    private transient boolean delegateToCLR;
    private Hashtable primitiveClasses;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Character;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Short;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Float;
    static Class class$java$lang$Double;
    static Class class$java$lang$String;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: javax.management.loading.MLet.loadClass(java.lang.String, javax.management.loading.ClassLoaderRepository):java.lang.Class, file: gencallgraphv3.jar:j2ee.jar:javax/management/loading/MLet.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:115)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0xa8
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    public synchronized java.lang.Class loadClass(java.lang.String r1, javax.management.loading.ClassLoaderRepository r2) throws java.lang.ClassNotFoundException {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: javax.management.loading.MLet.loadClass(java.lang.String, javax.management.loading.ClassLoaderRepository):java.lang.Class, file: gencallgraphv3.jar:j2ee.jar:javax/management/loading/MLet.class
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.loading.MLet.loadClass(java.lang.String, javax.management.loading.ClassLoaderRepository):java.lang.Class");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: javax.management.loading.MLet.getTmpDir():java.lang.String, file: gencallgraphv3.jar:j2ee.jar:javax/management/loading/MLet.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:115)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0xa8
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    private java.lang.String getTmpDir() {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: javax.management.loading.MLet.getTmpDir():java.lang.String, file: gencallgraphv3.jar:j2ee.jar:javax/management/loading/MLet.class
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.loading.MLet.getTmpDir():java.lang.String");
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public MLet() {
        this(new URL[0]);
    }

    public MLet(URL[] urlArr) {
        this(urlArr, true);
    }

    public MLet(URL[] urlArr, ClassLoader classLoader) {
        this(urlArr, classLoader, true);
    }

    public MLet(URL[] urlArr, ClassLoader classLoader, URLStreamHandlerFactory uRLStreamHandlerFactory) {
        this(urlArr, classLoader, uRLStreamHandlerFactory, true);
    }

    public MLet(URL[] urlArr, boolean z) {
        super(urlArr);
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        this.server = null;
        this.mletList = new Vector();
        this.mletObjectName = null;
        this.myUrls = null;
        this.primitiveClasses = new Hashtable(8);
        Hashtable hashtable = this.primitiveClasses;
        String cls9 = Boolean.TYPE.toString();
        if (class$java$lang$Boolean == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_BOOLEAN);
            class$java$lang$Boolean = cls;
        } else {
            cls = class$java$lang$Boolean;
        }
        hashtable.put(cls9, cls);
        Hashtable hashtable2 = this.primitiveClasses;
        String cls10 = Character.TYPE.toString();
        if (class$java$lang$Character == null) {
            cls2 = class$(JavaBasicTypes.JAVA_LANG_CHARACTER);
            class$java$lang$Character = cls2;
        } else {
            cls2 = class$java$lang$Character;
        }
        hashtable2.put(cls10, cls2);
        Hashtable hashtable3 = this.primitiveClasses;
        String cls11 = Byte.TYPE.toString();
        if (class$java$lang$Byte == null) {
            cls3 = class$(JavaBasicTypes.JAVA_LANG_BYTE);
            class$java$lang$Byte = cls3;
        } else {
            cls3 = class$java$lang$Byte;
        }
        hashtable3.put(cls11, cls3);
        Hashtable hashtable4 = this.primitiveClasses;
        String cls12 = Short.TYPE.toString();
        if (class$java$lang$Short == null) {
            cls4 = class$(JavaBasicTypes.JAVA_LANG_SHORT);
            class$java$lang$Short = cls4;
        } else {
            cls4 = class$java$lang$Short;
        }
        hashtable4.put(cls12, cls4);
        Hashtable hashtable5 = this.primitiveClasses;
        String cls13 = Integer.TYPE.toString();
        if (class$java$lang$Integer == null) {
            cls5 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
            class$java$lang$Integer = cls5;
        } else {
            cls5 = class$java$lang$Integer;
        }
        hashtable5.put(cls13, cls5);
        Hashtable hashtable6 = this.primitiveClasses;
        String cls14 = Long.TYPE.toString();
        if (class$java$lang$Long == null) {
            cls6 = class$(JavaBasicTypes.JAVA_LANG_LONG);
            class$java$lang$Long = cls6;
        } else {
            cls6 = class$java$lang$Long;
        }
        hashtable6.put(cls14, cls6);
        Hashtable hashtable7 = this.primitiveClasses;
        String cls15 = Float.TYPE.toString();
        if (class$java$lang$Float == null) {
            cls7 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
            class$java$lang$Float = cls7;
        } else {
            cls7 = class$java$lang$Float;
        }
        hashtable7.put(cls15, cls7);
        Hashtable hashtable8 = this.primitiveClasses;
        String cls16 = Double.TYPE.toString();
        if (class$java$lang$Double == null) {
            cls8 = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls8;
        } else {
            cls8 = class$java$lang$Double;
        }
        hashtable8.put(cls16, cls8);
        init(z);
    }

    public MLet(URL[] urlArr, ClassLoader classLoader, boolean z) {
        super(urlArr, classLoader);
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        this.server = null;
        this.mletList = new Vector();
        this.mletObjectName = null;
        this.myUrls = null;
        this.primitiveClasses = new Hashtable(8);
        Hashtable hashtable = this.primitiveClasses;
        String cls9 = Boolean.TYPE.toString();
        if (class$java$lang$Boolean == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_BOOLEAN);
            class$java$lang$Boolean = cls;
        } else {
            cls = class$java$lang$Boolean;
        }
        hashtable.put(cls9, cls);
        Hashtable hashtable2 = this.primitiveClasses;
        String cls10 = Character.TYPE.toString();
        if (class$java$lang$Character == null) {
            cls2 = class$(JavaBasicTypes.JAVA_LANG_CHARACTER);
            class$java$lang$Character = cls2;
        } else {
            cls2 = class$java$lang$Character;
        }
        hashtable2.put(cls10, cls2);
        Hashtable hashtable3 = this.primitiveClasses;
        String cls11 = Byte.TYPE.toString();
        if (class$java$lang$Byte == null) {
            cls3 = class$(JavaBasicTypes.JAVA_LANG_BYTE);
            class$java$lang$Byte = cls3;
        } else {
            cls3 = class$java$lang$Byte;
        }
        hashtable3.put(cls11, cls3);
        Hashtable hashtable4 = this.primitiveClasses;
        String cls12 = Short.TYPE.toString();
        if (class$java$lang$Short == null) {
            cls4 = class$(JavaBasicTypes.JAVA_LANG_SHORT);
            class$java$lang$Short = cls4;
        } else {
            cls4 = class$java$lang$Short;
        }
        hashtable4.put(cls12, cls4);
        Hashtable hashtable5 = this.primitiveClasses;
        String cls13 = Integer.TYPE.toString();
        if (class$java$lang$Integer == null) {
            cls5 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
            class$java$lang$Integer = cls5;
        } else {
            cls5 = class$java$lang$Integer;
        }
        hashtable5.put(cls13, cls5);
        Hashtable hashtable6 = this.primitiveClasses;
        String cls14 = Long.TYPE.toString();
        if (class$java$lang$Long == null) {
            cls6 = class$(JavaBasicTypes.JAVA_LANG_LONG);
            class$java$lang$Long = cls6;
        } else {
            cls6 = class$java$lang$Long;
        }
        hashtable6.put(cls14, cls6);
        Hashtable hashtable7 = this.primitiveClasses;
        String cls15 = Float.TYPE.toString();
        if (class$java$lang$Float == null) {
            cls7 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
            class$java$lang$Float = cls7;
        } else {
            cls7 = class$java$lang$Float;
        }
        hashtable7.put(cls15, cls7);
        Hashtable hashtable8 = this.primitiveClasses;
        String cls16 = Double.TYPE.toString();
        if (class$java$lang$Double == null) {
            cls8 = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls8;
        } else {
            cls8 = class$java$lang$Double;
        }
        hashtable8.put(cls16, cls8);
        init(z);
    }

    public MLet(URL[] urlArr, ClassLoader classLoader, URLStreamHandlerFactory uRLStreamHandlerFactory, boolean z) {
        super(urlArr, classLoader, uRLStreamHandlerFactory);
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        this.server = null;
        this.mletList = new Vector();
        this.mletObjectName = null;
        this.myUrls = null;
        this.primitiveClasses = new Hashtable(8);
        Hashtable hashtable = this.primitiveClasses;
        String cls9 = Boolean.TYPE.toString();
        if (class$java$lang$Boolean == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_BOOLEAN);
            class$java$lang$Boolean = cls;
        } else {
            cls = class$java$lang$Boolean;
        }
        hashtable.put(cls9, cls);
        Hashtable hashtable2 = this.primitiveClasses;
        String cls10 = Character.TYPE.toString();
        if (class$java$lang$Character == null) {
            cls2 = class$(JavaBasicTypes.JAVA_LANG_CHARACTER);
            class$java$lang$Character = cls2;
        } else {
            cls2 = class$java$lang$Character;
        }
        hashtable2.put(cls10, cls2);
        Hashtable hashtable3 = this.primitiveClasses;
        String cls11 = Byte.TYPE.toString();
        if (class$java$lang$Byte == null) {
            cls3 = class$(JavaBasicTypes.JAVA_LANG_BYTE);
            class$java$lang$Byte = cls3;
        } else {
            cls3 = class$java$lang$Byte;
        }
        hashtable3.put(cls11, cls3);
        Hashtable hashtable4 = this.primitiveClasses;
        String cls12 = Short.TYPE.toString();
        if (class$java$lang$Short == null) {
            cls4 = class$(JavaBasicTypes.JAVA_LANG_SHORT);
            class$java$lang$Short = cls4;
        } else {
            cls4 = class$java$lang$Short;
        }
        hashtable4.put(cls12, cls4);
        Hashtable hashtable5 = this.primitiveClasses;
        String cls13 = Integer.TYPE.toString();
        if (class$java$lang$Integer == null) {
            cls5 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
            class$java$lang$Integer = cls5;
        } else {
            cls5 = class$java$lang$Integer;
        }
        hashtable5.put(cls13, cls5);
        Hashtable hashtable6 = this.primitiveClasses;
        String cls14 = Long.TYPE.toString();
        if (class$java$lang$Long == null) {
            cls6 = class$(JavaBasicTypes.JAVA_LANG_LONG);
            class$java$lang$Long = cls6;
        } else {
            cls6 = class$java$lang$Long;
        }
        hashtable6.put(cls14, cls6);
        Hashtable hashtable7 = this.primitiveClasses;
        String cls15 = Float.TYPE.toString();
        if (class$java$lang$Float == null) {
            cls7 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
            class$java$lang$Float = cls7;
        } else {
            cls7 = class$java$lang$Float;
        }
        hashtable7.put(cls15, cls7);
        Hashtable hashtable8 = this.primitiveClasses;
        String cls16 = Double.TYPE.toString();
        if (class$java$lang$Double == null) {
            cls8 = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls8;
        } else {
            cls8 = class$java$lang$Double;
        }
        hashtable8.put(cls16, cls8);
        init(z);
    }

    private void init(boolean z) {
        this.delegateToCLR = z;
        try {
            this.libraryDirectory = System.getProperty("jmx.mlet.library.dir");
            this.libraryDirectory = getTmpDir();
        } catch (SecurityException e) {
        }
    }

    @Override // java.net.URLClassLoader, javax.management.loading.MLetMBean
    public void addURL(URL url) {
        if (!Arrays.asList(getURLs()).contains(url)) {
            super.addURL(url);
        }
    }

    @Override // javax.management.loading.MLetMBean
    public void addURL(String str) throws ServiceNotFoundException {
        try {
            URL url = new URL(str);
            if (!Arrays.asList(getURLs()).contains(url)) {
                super.addURL(url);
            }
        } catch (MalformedURLException e) {
            debug("addURL", new StringBuffer().append(str).append(": Malformed URL. ").append(e).toString());
            throw new ServiceNotFoundException("The specified URL is malformed");
        }
    }

    @Override // java.net.URLClassLoader, javax.management.loading.MLetMBean
    public URL[] getURLs() {
        return super.getURLs();
    }

    @Override // javax.management.loading.MLetMBean
    public Set getMBeansFromURL(URL url) throws ServiceNotFoundException {
        if (url == null) {
            throw new ServiceNotFoundException("The specified URL is null");
        }
        return getMBeansFromURL(url.toString());
    }

    @Override // javax.management.loading.MLetMBean
    public Set getMBeansFromURL(String str) throws ServiceNotFoundException {
        ObjectInstance objectInstance;
        if (this.server == null) {
            throw new IllegalStateException("This MLet MBean is not registered with an MBeanServer.");
        }
        if (str == null) {
            if (isTraceOn()) {
                trace("getMBeansFromURL", "URL is null");
            }
            throw new ServiceNotFoundException("The specified URL is null");
        }
        String replace = str.replace(File.separatorChar, '/');
        if (isTraceOn()) {
            trace("getMBeansFromURL", new StringBuffer().append("<URL = ").append(replace).append(">").toString());
        }
        this.mletList = new MLetParser().parseURL(replace);
        if (this.mletList == null) {
            if (isTraceOn()) {
                trace("getMBeansFromURL", new StringBuffer().append("Problems while parsing URL ").append(replace).toString());
            }
            throw new ServiceNotFoundException(new StringBuffer().append("Problems while parsing URL ").append(replace).toString());
        } else if (this.mletList.size() == 0) {
            if (isTraceOn()) {
                trace("getMBeansFromURL", new StringBuffer().append("File ").append(replace).append(" not found or MLET tag not defined in file").toString());
            }
            throw new ServiceNotFoundException(new StringBuffer().append("File ").append(replace).append(" not found or MLET tag not defined in file").toString());
        } else {
            HashSet hashSet = new HashSet();
            Enumeration elements = this.mletList.elements();
            while (elements.hasMoreElements()) {
                MLetContent mLetContent = (MLetContent) elements.nextElement();
                String code = mLetContent.getCode();
                if (code != null && code.endsWith(".class")) {
                    code = code.substring(0, code.length() - 6);
                }
                String name = mLetContent.getName();
                URL codeBase = mLetContent.getCodeBase();
                String version = mLetContent.getVersion();
                String serializedObject = mLetContent.getSerializedObject();
                String jarFiles = mLetContent.getJarFiles();
                URL documentBase = mLetContent.getDocumentBase();
                Map attributes = mLetContent.getAttributes();
                if (isTraceOn()) {
                    trace("getMBeansFromURL", new StringBuffer().append("MLET TAG     = ").append(attributes.toString()).toString());
                    trace("getMBeansFromURL", new StringBuffer().append("CODEBASE     = ").append(codeBase).toString());
                    trace("getMBeansFromURL", new StringBuffer().append("ARCHIVE      = ").append(jarFiles).toString());
                    trace("getMBeansFromURL", new StringBuffer().append("CODE         = ").append(code).toString());
                    trace("getMBeansFromURL", new StringBuffer().append("OBJECT       = ").append(serializedObject).toString());
                    trace("getMBeansFromURL", new StringBuffer().append("NAME         = ").append(name).toString());
                    trace("getMBeansFromURL", new StringBuffer().append("VERSION      = ").append(version).toString());
                    trace("getMBeansFromURL", new StringBuffer().append("DOCUMENT URL = ").append(documentBase).toString());
                }
                StringTokenizer stringTokenizer = new StringTokenizer(jarFiles, ",", false);
                while (stringTokenizer.hasMoreTokens()) {
                    String trim = stringTokenizer.nextToken().trim();
                    if (isTraceOn()) {
                        trace("getMBeansFromURL", new StringBuffer().append("Load archive for codebase <").append(codeBase).append(">, file <").append(trim).append(">").toString());
                    }
                    try {
                        codeBase = check(version, codeBase, trim, mLetContent);
                        try {
                            if (!Arrays.asList(getURLs()).contains(new URL(new StringBuffer().append(codeBase.toString()).append(trim).toString()))) {
                                addURL(new StringBuffer().append(codeBase).append(trim).toString());
                            }
                        } catch (MalformedURLException e) {
                        }
                    } catch (Exception e2) {
                        if (isDebugOn()) {
                            debug("getMBeansFromURL", new StringBuffer().append("check returned exception: ").append(e2).toString());
                        }
                        hashSet.add(e2);
                    }
                }
                if (code != null && serializedObject != null) {
                    if (isTraceOn()) {
                        trace("getMBeansFromURL", "CODE and OBJECT parameters cannot be specified at the same time in tag MLET.");
                    }
                    hashSet.add(new Error("CODE and OBJECT parameters cannot be specified at the same time in tag MLET"));
                } else if (code == null && serializedObject == null) {
                    if (isTraceOn()) {
                        trace("getMBeansFromURL", "Either CODE or OBJECT parameter must be specified in tag MLET.");
                    }
                    hashSet.add(new Error("Either CODE or OBJECT parameter must be specified in tag MLET"));
                } else {
                    if (code != null) {
                        try {
                            Vector vector = new Vector();
                            Vector vector2 = new Vector();
                            for (String str2 : attributes.keySet()) {
                                if (str2.equals(Report.types)) {
                                    vector = (Vector) mLetContent.getParameter(str2);
                                }
                                if (str2.equals("values")) {
                                    vector2 = (Vector) mLetContent.getParameter(str2);
                                }
                            }
                            for (int i = 0; i < vector.size(); i++) {
                                vector2.setElementAt(constructParameter((String) vector2.elementAt(i), (String) vector.elementAt(i)), i);
                            }
                            if (vector.isEmpty()) {
                                if (name == null) {
                                    objectInstance = this.server.createMBean(code, null, this.mletObjectName);
                                } else {
                                    objectInstance = this.server.createMBean(code, new ObjectName(name), this.mletObjectName);
                                }
                            } else {
                                Object[] array = vector2.toArray();
                                String[] strArr = new String[vector.size()];
                                for (int i2 = 0; i2 < strArr.length; i2++) {
                                    strArr[i2] = (String) vector.elementAt(i2);
                                }
                                if (isDebugOn()) {
                                    for (int i3 = 0; i3 < strArr.length; i3++) {
                                        debug("getMBeansFromURL", new StringBuffer().append("Signature     = ").append(strArr[i3]).toString());
                                        debug("getMBeansFromURL", new StringBuffer().append("Params     = ").append(array[i3].toString()).toString());
                                    }
                                }
                                if (name == null) {
                                    objectInstance = this.server.createMBean(code, null, this.mletObjectName, array, strArr);
                                } else {
                                    objectInstance = this.server.createMBean(code, new ObjectName(name), this.mletObjectName, array, strArr);
                                }
                            }
                        } catch (IOException e3) {
                            if (isTraceOn()) {
                                trace("getMBeansFromURL", new StringBuffer().append("IOException: ").append(e3.getMessage()).toString());
                            }
                            hashSet.add(e3);
                        } catch (Error e4) {
                            if (isTraceOn()) {
                                trace("getMBeansFromURL", new StringBuffer().append("Error: ").append(e4.getMessage()).toString());
                            }
                            hashSet.add(e4);
                        } catch (SecurityException e5) {
                            if (isTraceOn()) {
                                trace("getMBeansFromURL", new StringBuffer().append("SecurityException: ").append(e5.getMessage()).toString());
                            }
                            hashSet.add(e5);
                        } catch (InstanceAlreadyExistsException e6) {
                            if (isTraceOn()) {
                                trace("getMBeansFromURL", new StringBuffer().append("InstanceAlreadyExistsException: ").append(e6.getMessage()).toString());
                            }
                            hashSet.add(e6);
                        } catch (InstanceNotFoundException e7) {
                            if (isTraceOn()) {
                                trace("getMBeansFromURL", new StringBuffer().append("InstanceNotFoundException: ").append(e7.getMessage()).toString());
                            }
                            hashSet.add(e7);
                        } catch (MBeanRegistrationException e8) {
                            if (isTraceOn()) {
                                trace("getMBeansFromURL", new StringBuffer().append("MBeanRegistrationException: ").append(e8.getMessage()).toString());
                            }
                            hashSet.add(e8);
                        } catch (MBeanException e9) {
                            if (isTraceOn()) {
                                trace("getMBeansFromURL", new StringBuffer().append("MBeanException: ").append(e9.getMessage()).toString());
                            }
                            hashSet.add(e9);
                        } catch (NotCompliantMBeanException e10) {
                            if (isTraceOn()) {
                                trace("getMBeansFromURL", new StringBuffer().append("NotCompliantMBeanException: ").append(e10.getMessage()).toString());
                            }
                            hashSet.add(e10);
                        } catch (ReflectionException e11) {
                            if (isTraceOn()) {
                                trace("getMBeansFromURL", new StringBuffer().append("ReflectionException: ").append(e11.getMessage()).toString());
                            }
                            hashSet.add(e11);
                        } catch (Exception e12) {
                            if (isTraceOn()) {
                                trace("getMBeansFromURL", new StringBuffer().append("Exception: ").append(e12.getClass().getName()).append(e12.getMessage()).toString());
                            }
                            hashSet.add(e12);
                        }
                    } else {
                        Object loadSerializedObject = loadSerializedObject(codeBase, serializedObject);
                        if (name == null) {
                            this.server.registerMBean(loadSerializedObject, null);
                        } else {
                            this.server.registerMBean(loadSerializedObject, new ObjectName(name));
                        }
                        objectInstance = new ObjectInstance(name, loadSerializedObject.getClass().getName());
                    }
                    hashSet.add(objectInstance);
                }
            }
            return hashSet;
        }
    }

    @Override // javax.management.loading.MLetMBean
    public String getLibraryDirectory() {
        return this.libraryDirectory;
    }

    @Override // javax.management.loading.MLetMBean
    public void setLibraryDirectory(String str) {
        this.libraryDirectory = str;
    }

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        setMBeanServer(mBeanServer);
        if (objectName == null) {
            objectName = new ObjectName(new StringBuffer().append(mBeanServer.getDefaultDomain()).append(":").append("type=MLet").toString());
        }
        this.mletObjectName = objectName;
        return this.mletObjectName;
    }

    @Override // javax.management.MBeanRegistration
    public void postRegister(Boolean bool) {
    }

    @Override // javax.management.MBeanRegistration
    public void preDeregister() throws Exception {
    }

    @Override // javax.management.MBeanRegistration
    public void postDeregister() {
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException, UnsupportedOperationException {
        throw new UnsupportedOperationException("MLet.writeExternal");
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException, UnsupportedOperationException {
        throw new UnsupportedOperationException("MLet.readExternal");
    }

    @Override // java.net.URLClassLoader, java.lang.ClassLoader
    protected Class findClass(String str) throws ClassNotFoundException {
        return findClass(str, this.currentClr);
    }

    Class findClass(String str, ClassLoaderRepository classLoaderRepository) throws ClassNotFoundException {
        Class<?> cls = null;
        if (isTraceOn()) {
            trace("findClass", str);
        }
        try {
            cls = super.findClass(str);
            if (isTraceOn()) {
                trace("findClass", new StringBuffer().append("Class ").append(str).append(" loaded through mlet classloader").toString());
            }
        } catch (ClassNotFoundException e) {
            debug("findClass", new StringBuffer().append("Class ").append(str).append(" not found locally.").toString());
        }
        if (cls == null && this.delegateToCLR && classLoaderRepository != null) {
            try {
                debug("findClass", new StringBuffer().append("Class ").append(str).append(": looking in CLR").toString());
                cls = classLoaderRepository.loadClassBefore(this, str);
                if (isTraceOn()) {
                    trace("findClass", new StringBuffer().append("Class ").append(str).append(" loaded through the default classloader repository").toString());
                }
            } catch (ClassNotFoundException e2) {
                debug("findClass", new StringBuffer().append("Class ").append(str).append(" not found in CLR.").toString());
            }
        }
        if (cls == null) {
            debug("findClass", new StringBuffer().append("Failed to load class ").append(str).toString());
            throw new ClassNotFoundException(str);
        }
        return cls;
    }

    @Override // java.lang.ClassLoader
    protected String findLibrary(String str) {
        String mapLibraryName = System.mapLibraryName(str);
        if (isTraceOn()) {
            trace("findLibrary", new StringBuffer().append("Search ").append(str).append(" in all JAR files.").toString());
        }
        if (isTraceOn()) {
            trace("findLibrary", new StringBuffer().append("loadLibraryAsResource(").append(mapLibraryName).append(")").toString());
        }
        String loadLibraryAsResource = loadLibraryAsResource(mapLibraryName);
        if (loadLibraryAsResource != null) {
            if (isTraceOn()) {
                trace("findLibrary", new StringBuffer().append(mapLibraryName).append(" loaded ").append("absolute path = ").append(loadLibraryAsResource).toString());
            }
            return loadLibraryAsResource;
        }
        String stringBuffer = new StringBuffer().append(removeSpace(System.getProperty("os.name"))).append(File.separator).append(removeSpace(System.getProperty("os.arch"))).append(File.separator).append(removeSpace(System.getProperty("os.version"))).append(File.separator).append(Launcher.ANT_PRIVATELIB).append(File.separator).append(mapLibraryName).toString();
        if (isTraceOn()) {
            trace("findLibrary", new StringBuffer().append("loadLibraryAsResource(").append(stringBuffer).append(")").toString());
        }
        String loadLibraryAsResource2 = loadLibraryAsResource(stringBuffer);
        if (loadLibraryAsResource2 != null) {
            if (isTraceOn()) {
                trace("findLibrary", new StringBuffer().append(stringBuffer).append(" loaded ").append("absolute path = ").append(loadLibraryAsResource2).toString());
            }
            return loadLibraryAsResource2;
        } else if (isTraceOn()) {
            trace("findLibrary", new StringBuffer().append(str).append(" not found in any JAR file.").toString());
            trace("findLibrary", new StringBuffer().append("Search ").append(str).append(" along the path specified as the java.library.path property.").toString());
            return null;
        } else {
            return null;
        }
    }

    private synchronized String loadLibraryAsResource(String str) {
        try {
            File file = new File(this.libraryDirectory, str);
            if (file.exists()) {
                file.delete();
            }
            InputStream resourceAsStream = getResourceAsStream(str.replace(File.separatorChar, '/'));
            if (resourceAsStream != null) {
                file.getParentFile().mkdirs();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while (true) {
                    int read = resourceAsStream.read();
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(read);
                }
                resourceAsStream.close();
                fileOutputStream.close();
                if (file.exists()) {
                    return file.getAbsolutePath();
                }
                return null;
            }
            return null;
        } catch (Exception e) {
            debug("loadLibraryAsResource", new StringBuffer().append(str).append(": Failed to load library. ").append(e).toString());
            return null;
        }
    }

    private String removeSpace(String str) {
        String stringBuffer;
        String trim = str.trim();
        int indexOf = trim.indexOf(32);
        if (indexOf == -1) {
            return trim;
        }
        String str2 = "";
        int i = 0;
        while (true) {
            int i2 = i;
            if (indexOf != -1) {
                trim = trim.substring(i2);
                indexOf = trim.indexOf(32);
                if (indexOf != -1) {
                    stringBuffer = new StringBuffer().append(str2).append(trim.substring(0, indexOf)).toString();
                } else {
                    stringBuffer = new StringBuffer().append(str2).append(trim.substring(0)).toString();
                }
                str2 = stringBuffer;
                i = indexOf + 1;
            } else {
                return str2;
            }
        }
    }

    protected URL check(String str, URL url, String str2, MLetContent mLetContent) throws Exception {
        return url;
    }

    private Object loadSerializedObject(URL url, String str) throws IOException, ClassNotFoundException {
        if (str != null) {
            str = str.replace(File.separatorChar, '/');
        }
        if (isTraceOn()) {
            trace("loadSerializedObject", new StringBuffer().append(url.toString()).append(str).toString());
        }
        InputStream resourceAsStream = getResourceAsStream(str);
        if (resourceAsStream != null) {
            try {
                MLetObjectInputStream mLetObjectInputStream = new MLetObjectInputStream(resourceAsStream, this);
                Object readObject = mLetObjectInputStream.readObject();
                mLetObjectInputStream.close();
                return readObject;
            } catch (IOException e) {
                if (isDebugOn()) {
                    debug("loadSerializedObject", new StringBuffer().append("Exception while deserializing ").append(str).append(", ").append(e.getMessage()).toString());
                }
                throw e;
            } catch (ClassNotFoundException e2) {
                if (isDebugOn()) {
                    debug("loadSerializedObject", new StringBuffer().append("Exception while deserializing ").append(str).append(", ").append(e2.getMessage()).toString());
                }
                throw e2;
            }
        }
        if (isDebugOn()) {
            debug("loadSerializedObject", new StringBuffer().append("Error: File ").append(str).append(" containing serialized object not found").toString());
        }
        throw new Error(new StringBuffer().append("File ").append(str).append(" containing serialized object not found").toString());
    }

    private Object constructParameter(String str, String str2) {
        Class<?> cls;
        Class cls2 = (Class) this.primitiveClasses.get(str2);
        if (cls2 != null) {
            try {
                Class<?>[] clsArr = new Class[1];
                if (class$java$lang$String == null) {
                    cls = class$("java.lang.String");
                    class$java$lang$String = cls;
                } else {
                    cls = class$java$lang$String;
                }
                clsArr[0] = cls;
                return cls2.getConstructor(clsArr).newInstance(str);
            } catch (Exception e) {
                if (isDebugOn()) {
                    debug(dbgTag, "constructParameter", new StringBuffer().append("Unexpected Exception").append(e.getClass().getName()).append(" occured").toString());
                }
            }
        }
        if (str2.compareTo(JavaBasicTypes.JAVA_LANG_BOOLEAN) == 0) {
            return new Boolean(str);
        }
        if (str2.compareTo(JavaBasicTypes.JAVA_LANG_BYTE) == 0) {
            return new Byte(str);
        }
        if (str2.compareTo(JavaBasicTypes.JAVA_LANG_SHORT) == 0) {
            return new Short(str);
        }
        if (str2.compareTo(JavaBasicTypes.JAVA_LANG_LONG) == 0) {
            return new Long(str);
        }
        if (str2.compareTo(JavaBasicTypes.JAVA_LANG_INTEGER) == 0) {
            return new Integer(str);
        }
        if (str2.compareTo(JavaBasicTypes.JAVA_LANG_FLOAT) == 0) {
            return new Float(str);
        }
        if (str2.compareTo(JavaBasicTypes.JAVA_LANG_DOUBLE) == 0) {
            return new Double(str);
        }
        if (str2.compareTo("java.lang.String") == 0) {
            return str;
        }
        return str;
    }

    private synchronized void setMBeanServer(MBeanServer mBeanServer) {
        this.server = mBeanServer;
        this.currentClr = mBeanServer.getClassLoaderRepository();
    }

    private boolean isTraceOn() {
        return Trace.isSelected(1, 2);
    }

    private void trace(String str, String str2, String str3) {
        Trace.send(1, 2, str, str2, str3);
    }

    private void trace(String str, String str2) {
        trace(dbgTag, str, str2);
    }

    private boolean isDebugOn() {
        return Trace.isSelected(2, 2);
    }

    private void debug(String str, String str2, String str3) {
        Trace.send(2, 2, str, str2, str3);
    }

    private void debug(String str, String str2) {
        debug(dbgTag, str, str2);
    }
}
