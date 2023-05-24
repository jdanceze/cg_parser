package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.stream.Stream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.tools.ant.taskdefs.rmic.DefaultRmicAdapter;
import org.apache.tools.ant.util.StringUtils;
import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/IPlanetEjbc.class */
public class IPlanetEjbc {
    private static final int MIN_NUM_ARGS = 2;
    private static final int MAX_NUM_ARGS = 8;
    private static final int NUM_CLASSES_WITH_IIOP = 15;
    private static final int NUM_CLASSES_WITHOUT_IIOP = 9;
    private static final String ENTITY_BEAN = "entity";
    private static final String STATELESS_SESSION = "stateless";
    private static final String STATEFUL_SESSION = "stateful";
    private File stdDescriptor;
    private File iasDescriptor;
    private File destDirectory;
    private String classpath;
    private String[] classpathElements;
    private File iasHomeDir;
    private SAXParser parser;
    private String displayName;
    private boolean retainSource = false;
    private boolean debugOutput = false;
    private EjbcHandler handler = new EjbcHandler();
    private Hashtable<String, File> ejbFiles = new Hashtable<>();

    public IPlanetEjbc(File stdDescriptor, File iasDescriptor, File destDirectory, String classpath, SAXParser parser) {
        this.stdDescriptor = stdDescriptor;
        this.iasDescriptor = iasDescriptor;
        this.destDirectory = destDirectory;
        this.classpath = classpath;
        this.parser = parser;
        if (classpath != null) {
            StringTokenizer st = new StringTokenizer(classpath, File.pathSeparator);
            int count = st.countTokens();
            this.classpathElements = (String[]) Collections.list(st).toArray(new String[count]);
        }
    }

    public void setRetainSource(boolean retainSource) {
        this.retainSource = retainSource;
    }

    public void setDebugOutput(boolean debugOutput) {
        this.debugOutput = debugOutput;
    }

    public void registerDTD(String publicID, String location) {
        this.handler.registerDTD(publicID, location);
    }

    public void setIasHomeDir(File iasHomeDir) {
        this.iasHomeDir = iasHomeDir;
    }

