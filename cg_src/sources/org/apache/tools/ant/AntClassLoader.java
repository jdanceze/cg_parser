package org.apache.tools.ant;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.ant.launch.Locator;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.LoaderUtils;
import org.apache.tools.ant.util.ReflectUtil;
import org.apache.tools.ant.util.StringUtils;
import org.apache.tools.ant.util.VectorSet;
import org.apache.tools.zip.ZipLong;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/AntClassLoader.class */
public class AntClassLoader extends ClassLoader implements SubBuildListener, Closeable {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private static final boolean IS_ATLEAST_JAVA9 = JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.JAVA_9);
    private static final Class[] MR_JARFILE_CTOR_ARGS;
    private static final Object MR_JARFILE_CTOR_RUNTIME_VERSION_VAL;
    private static final int BUFFER_SIZE = 8192;
    private static final int NUMBER_OF_STRINGS = 256;
    private final Vector<File> pathComponents;
    private Project project;
    private boolean parentFirst;
    private final Vector<String> systemPackages;
    private final Vector<String> loaderPackages;
    private boolean ignoreBase;
    private ClassLoader parent;
    private Hashtable<File, JarFile> jarFiles;
    private static Map<String, String> pathMap;
    private ClassLoader savedContextLoader;
    private boolean isContextLoaderSaved;
    private static final ZipLong EOCD_SIG;
    private static final ZipLong SINGLE_SEGMENT_SPLIT_MARKER;

    static {
        registerAsParallelCapable();
        if (IS_ATLEAST_JAVA9) {
            Class[] ctorArgs = null;
            Object runtimeVersionVal = null;
            try {
                Class<?> runtimeVersionClass = Class.forName("java.lang.Runtime$Version");
                ctorArgs = new Class[]{File.class, Boolean.TYPE, Integer.TYPE, runtimeVersionClass};
                runtimeVersionVal = Runtime.class.getDeclaredMethod("version", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
            }
            MR_JARFILE_CTOR_ARGS = ctorArgs;
            MR_JARFILE_CTOR_RUNTIME_VERSION_VAL = runtimeVersionVal;
        } else {
            MR_JARFILE_CTOR_ARGS = null;
            MR_JARFILE_CTOR_RUNTIME_VERSION_VAL = null;
        }
        pathMap = Collections.synchronizedMap(new HashMap());
        EOCD_SIG = new ZipLong(101010256L);
        SINGLE_SEGMENT_SPLIT_MARKER = new ZipLong(808471376L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/AntClassLoader$ResourceEnumeration.class */
    public class ResourceEnumeration implements Enumeration<URL> {
        private final String resourceName;
        private int pathElementsIndex = 0;
        private URL nextResource;

        ResourceEnumeration(String name) {
            this.resourceName = name;
            findNextResource();
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.nextResource != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        public URL nextElement() {
            URL ret = this.nextResource;
            if (ret == null) {
                throw new NoSuchElementException();
            }
            findNextResource();
            return ret;
        }

        private void findNextResource() {
            URL url = null;
            while (this.pathElementsIndex < AntClassLoader.this.pathComponents.size() && url == null) {
                try {
                    File pathComponent = (File) AntClassLoader.this.pathComponents.elementAt(this.pathElementsIndex);
                    url = AntClassLoader.this.getResourceURL(pathComponent, this.resourceName);
                    this.pathElementsIndex++;
                } catch (BuildException e) {
                }
            }
            this.nextResource = url;
        }
    }

    public AntClassLoader(ClassLoader parent, Project project, Path classpath) {
        this.pathComponents = new VectorSet();
        this.parentFirst = true;
        this.systemPackages = new Vector<>();
        this.loaderPackages = new Vector<>();
        this.ignoreBase = false;
        this.parent = null;
        this.jarFiles = new Hashtable<>();
        this.savedContextLoader = null;
        this.isContextLoaderSaved = false;
        setParent(parent);
        setClassPath(classpath);
        setProject(project);
    }

    public AntClassLoader() {
        this.pathComponents = new VectorSet();
        this.parentFirst = true;
        this.systemPackages = new Vector<>();
        this.loaderPackages = new Vector<>();
        this.ignoreBase = false;
        this.parent = null;
        this.jarFiles = new Hashtable<>();
        this.savedContextLoader = null;
        this.isContextLoaderSaved = false;
        setParent(null);
    }

    public AntClassLoader(Project project, Path classpath) {
        this.pathComponents = new VectorSet();
        this.parentFirst = true;
        this.systemPackages = new Vector<>();
        this.loaderPackages = new Vector<>();
        this.ignoreBase = false;
        this.parent = null;
        this.jarFiles = new Hashtable<>();
        this.savedContextLoader = null;
        this.isContextLoaderSaved = false;
        setParent(null);
        setProject(project);
        setClassPath(classpath);
    }

    public AntClassLoader(ClassLoader parent, Project project, Path classpath, boolean parentFirst) {
        this(project, classpath);
        if (parent != null) {
            setParent(parent);
        }
        setParentFirst(parentFirst);
        addJavaLibraries();
    }

    public AntClassLoader(Project project, Path classpath, boolean parentFirst) {
        this(null, project, classpath, parentFirst);
    }

    public AntClassLoader(ClassLoader parent, boolean parentFirst) {
        this.pathComponents = new VectorSet();
        this.parentFirst = true;
        this.systemPackages = new Vector<>();
        this.loaderPackages = new Vector<>();
        this.ignoreBase = false;
        this.parent = null;
        this.jarFiles = new Hashtable<>();
        this.savedContextLoader = null;
        this.isContextLoaderSaved = false;
        setParent(parent);
        this.project = null;
        this.parentFirst = parentFirst;
    }

    public void setProject(Project project) {
        this.project = project;
        if (project != null) {
            project.addBuildListener(this);
        }
    }

    public void setClassPath(Path classpath) {
        String[] list;
        this.pathComponents.removeAllElements();
        if (classpath != null) {
            for (String pathElement : classpath.concatSystemClasspath(Definer.OnError.POLICY_IGNORE).list()) {
                try {
                    addPathElement(pathElement);
                } catch (BuildException e) {
                    log("Ignoring path element " + pathElement + " from classpath due to exception " + e, 4);
                }
            }
        }
    }

    public void setParent(ClassLoader parent) {
        this.parent = parent == null ? AntClassLoader.class.getClassLoader() : parent;
    }

    public void setParentFirst(boolean parentFirst) {
        this.parentFirst = parentFirst;
    }

    protected void log(String message, int priority) {
        if (this.project != null) {
            this.project.log(message, priority);
        } else if (priority < 2) {
            System.err.println(message);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setThreadContextLoader() {
        if (this.isContextLoaderSaved) {
            throw new BuildException("Context loader has not been reset");
        }
        if (LoaderUtils.isContextLoaderAvailable()) {
            this.savedContextLoader = LoaderUtils.getContextClassLoader();
            ClassLoader loader = this;
            if (this.project != null && "only".equals(this.project.getProperty(MagicNames.BUILD_SYSCLASSPATH))) {
                loader = getClass().getClassLoader();
            }
            LoaderUtils.setContextClassLoader(loader);
            this.isContextLoaderSaved = true;
        }
    }

    public void resetThreadContextLoader() {
        if (LoaderUtils.isContextLoaderAvailable() && this.isContextLoaderSaved) {
            LoaderUtils.setContextClassLoader(this.savedContextLoader);
            this.savedContextLoader = null;
            this.isContextLoaderSaved = false;
        }
    }

    public void addPathElement(String pathElement) throws BuildException {
        File pathComponent = this.project != null ? this.project.resolveFile(pathElement) : new File(pathElement);
        try {
            addPathFile(pathComponent);
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    public void addPathComponent(File file) {
        if (this.pathComponents.contains(file)) {
            return;
        }
        this.pathComponents.addElement(file);
    }

    protected void addPathFile(File pathComponent) throws IOException {
        if (!this.pathComponents.contains(pathComponent)) {
            this.pathComponents.addElement(pathComponent);
        }
        if (pathComponent.isDirectory()) {
            return;
        }
        String absPathPlusTimeAndLength = pathComponent.getAbsolutePath() + pathComponent.lastModified() + HelpFormatter.DEFAULT_OPT_PREFIX + pathComponent.length();
        String classpath = pathMap.get(absPathPlusTimeAndLength);
        if (classpath == null) {
            JarFile jarFile = newJarFile(pathComponent);
            try {
                Manifest manifest = jarFile.getManifest();
                if (manifest != null) {
                    classpath = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH);
                    if (jarFile != null) {
                        jarFile.close();
                    }
                    if (classpath == null) {
                        classpath = "";
                    }
                    pathMap.put(absPathPlusTimeAndLength, classpath);
                } else if (jarFile != null) {
                    jarFile.close();
                    return;
                } else {
                    return;
                }
            } catch (Throwable th) {
                if (jarFile != null) {
                    try {
                        jarFile.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (!classpath.isEmpty()) {
            URL baseURL = FILE_UTILS.getFileURL(pathComponent);
            StringTokenizer st = new StringTokenizer(classpath);
            while (st.hasMoreTokens()) {
                String classpathElement = st.nextToken();
                URL libraryURL = new URL(baseURL, classpathElement);
                if (!libraryURL.getProtocol().equals("file")) {
                    log("Skipping jar library " + classpathElement + " since only relative URLs are supported by this loader", 3);
                } else {
                    String decodedPath = Locator.decodeUri(libraryURL.getFile());
                    File libraryFile = new File(decodedPath);
                    if (libraryFile.exists() && !isInPath(libraryFile)) {
                        addPathFile(libraryFile);
                    }
                }
            }
        }
    }

    public String getClasspath() {
        StringBuilder sb = new StringBuilder();
        Iterator<File> it = this.pathComponents.iterator();
        while (it.hasNext()) {
            File component = it.next();
            if (sb.length() > 0) {
                sb.append(File.pathSeparator);
            }
            sb.append(component.getAbsolutePath());
        }
        return sb.toString();
    }

    public synchronized void setIsolated(boolean isolated) {
        this.ignoreBase = isolated;
    }

    @Deprecated
    public static void initializeClass(Class<?> theClass) {
        Constructor<?>[] cons = theClass.getDeclaredConstructors();
        if (cons != null && cons.length > 0 && cons[0] != null) {
            String[] strs = new String[256];
            try {
                cons[0].newInstance(strs);
            } catch (Exception e) {
            }
        }
    }

    public void addSystemPackageRoot(String packageRoot) {
        this.systemPackages.addElement(packageRoot + (packageRoot.endsWith(".") ? "" : "."));
    }

    public void addLoaderPackageRoot(String packageRoot) {
        this.loaderPackages.addElement(packageRoot + (packageRoot.endsWith(".") ? "" : "."));
    }

    public Class<?> forceLoadClass(String classname) throws ClassNotFoundException {
        log("force loading " + classname, 4);
        Class<?> theClass = findLoadedClass(classname);
        if (theClass == null) {
            theClass = findClass(classname);
        }
        return theClass;
    }

    public Class<?> forceLoadSystemClass(String classname) throws ClassNotFoundException {
        log("force system loading " + classname, 4);
        Class<?> theClass = findLoadedClass(classname);
        if (theClass == null) {
            theClass = findBaseClass(classname);
        }
        return theClass;
    }

    @Override // java.lang.ClassLoader
    public InputStream getResourceAsStream(String name) {
        InputStream resourceAsStream;
        InputStream resourceStream = null;
        if (isParentFirst(name)) {
            resourceStream = loadBaseResource(name);
        }
        if (resourceStream != null) {
            log("ResourceStream for " + name + " loaded from parent loader", 4);
        } else {
            resourceStream = loadResource(name);
            if (resourceStream != null) {
                log("ResourceStream for " + name + " loaded from ant loader", 4);
            }
        }
        if (resourceStream == null && !isParentFirst(name)) {
            if (this.ignoreBase) {
                if (getRootLoader() == null) {
                    resourceAsStream = null;
                } else {
                    resourceAsStream = getRootLoader().getResourceAsStream(name);
                }
                resourceStream = resourceAsStream;
            } else {
                resourceStream = loadBaseResource(name);
            }
            if (resourceStream != null) {
                log("ResourceStream for " + name + " loaded from parent loader", 4);
            }
        }
        if (resourceStream == null) {
            log("Couldn't load ResourceStream for " + name, 4);
        }
        return resourceStream;
    }

    private InputStream loadResource(String name) {
        return (InputStream) this.pathComponents.stream().map(path -> {
            return getResourceStream(name, name);
        }).filter((v0) -> {
            return Objects.nonNull(v0);
        }).findFirst().orElse(null);
    }

    private InputStream loadBaseResource(String name) {
        return this.parent == null ? super.getResourceAsStream(name) : this.parent.getResourceAsStream(name);
    }

    private InputStream getResourceStream(File file, String resourceName) {
        try {
            JarFile jarFile = this.jarFiles.get(file);
            if (jarFile == null && file.isDirectory()) {
                File resource = new File(file, resourceName);
                if (resource.exists()) {
                    return Files.newInputStream(resource.toPath(), new OpenOption[0]);
                }
                return null;
            }
            if (jarFile == null) {
                if (file.exists()) {
                    this.jarFiles.put(file, newJarFile(file));
                    jarFile = this.jarFiles.get(file);
                } else {
                    return null;
                }
            }
            JarEntry entry = jarFile.getJarEntry(resourceName);
            if (entry != null) {
                return jarFile.getInputStream(entry);
            }
            return null;
        } catch (Exception e) {
            log("Ignoring Exception " + e.getClass().getName() + ": " + e.getMessage() + " reading resource " + resourceName + " from " + file, 3);
            return null;
        }
    }

    private boolean isParentFirst(String resourceName) {
        Stream stream = this.loaderPackages.stream();
        Objects.requireNonNull(resourceName);
        if (stream.noneMatch(this::startsWith)) {
            Stream stream2 = this.systemPackages.stream();
            Objects.requireNonNull(resourceName);
            if (stream2.anyMatch(this::startsWith) || this.parentFirst) {
                return true;
            }
        }
        return false;
    }

    private ClassLoader getRootLoader() {
        ClassLoader ret;
        ClassLoader classLoader = getClass().getClassLoader();
        while (true) {
            ret = classLoader;
            if (ret == null || ret.getParent() == null) {
                break;
            }
            classLoader = ret.getParent();
        }
        return ret;
    }

    @Override // java.lang.ClassLoader
    public URL getResource(String name) {
        URL url = null;
        if (isParentFirst(name)) {
            url = this.parent == null ? super.getResource(name) : this.parent.getResource(name);
        }
        if (url != null) {
            log("Resource " + name + " loaded from parent loader", 4);
        } else {
            Iterator<File> it = this.pathComponents.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                File pathComponent = it.next();
                url = getResourceURL(pathComponent, name);
                if (url != null) {
                    log("Resource " + name + " loaded from ant loader", 4);
                    break;
                }
            }
        }
        if (url == null && !isParentFirst(name)) {
            if (this.ignoreBase) {
                url = getRootLoader() == null ? null : getRootLoader().getResource(name);
            } else {
                url = this.parent == null ? super.getResource(name) : this.parent.getResource(name);
            }
            if (url != null) {
                log("Resource " + name + " loaded from parent loader", 4);
            }
        }
        if (url == null) {
            log("Couldn't load Resource " + name, 4);
        }
        return url;
    }

    public Enumeration<URL> getNamedResources(String name) throws IOException {
        return findResources(name, false);
    }

    @Override // java.lang.ClassLoader
    protected Enumeration<URL> findResources(String name) throws IOException {
        return findResources(name, true);
    }

    protected Enumeration<URL> findResources(String name, boolean parentHasBeenSearched) throws IOException {
        Enumeration<URL> base;
        Enumeration<URL> mine = new ResourceEnumeration(name);
        if (this.parent != null && (!parentHasBeenSearched || this.parent != getParent())) {
            base = this.parent.getResources(name);
        } else {
            base = Collections.emptyEnumeration();
        }
        if (isParentFirst(name)) {
            return append(base, mine);
        }
        if (this.ignoreBase) {
            return getRootLoader() == null ? mine : append(mine, getRootLoader().getResources(name));
        }
        return append(mine, base);
    }

    private static Enumeration<URL> append(Enumeration<URL> one, Enumeration<URL> two) {
        return (Enumeration) Stream.concat(Collections.list(one).stream(), Collections.list(two).stream()).collect(Collectors.collectingAndThen(Collectors.toList(), (v0) -> {
            return Collections.enumeration(v0);
        }));
    }

    protected URL getResourceURL(File file, String resourceName) {
        try {
            JarFile jarFile = this.jarFiles.get(file);
            if (jarFile == null && file.isDirectory()) {
                File resource = new File(file, resourceName);
                if (resource.exists()) {
                    try {
                        return FILE_UTILS.getFileURL(resource);
                    } catch (MalformedURLException e) {
                        return null;
                    }
                }
                return null;
            }
            if (jarFile == null) {
                if (file.exists()) {
                    if (!isZip(file)) {
                        String msg = "CLASSPATH element " + file + " is not a JAR.";
                        log(msg, 1);
                        return null;
                    }
                    this.jarFiles.put(file, newJarFile(file));
                    jarFile = this.jarFiles.get(file);
                } else {
                    return null;
                }
            }
            JarEntry entry = jarFile.getJarEntry(resourceName);
            if (entry != null) {
                try {
                    return new URL("jar:" + FILE_UTILS.getFileURL(file) + "!/" + entry);
                } catch (MalformedURLException e2) {
                    return null;
                }
            }
            return null;
        } catch (Exception e3) {
            String msg2 = "Unable to obtain resource from " + file + ": ";
            log(msg2 + e3, 1);
            log(StringUtils.getStackTrace(e3), 1);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.lang.ClassLoader
    public synchronized Class<?> loadClass(String classname, boolean resolve) throws ClassNotFoundException {
        Class<?> theClass;
        Class<?> theClass2 = findLoadedClass(classname);
        if (theClass2 != null) {
            return theClass2;
        }
        if (isParentFirst(classname)) {
            try {
                theClass = findBaseClass(classname);
                log("Class " + classname + " loaded from parent loader (parentFirst)", 4);
            } catch (ClassNotFoundException e) {
                theClass = findClass(classname);
                log("Class " + classname + " loaded from ant loader (parentFirst)", 4);
            }
        } else {
            try {
                theClass = findClass(classname);
                log("Class " + classname + " loaded from ant loader", 4);
            } catch (ClassNotFoundException cnfe) {
                if (this.ignoreBase) {
                    throw cnfe;
                }
                theClass = findBaseClass(classname);
                log("Class " + classname + " loaded from parent loader", 4);
            }
        }
        if (resolve) {
            resolveClass(theClass);
        }
        return theClass;
    }

    private String getClassFilename(String classname) {
        return classname.replace('.', '/') + ".class";
    }

    protected Class<?> defineClassFromData(File container, byte[] classData, String classname) throws IOException {
        definePackage(container, classname);
        ProtectionDomain currentPd = Project.class.getProtectionDomain();
        String classResource = getClassFilename(classname);
        CodeSource src = new CodeSource(FILE_UTILS.getFileURL(container), getCertificates(container, classResource));
        ProtectionDomain classesPd = new ProtectionDomain(src, currentPd.getPermissions(), this, currentPd.getPrincipals());
        return defineClass(classname, classData, 0, classData.length, classesPd);
    }

    protected void definePackage(File container, String className) throws IOException {
        int classIndex = className.lastIndexOf(46);
        if (classIndex == -1) {
            return;
        }
        String packageName = className.substring(0, classIndex);
        if (getPackage(packageName) != null) {
            return;
        }
        Manifest manifest = getJarManifest(container);
        if (manifest == null) {
            definePackage(packageName, null, null, null, null, null, null, null);
        } else {
            definePackage(container, packageName, manifest);
        }
    }

    private Manifest getJarManifest(File container) throws IOException {
        JarFile jarFile;
        if (container.isDirectory() || (jarFile = this.jarFiles.get(container)) == null) {
            return null;
        }
        return jarFile.getManifest();
    }

    private Certificate[] getCertificates(File container, String entry) {
        JarFile jarFile;
        JarEntry ent;
        if (container.isDirectory() || (jarFile = this.jarFiles.get(container)) == null || (ent = jarFile.getJarEntry(entry)) == null) {
            return null;
        }
        return ent.getCertificates();
    }

    protected void definePackage(File container, String packageName, Manifest manifest) {
        String sectionName = packageName.replace('.', '/') + "/";
        String specificationTitle = null;
        String specificationVendor = null;
        String specificationVersion = null;
        String implementationTitle = null;
        String implementationVendor = null;
        String implementationVersion = null;
        String sealedString = null;
        URL sealBase = null;
        Attributes sectionAttributes = manifest.getAttributes(sectionName);
        if (sectionAttributes != null) {
            specificationTitle = sectionAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
            specificationVendor = sectionAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            specificationVersion = sectionAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
            implementationTitle = sectionAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            implementationVendor = sectionAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            implementationVersion = sectionAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            sealedString = sectionAttributes.getValue(Attributes.Name.SEALED);
        }
        Attributes mainAttributes = manifest.getMainAttributes();
        if (mainAttributes != null) {
            if (specificationTitle == null) {
                specificationTitle = mainAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
            }
            if (specificationVendor == null) {
                specificationVendor = mainAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            }
            if (specificationVersion == null) {
                specificationVersion = mainAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
            }
            if (implementationTitle == null) {
                implementationTitle = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            }
            if (implementationVendor == null) {
                implementationVendor = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            }
            if (implementationVersion == null) {
                implementationVersion = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            }
            if (sealedString == null) {
                sealedString = mainAttributes.getValue(Attributes.Name.SEALED);
            }
        }
        if (sealedString != null && sealedString.equalsIgnoreCase("true")) {
            try {
                sealBase = new URL(FileUtils.getFileUtils().toURI(container.getAbsolutePath()));
            } catch (MalformedURLException e) {
            }
        }
        definePackage(packageName, specificationTitle, specificationVersion, specificationVendor, implementationTitle, implementationVersion, implementationVendor, sealBase);
    }

    private Class<?> getClassFromStream(InputStream stream, String classname, File container) throws IOException, SecurityException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        while (true) {
            int bytesRead = stream.read(buffer, 0, 8192);
            if (bytesRead != -1) {
                baos.write(buffer, 0, bytesRead);
            } else {
                byte[] classData = baos.toByteArray();
                return defineClassFromData(container, classData, classname);
            }
        }
    }

    @Override // java.lang.ClassLoader
    public Class<?> findClass(String name) throws ClassNotFoundException {
        log("Finding class " + name, 4);
        return findClassInComponents(name);
    }

    protected boolean isInPath(File component) {
        return this.pathComponents.contains(component);
    }

    private Class<?> findClassInComponents(String name) throws ClassNotFoundException {
        InputStream stream;
        String classFilename = getClassFilename(name);
        Iterator<File> it = this.pathComponents.iterator();
        while (it.hasNext()) {
            File pathComponent = it.next();
            try {
                stream = getResourceStream(pathComponent, classFilename);
            } catch (IOException ioe) {
                log("Exception reading component " + pathComponent + " (reason: " + ioe.getMessage() + ")", 3);
            } catch (SecurityException se) {
                throw se;
            }
            if (stream != null) {
                log("Loaded from " + pathComponent + Instruction.argsep + classFilename, 4);
                Class<?> classFromStream = getClassFromStream(stream, name, pathComponent);
                if (stream != null) {
                    stream.close();
                }
                return classFromStream;
            } else if (stream != null) {
                stream.close();
            }
        }
        throw new ClassNotFoundException(name);
    }

    private Class<?> findBaseClass(String name) throws ClassNotFoundException {
        return this.parent == null ? findSystemClass(name) : this.parent.loadClass(name);
    }

    public synchronized void cleanup() {
        for (JarFile jarFile : this.jarFiles.values()) {
            FileUtils.close(jarFile);
        }
        this.jarFiles = new Hashtable<>();
        if (this.project != null) {
            this.project.removeBuildListener(this);
        }
        this.project = null;
    }

    public ClassLoader getConfiguredParent() {
        return this.parent;
    }

    @Override // org.apache.tools.ant.BuildListener
    public void buildStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void buildFinished(BuildEvent event) {
        cleanup();
    }

    @Override // org.apache.tools.ant.SubBuildListener
    public void subBuildFinished(BuildEvent event) {
        if (event.getProject() == this.project) {
            cleanup();
        }
    }

    @Override // org.apache.tools.ant.SubBuildListener
    public void subBuildStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void targetStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void targetFinished(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void taskStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void taskFinished(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void messageLogged(BuildEvent event) {
    }

    public void addJavaLibraries() {
        JavaEnvUtils.getJrePackages().forEach(this::addSystemPackageRoot);
    }

    public String toString() {
        return "AntClassLoader[" + getClasspath() + "]";
    }

    @Override // java.lang.ClassLoader
    public Enumeration<URL> getResources(String name) throws IOException {
        return getNamedResources(name);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        cleanup();
    }

    public static AntClassLoader newAntClassLoader(ClassLoader parent, Project project, Path path, boolean parentFirst) {
        return new AntClassLoader(parent, project, path, parentFirst);
    }

    private static boolean isZip(File file) throws IOException {
        byte[] sig = new byte[4];
        if (readFully(file, sig)) {
            ZipLong start = new ZipLong(sig);
            return ZipLong.LFH_SIG.equals(start) || EOCD_SIG.equals(start) || ZipLong.DD_SIG.equals(start) || SINGLE_SEGMENT_SPLIT_MARKER.equals(start);
        }
        return false;
    }

    private static boolean readFully(File f, byte[] b) throws IOException {
        InputStream fis = Files.newInputStream(f.toPath(), new OpenOption[0]);
        try {
            int len = b.length;
            int count = 0;
            while (count != len) {
                int x = fis.read(b, count, len - count);
                if (x == -1) {
                    break;
                }
                count += x;
            }
            boolean z = count == len;
            if (fis != null) {
                fis.close();
            }
            return z;
        } catch (Throwable th) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static JarFile newJarFile(File file) throws IOException {
        if (!IS_ATLEAST_JAVA9 || MR_JARFILE_CTOR_ARGS == null || MR_JARFILE_CTOR_RUNTIME_VERSION_VAL == null) {
            return new JarFile(file);
        }
        return (JarFile) ReflectUtil.newInstance(JarFile.class, MR_JARFILE_CTOR_ARGS, new Object[]{file, true, 1, MR_JARFILE_CTOR_RUNTIME_VERSION_VAL});
    }
}