    public Hashtable<String, File> getEjbFiles() {
        return this.ejbFiles;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String[] getCmpDescriptors() {
        return (String[]) Stream.of((Object[]) this.handler.getEjbs()).map((v0) -> {
            return v0.getCmpDescriptors();
        }).flatMap((v0) -> {
            return v0.stream();
        }).toArray(x$0 -> {
            return new String[x$0];
        });
    }

    public static void main(String[] args) {
        File destDirectory = null;
        String classpath = null;
        boolean debug = false;
        boolean retainSource = false;
        if (args.length < 2 || args.length > 8) {
            usage();
            return;
        }
        File stdDescriptor = new File(args[args.length - 2]);
        File iasDescriptor = new File(args[args.length - 1]);
        int i = 0;
        while (i < args.length - 2) {
            if ("-classpath".equals(args[i])) {
                i++;
                classpath = args[i];
            } else if ("-d".equals(args[i])) {
                i++;
                destDirectory = new File(args[i]);
            } else if ("-debug".equals(args[i])) {
                debug = true;
            } else if ("-keepsource".equals(args[i])) {
                retainSource = true;
            } else {
                usage();
                return;
            }
            i++;
        }
        if (classpath == null) {
            Properties props = System.getProperties();
            classpath = props.getProperty("java.class.path");
        }
        if (destDirectory == null) {
            Properties props2 = System.getProperties();
            destDirectory = new File(props2.getProperty("user.dir"));
        }
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setValidating(true);
        try {
            SAXParser parser = parserFactory.newSAXParser();
            IPlanetEjbc ejbc = new IPlanetEjbc(stdDescriptor, iasDescriptor, destDirectory, classpath, parser);
            ejbc.setDebugOutput(debug);
            ejbc.setRetainSource(retainSource);
            try {
                ejbc.execute();
            } catch (IOException e) {
                System.out.println("An IOException has occurred while reading the XML descriptors (" + e.getMessage() + ").");
            } catch (EjbcException e2) {
                System.out.println("An error has occurred while executing the ejbc utility (" + e2.getMessage() + ").");
            } catch (SAXException e3) {
                System.out.println("A SAXException has occurred while reading the XML descriptors (" + e3.getMessage() + ").");
            }
        } catch (Exception e4) {
            System.out.println("An exception was generated while trying to ");
            System.out.println("create a new SAXParser.");
            e4.printStackTrace();
        }
    }

    private static void usage() {
        System.out.println("java org.apache.tools.ant.taskdefs.optional.ejb.IPlanetEjbc \\");
        System.out.println("  [OPTIONS] [EJB 1.1 descriptor] [iAS EJB descriptor]");
        System.out.println();
        System.out.println("Where OPTIONS are:");
        System.out.println("  -debug -- for additional debugging output");
        System.out.println("  -keepsource -- to retain Java source files generated");
        System.out.println("  -classpath [classpath] -- classpath used for compilation");
        System.out.println("  -d [destination directory] -- directory for compiled classes");
        System.out.println();
        System.out.println("If a classpath is not specified, the system classpath");
        System.out.println("will be used.  If a destination directory is not specified,");
        System.out.println("the current working directory will be used (classes will");
        System.out.println("still be placed in subfolders which correspond to their");
        System.out.println("package name).");
        System.out.println();
        System.out.println("The EJB home interface, remote interface, and implementation");
        System.out.println("class must be found in the destination directory.  In");
        System.out.println("addition, the destination will look for the stubs and skeletons");
        System.out.println("in the destination directory to ensure they are up to date.");
    }

    public void execute() throws EjbcException, IOException, SAXException {
        checkConfiguration();
        EjbInfo[] ejbs = getEjbs();
        for (EjbInfo ejb : ejbs) {
            log("EJBInfo...");
            log(ejb.toString());
        }
        for (EjbInfo ejb2 : ejbs) {
            ejb2.checkConfiguration(this.destDirectory);
            if (ejb2.mustBeRecompiled(this.destDirectory)) {
                log(ejb2.getName() + " must be recompiled using ejbc.");
                callEjbc(buildArgumentList(ejb2));
            } else {
                log(ejb2.getName() + " is up to date.");
            }
        }
    }

    private void callEjbc(String[] arguments) {
        String command;
        if (this.iasHomeDir == null) {
            command = "";
        } else {
            command = this.iasHomeDir.toString() + File.separator + "bin" + File.separator;
        }
        String command2 = command + "ejbc ";
        String args = String.join(Instruction.argsep, arguments);
        log(command2 + args);
        try {
            Process p = Runtime.getRuntime().exec(command2 + args);
            RedirectOutput output = new RedirectOutput(p.getInputStream());
            RedirectOutput error = new RedirectOutput(p.getErrorStream());
            output.start();
            error.start();
            p.waitFor();
            p.destroy();
        } catch (IOException e) {
            log("An IOException has occurred while trying to execute ejbc.");
            log(StringUtils.getStackTrace(e));
        } catch (InterruptedException e2) {
        }
    }

    protected void checkConfiguration() throws EjbcException {
        StringBuilder msg = new StringBuilder();
        if (this.stdDescriptor == null) {
            msg.append("A standard XML descriptor file must be specified.  ");
        }
        if (this.iasDescriptor == null) {
            msg.append("An iAS-specific XML descriptor file must be specified.  ");
        }
        if (this.classpath == null) {
            msg.append("A classpath must be specified.    ");
        }
        if (this.parser == null) {
            msg.append("An XML parser must be specified.    ");
        }
        if (this.destDirectory == null) {
            msg.append("A destination directory must be specified.  ");
        } else if (!this.destDirectory.exists()) {
            msg.append("The destination directory specified does not exist.  ");
        } else if (!this.destDirectory.isDirectory()) {
            msg.append("The destination specified is not a directory.  ");
        }
        if (msg.length() > 0) {
            throw new EjbcException(msg.toString());
        }
    }

    private EjbInfo[] getEjbs() throws IOException, SAXException {
        this.parser.parse(this.stdDescriptor, this.handler);
        this.parser.parse(this.iasDescriptor, this.handler);
        return this.handler.getEjbs();
    }

    private String[] buildArgumentList(EjbInfo ejb) {
        List<String> arguments = new ArrayList<>();
        if (this.debugOutput) {
            arguments.add("-debug");
        }
        if (ejb.getBeantype().equals(STATELESS_SESSION)) {
            arguments.add("-sl");
        } else if (ejb.getBeantype().equals(STATEFUL_SESSION)) {
            arguments.add("-sf");
        }
        if (ejb.getIiop()) {
            arguments.add("-iiop");
        }
        if (ejb.getCmp()) {
            arguments.add("-cmp");
        }
        if (this.retainSource) {
            arguments.add("-gs");
        }
        if (ejb.getHasession()) {
            arguments.add("-fo");
        }
        arguments.add("-classpath");
        arguments.add(this.classpath);
        arguments.add("-d");
        arguments.add(this.destDirectory.toString());
        arguments.add(ejb.getHome().getQualifiedClassName());
        arguments.add(ejb.getRemote().getQualifiedClassName());
        arguments.add(ejb.getImplementation().getQualifiedClassName());
        return (String[]) arguments.toArray(new String[arguments.size()]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String msg) {
        if (this.debugOutput) {
            System.out.println(msg);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/IPlanetEjbc$EjbcException.class */
    public class EjbcException extends Exception {
        private static final long serialVersionUID = 1;

        public EjbcException(String msg) {
            super(msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/IPlanetEjbc$EjbcHandler.class */
    public class EjbcHandler extends HandlerBase {
        private static final String PUBLICID_EJB11 = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN";
        private static final String PUBLICID_IPLANET_EJB_60 = "-//Sun Microsystems, Inc.//DTD iAS Enterprise JavaBeans 1.0//EN";
        private static final String DEFAULT_IAS60_EJB11_DTD_LOCATION = "ejb-jar_1_1.dtd";
        private static final String DEFAULT_IAS60_DTD_LOCATION = "IASEjb_jar_1_0.dtd";
        private EjbInfo currentEjb;
        private String currentText;
        private String ejbType;
        private Map<String, String> resourceDtds = new HashMap();
        private Map<String, String> fileDtds = new HashMap();
        private Map<String, EjbInfo> ejbs = new HashMap();
        private boolean iasDescriptor = false;
        private String currentLoc = "";

        public EjbcHandler() {
            registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", DEFAULT_IAS60_EJB11_DTD_LOCATION);
            registerDTD(PUBLICID_IPLANET_EJB_60, DEFAULT_IAS60_DTD_LOCATION);
        }

        public EjbInfo[] getEjbs() {
            return (EjbInfo[]) this.ejbs.values().toArray(new EjbInfo[this.ejbs.size()]);
        }

        public String getDisplayName() {
            return IPlanetEjbc.this.displayName;
        }

        public void registerDTD(String publicID, String location) {
            IPlanetEjbc.this.log("Registering: " + location);
            if (publicID == null || location == null) {
                return;
            }
            if (ClassLoader.getSystemResource(location) != null) {
                IPlanetEjbc.this.log("Found resource: " + location);
                this.resourceDtds.put(publicID, location);
                return;
            }
            File dtdFile = new File(location);
            if (dtdFile.exists() && dtdFile.isFile()) {
                IPlanetEjbc.this.log("Found file: " + location);
                this.fileDtds.put(publicID, location);
            }
        }

        @Override // org.xml.sax.HandlerBase, org.xml.sax.EntityResolver
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
            InputStream inputStream = null;
            try {
                String location = this.resourceDtds.get(publicId);
                if (location != null) {
                    inputStream = ClassLoader.getSystemResource(location).openStream();
                } else {
                    String location2 = this.fileDtds.get(publicId);
                    if (location2 != null) {
                        inputStream = Files.newInputStream(Paths.get(location2, new String[0]), new OpenOption[0]);
                    }
                }
            } catch (IOException e) {
            }
            if (inputStream == null) {
                return super.resolveEntity(publicId, systemId);
            }
            return new InputSource(inputStream);
        }

        @Override // org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void startElement(String name, AttributeList atts) throws SAXException {
            this.currentLoc += "\\" + name;
            this.currentText = "";
            if ("\\ejb-jar".equals(this.currentLoc)) {
                this.iasDescriptor = false;
            } else if ("\\ias-ejb-jar".equals(this.currentLoc)) {
                this.iasDescriptor = true;
            }
            if ("session".equals(name) || "entity".equals(name)) {
                this.ejbType = name;
            }
        }

        @Override // org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void characters(char[] ch, int start, int len) throws SAXException {
            this.currentText += new String(ch).substring(start, start + len);
        }

        @Override // org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void endElement(String name) throws SAXException {
            if (this.iasDescriptor) {
                iasCharacters(this.currentText);
            } else {
                stdCharacters(this.currentText);
            }
            int nameLength = name.length() + 1;
            int locLength = this.currentLoc.length();
            this.currentLoc = this.currentLoc.substring(0, locLength - nameLength);
        }

        private void stdCharacters(String value) {
            if ("\\ejb-jar\\display-name".equals(this.currentLoc)) {
                IPlanetEjbc.this.displayName = value;
                return;
            }
            String base = "\\ejb-jar\\enterprise-beans\\" + this.ejbType;
            if ((base + "\\ejb-name").equals(this.currentLoc)) {
                this.currentEjb = this.ejbs.computeIfAbsent(value, x$0 -> {
                    return new EjbInfo(x$0);
                });
            } else if ((base + "\\home").equals(this.currentLoc)) {
                this.currentEjb.setHome(value);
            } else if ((base + "\\remote").equals(this.currentLoc)) {
                this.currentEjb.setRemote(value);
            } else if ((base + "\\ejb-class").equals(this.currentLoc)) {
                this.currentEjb.setImplementation(value);
            } else if ((base + "\\prim-key-class").equals(this.currentLoc)) {
                this.currentEjb.setPrimaryKey(value);
            } else if ((base + "\\session-type").equals(this.currentLoc)) {
                this.currentEjb.setBeantype(value);
            } else if ((base + "\\persistence-type").equals(this.currentLoc)) {
                this.currentEjb.setCmp(value);
            }
        }

        private void iasCharacters(String value) {
            String base = "\\ias-ejb-jar\\enterprise-beans\\" + this.ejbType;
            if ((base + "\\ejb-name").equals(this.currentLoc)) {
                this.currentEjb = this.ejbs.computeIfAbsent(value, x$0 -> {
                    return new EjbInfo(x$0);
                });
            } else if ((base + "\\iiop").equals(this.currentLoc)) {
                this.currentEjb.setIiop(value);
            } else if ((base + "\\failover-required").equals(this.currentLoc)) {
                this.currentEjb.setHasession(value);
            } else if ((base + "\\persistence-manager\\properties-file-location").equals(this.currentLoc)) {
                this.currentEjb.addCmpDescriptor(value);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/IPlanetEjbc$EjbInfo.class */
    public class EjbInfo {
        private String name;
        private Classname home;
        private Classname remote;
        private Classname implementation;
        private Classname primaryKey;
        private String beantype = "entity";
        private boolean cmp = false;
        private boolean iiop = false;
        private boolean hasession = false;
        private List<String> cmpDescriptors = new ArrayList();

        public EjbInfo(String name) {
            this.name = name;
        }

        public String getName() {
            if (this.name == null) {
                if (this.implementation == null) {
                    return "[unnamed]";
                }
                return this.implementation.getClassName();
            }
            return this.name;
        }

        public void setHome(String home) {
            setHome(new Classname(home));
        }

        public void setHome(Classname home) {
            this.home = home;
        }

        public Classname getHome() {
            return this.home;
        }

        public void setRemote(String remote) {
            setRemote(new Classname(remote));
        }

        public void setRemote(Classname remote) {
            this.remote = remote;
        }

        public Classname getRemote() {
            return this.remote;
        }

        public void setImplementation(String implementation) {
            setImplementation(new Classname(implementation));
        }

        public void setImplementation(Classname implementation) {
            this.implementation = implementation;
        }

        public Classname getImplementation() {
            return this.implementation;
        }

        public void setPrimaryKey(String primaryKey) {
            setPrimaryKey(new Classname(primaryKey));
        }

        public void setPrimaryKey(Classname primaryKey) {
            this.primaryKey = primaryKey;
        }

        public Classname getPrimaryKey() {
            return this.primaryKey;
        }

        public void setBeantype(String beantype) {
            this.beantype = beantype.toLowerCase();
        }

        public String getBeantype() {
            return this.beantype;
        }

        public void setCmp(boolean cmp) {
            this.cmp = cmp;
        }

        public void setCmp(String cmp) {
            setCmp("Container".equals(cmp));
        }

        public boolean getCmp() {
            return this.cmp;
        }

        public void setIiop(boolean iiop) {
            this.iiop = iiop;
        }

        public void setIiop(String iiop) {
            setIiop(Boolean.parseBoolean(iiop));
        }

        public boolean getIiop() {
            return this.iiop;
        }

        public void setHasession(boolean hasession) {
            this.hasession = hasession;
        }

        public void setHasession(String hasession) {
            setHasession(Boolean.parseBoolean(hasession));
        }

        public boolean getHasession() {
            return this.hasession;
        }

        public void addCmpDescriptor(String descriptor) {
            this.cmpDescriptors.add(descriptor);
        }

        public List<String> getCmpDescriptors() {
            return this.cmpDescriptors;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkConfiguration(File buildDir) throws EjbcException {
            if (this.home == null) {
                throw new EjbcException("A home interface was not found for the " + this.name + " EJB.");
            }
            if (this.remote == null) {
                throw new EjbcException("A remote interface was not found for the " + this.name + " EJB.");
            }
            if (this.implementation == null) {
                throw new EjbcException("An EJB implementation class was not found for the " + this.name + " EJB.");
            }
            if (!this.beantype.equals("entity") && !this.beantype.equals(IPlanetEjbc.STATELESS_SESSION) && !this.beantype.equals(IPlanetEjbc.STATEFUL_SESSION)) {
                throw new EjbcException("The beantype found (" + this.beantype + ") isn't valid in the " + this.name + " EJB.");
            }
            if (this.cmp && !this.beantype.equals("entity")) {
                System.out.println("CMP stubs and skeletons may not be generated for a Session Bean -- the \"cmp\" attribute will be ignoredfor the " + this.name + " EJB.");
            }
            if (this.hasession && !this.beantype.equals(IPlanetEjbc.STATEFUL_SESSION)) {
                System.out.println("Highly available stubs and skeletons may only be generated for a Stateful Session Bean-- the \"hasession\" attribute will be ignored for the " + this.name + " EJB.");
            }
            if (!this.remote.getClassFile(buildDir).exists()) {
                throw new EjbcException("The remote interface " + this.remote.getQualifiedClassName() + " could not be found.");
            }
            if (!this.home.getClassFile(buildDir).exists()) {
                throw new EjbcException("The home interface " + this.home.getQualifiedClassName() + " could not be found.");
            }
            if (!this.implementation.getClassFile(buildDir).exists()) {
                throw new EjbcException("The EJB implementation class " + this.implementation.getQualifiedClassName() + " could not be found.");
            }
        }

        public boolean mustBeRecompiled(File destDir) {
            long sourceModified = sourceClassesModified(destDir);
            long destModified = destClassesModified(destDir);
            return destModified < sourceModified;
        }

        private long sourceClassesModified(File buildDir) {
            File pkFile;
            File remoteFile = this.remote.getClassFile(buildDir);
            long modified = remoteFile.lastModified();
            if (modified == -1) {
                System.out.println("The class " + this.remote.getQualifiedClassName() + " couldn't be found on the classpath");
                return -1L;
            }
            File homeFile = this.home.getClassFile(buildDir);
            long modified2 = homeFile.lastModified();
            if (modified2 == -1) {
                System.out.println("The class " + this.home.getQualifiedClassName() + " couldn't be found on the classpath");
                return -1L;
            }
            long latestModified = Math.max(modified, modified2);
            if (this.primaryKey != null) {
                pkFile = this.primaryKey.getClassFile(buildDir);
                long modified3 = pkFile.lastModified();
                if (modified3 == -1) {
                    System.out.println("The class " + this.primaryKey.getQualifiedClassName() + "couldn't be found on the classpath");
                    return -1L;
                }
                latestModified = Math.max(latestModified, modified3);
            } else {
                pkFile = null;
            }
            File implFile = this.implementation.getClassFile(buildDir);
            if (implFile.lastModified() == -1) {
                System.out.println("The class " + this.implementation.getQualifiedClassName() + " couldn't be found on the classpath");
                return -1L;
            }
            String pathToFile = this.remote.getQualifiedClassName();
            IPlanetEjbc.this.ejbFiles.put(pathToFile.replace('.', File.separatorChar) + ".class", remoteFile);
            String pathToFile2 = this.home.getQualifiedClassName();
            IPlanetEjbc.this.ejbFiles.put(pathToFile2.replace('.', File.separatorChar) + ".class", homeFile);
            String pathToFile3 = this.implementation.getQualifiedClassName();
            IPlanetEjbc.this.ejbFiles.put(pathToFile3.replace('.', File.separatorChar) + ".class", implFile);
            if (pkFile != null) {
                String pathToFile4 = this.primaryKey.getQualifiedClassName();
                IPlanetEjbc.this.ejbFiles.put(pathToFile4.replace('.', File.separatorChar) + ".class", pkFile);
            }
            return latestModified;
        }

        private long destClassesModified(File destDir) {
            String[] classnames = classesToGenerate();
            long destClassesModified = Instant.now().toEpochMilli();
            boolean allClassesFound = true;
            for (String classname : classnames) {
                String pathToClass = classname.replace('.', File.separatorChar) + ".class";
                File classFile = new File(destDir, pathToClass);
                IPlanetEjbc.this.ejbFiles.put(pathToClass, classFile);
                allClassesFound = allClassesFound && classFile.exists();
                if (allClassesFound) {
                    long fileMod = classFile.lastModified();
                    destClassesModified = Math.min(destClassesModified, fileMod);
                }
            }
            if (allClassesFound) {
                return destClassesModified;
            }
            return -1L;
        }

        private String[] classesToGenerate() {
            String[] strArr;
            if (this.iiop) {
                strArr = new String[15];
            } else {
                strArr = new String[9];
            }
            String[] classnames = strArr;
            String remotePkg = this.remote.getPackageName() + ".";
            String remoteClass = this.remote.getClassName();
            String homePkg = this.home.getPackageName() + ".";
            String homeClass = this.home.getClassName();
            String implPkg = this.implementation.getPackageName() + ".";
            String implFullClass = this.implementation.getQualifiedWithUnderscores();
            int index = 0 + 1;
            classnames[0] = implPkg + "ejb_fac_" + implFullClass;
            int index2 = index + 1;
            classnames[index] = implPkg + "ejb_home_" + implFullClass;
            int index3 = index2 + 1;
            classnames[index2] = implPkg + "ejb_skel_" + implFullClass;
            int index4 = index3 + 1;
            classnames[index3] = remotePkg + "ejb_kcp_skel_" + remoteClass;
            int index5 = index4 + 1;
            classnames[index4] = homePkg + "ejb_kcp_skel_" + homeClass;
            int index6 = index5 + 1;
            classnames[index5] = remotePkg + "ejb_kcp_stub_" + remoteClass;
            int index7 = index6 + 1;
            classnames[index6] = homePkg + "ejb_kcp_stub_" + homeClass;
            int index8 = index7 + 1;
            classnames[index7] = remotePkg + "ejb_stub_" + remoteClass;
            int index9 = index8 + 1;
            classnames[index8] = homePkg + "ejb_stub_" + homeClass;
            if (!this.iiop) {
                return classnames;
            }
            int index10 = index9 + 1;
            classnames[index9] = "org.omg.stub." + remotePkg + "_" + remoteClass + DefaultRmicAdapter.RMI_STUB_SUFFIX;
            int index11 = index10 + 1;
            classnames[index10] = "org.omg.stub." + homePkg + "_" + homeClass + DefaultRmicAdapter.RMI_STUB_SUFFIX;
            int index12 = index11 + 1;
            classnames[index11] = "org.omg.stub." + remotePkg + "_ejb_RmiCorbaBridge_" + remoteClass + DefaultRmicAdapter.RMI_TIE_SUFFIX;
            int index13 = index12 + 1;
            classnames[index12] = "org.omg.stub." + homePkg + "_ejb_RmiCorbaBridge_" + homeClass + DefaultRmicAdapter.RMI_TIE_SUFFIX;
            int index14 = index13 + 1;
            classnames[index13] = remotePkg + "ejb_RmiCorbaBridge_" + remoteClass;
            int i = index14 + 1;
            classnames[index14] = homePkg + "ejb_RmiCorbaBridge_" + homeClass;
            return classnames;
        }

        public String toString() {
            StringBuilder s = new StringBuilder("EJB name: " + this.name + "\n\r              home:      " + this.home + "\n\r              remote:    " + this.remote + "\n\r              impl:      " + this.implementation + "\n\r              primaryKey: " + this.primaryKey + "\n\r              beantype:  " + this.beantype + "\n\r              cmp:       " + this.cmp + "\n\r              iiop:      " + this.iiop + "\n\r              hasession: " + this.hasession);
            for (String cmpDescriptor : this.cmpDescriptors) {
                s.append("\n\r              CMP Descriptor: ").append(cmpDescriptor);
            }
            return s.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/IPlanetEjbc$Classname.class */
    public static class Classname {
        private String qualifiedName;
        private String packageName;
        private String className;

        public Classname(String qualifiedName) {
            if (qualifiedName == null) {
                return;
            }
            this.qualifiedName = qualifiedName;
            int index = qualifiedName.lastIndexOf(46);
            if (index == -1) {
                this.className = qualifiedName;
                this.packageName = "";
                return;
            }
            this.packageName = qualifiedName.substring(0, index);
            this.className = qualifiedName.substring(index + 1);
        }

        public String getQualifiedClassName() {
            return this.qualifiedName;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public String getClassName() {
            return this.className;
        }

        public String getQualifiedWithUnderscores() {
            return this.qualifiedName.replace('.', '_');
        }

        public File getClassFile(File directory) {
            String pathToFile = this.qualifiedName.replace('.', File.separatorChar) + ".class";
            return new File(directory, pathToFile);
        }

        public String toString() {
            return getQualifiedClassName();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/IPlanetEjbc$RedirectOutput.class */
    public static class RedirectOutput extends Thread {
        private InputStream stream;

        public RedirectOutput(InputStream stream) {
            this.stream = stream;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(this.stream));
                while (true) {
                    String text = reader.readLine();
                    if (text != null) {
                        System.out.println(text);
                    } else {
                        reader.close();
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
